package org.flowerplatform.codesync.code.java.controller;

import static org.eclipse.jdt.core.dom.Modifier.isAbstract;
import static org.eclipse.jdt.core.dom.Modifier.isFinal;
import static org.eclipse.jdt.core.dom.Modifier.isNative;
import static org.eclipse.jdt.core.dom.Modifier.isPrivate;
import static org.eclipse.jdt.core.dom.Modifier.isProtected;
import static org.eclipse.jdt.core.dom.Modifier.isPublic;
import static org.eclipse.jdt.core.dom.Modifier.isStatic;
import static org.eclipse.jdt.core.dom.Modifier.isSynchronized;
import static org.eclipse.jdt.core.dom.Modifier.isTransient;
import static org.eclipse.jdt.core.dom.Modifier.isVolatile;
import static org.flowerplatform.codesync.code.java.JavaConstants.DECORATOR_ABSTRACT;
import static org.flowerplatform.codesync.code.java.JavaConstants.DECORATOR_FINAL;
import static org.flowerplatform.codesync.code.java.JavaConstants.DECORATOR_NATIVE;
import static org.flowerplatform.codesync.code.java.JavaConstants.DECORATOR_STATIC;
import static org.flowerplatform.codesync.code.java.JavaConstants.DECORATOR_SYNCHRONIZED;
import static org.flowerplatform.codesync.code.java.JavaConstants.DECORATOR_TRANSIENT;
import static org.flowerplatform.codesync.code.java.JavaConstants.DECORATOR_VOLATILE;
import static org.flowerplatform.codesync.code.java.JavaConstants.VISIBILITY_DEFAULT;
import static org.flowerplatform.codesync.code.java.JavaConstants.VISIBILITY_PRIVATE;
import static org.flowerplatform.codesync.code.java.JavaConstants.VISIBILITY_PROTECTED;
import static org.flowerplatform.codesync.code.java.JavaConstants.getImagePath;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaPlugin;
import org.flowerplatform.codesync.code.java.adapter.JavaModifierModelAdapter;
import org.flowerplatform.codesync.controller.CodeSyncControllerUtils;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * Sets the icon property, depending on the element's modifiers. The base icon depends on the 
 * element's visibility (public, protected, private, default); decorators are added for
 * other modifiers (e.g. abstract, final, static).
 * 
 * <p>
 * For info about the available icons and decorators, check {@link org.eclipse.jdt.ui.JavaElementImageDescriptor}.
 * 
 * @author Mariana Gheorghe
 */
public class JavaIconPropertyProvider extends ConstantValuePropertyProvider {
	
	public JavaIconPropertyProvider(String property, Object value) {
		super(property, value);
	}

	@Override
	public void populateWithProperties(Node node) {
		int flags = getModifiersFlags(node);
		
		// get the icon depending on visibility
		String icon = getVisibilityDefault();
		if (isPublic(flags)) {
			icon = (String) getValue();
		} else if (isProtected(flags)) {
			icon = getVisibilityProtected();
		} else if (isPrivate(flags)) {
			icon = getVisibilityPrivate();
		}
		
		// add extra decorators
		
		// top right
		if (isAbstract(flags)) {
			icon = append(icon, DECORATOR_ABSTRACT);
		}
		if (isFinal(flags)) {
			icon = append(icon, DECORATOR_FINAL);
		}
		if (isVolatile(flags)) {
			icon = append(icon, DECORATOR_VOLATILE);
		}
		if (isStatic(flags)) {
			icon = append(icon, DECORATOR_STATIC);
		}
		if (isNative(flags)) {
			icon = append(icon, DECORATOR_NATIVE);
		}
		
		// bottom right
		if (isTransient(flags)) {
			icon = append(icon, DECORATOR_TRANSIENT);
		}
		if (isSynchronized(flags)) {
			icon = append(icon, DECORATOR_SYNCHRONIZED);
		}
		
		node.getProperties().put(getProperty(), icon);
	}
	
	private String append(String icon, String decorator) {
		return CodeSyncCodeJavaPlugin.getInstance().getImageComposerUrl(icon, getImagePath(decorator));
	}

	protected int getModifiersFlags(Node node) {
		int flags = 0;
		for (Node modifier : getModifiers(node)) {
			if (CodeSyncControllerUtils.isRemoved(modifier)) {
				// don't decorate if the modifier was marked removed
				continue;
			}
			String keyword = (String) modifier.getOrPopulateProperties().get(FeatureProvider.NAME);
			if (keyword == null) {
				continue;
			}
			flags |= ModifierKeyword.toKeyword(keyword).toFlagValue();
		}
		return flags;
	}
	
	protected List<Node> getModifiers(Node node) {
		NodeService service = (NodeService) CorePlugin.getInstance().getNodeService();
		List<Node> modifiers = new ArrayList<Node>();
		for (Node child : service.getChildren(node, true)) {
			if (JavaModifierModelAdapter.MODIFIER.equals(child.getType())) {
				modifiers.add(child);
			}
		}
		return modifiers;
	}
	
	protected String getVisibilityPrivate() {
		return getIconWithVisibility(VISIBILITY_PRIVATE);
	}

	protected String getVisibilityProtected() {
		return getIconWithVisibility(VISIBILITY_PROTECTED);
	}

	protected String getVisibilityDefault() {
		return getIconWithVisibility(VISIBILITY_DEFAULT);
	}

	protected String getIconWithVisibility(String visibility) { 
		StringBuilder icon = new StringBuilder((String) getValue());
		icon.insert(icon.indexOf("_obj"), visibility);
		return icon.toString();
	}
	
}
