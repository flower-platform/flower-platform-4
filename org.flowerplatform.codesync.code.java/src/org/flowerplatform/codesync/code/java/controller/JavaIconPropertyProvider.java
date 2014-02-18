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
import static org.flowerplatform.codesync.code.java.JavaImageConstants.DECORATOR_ABSTRACT;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.DECORATOR_FINAL;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.DECORATOR_NATIVE;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.DECORATOR_STATIC;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.DECORATOR_SYNCHRONIZED;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.DECORATOR_TRANSIENT;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.DECORATOR_VOLATILE;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.VISIBILITY_DEFAULT;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.VISIBILITY_PRIVATE;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.VISIBILITY_PROTECTED;
import static org.flowerplatform.codesync.code.java.JavaImageConstants.getImagePath;
import static org.flowerplatform.codesync.code.java.adapter.JavaModifierModelAdapter.MODIFIER;
import static org.flowerplatform.codesync.code.java.feature_provider.JavaModifierFeatureProvider.MODIFIER_TYPE;

import java.util.Collections;
import java.util.List;

import org.flowerplatform.codesync.code.java.feature_provider.JavaFeaturesConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;

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
		return icon + "|" + getImagePath(decorator);
	}

	protected int getModifiersFlags(Node node) {
		int flags = 0;
		for (Node modifier : getModifiers(node)) {
			if (modifier.getType().equals(MODIFIER)) {
				if (modifier.getProperties().containsKey(MODIFIER_TYPE)) {
					flags |= (int) modifier.getProperties().get(MODIFIER_TYPE);
				}
			}
		}
		return flags;
	}
	
	protected List<Node> getModifiers(Node node) {
		NodeService service = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
		List<Node> categories = service.getChildren(node, true);
		for (Node category : categories) {
			if (category.getProperties().get("name").equals(JavaFeaturesConstants.MODIFIERS)) {
				// found the modifiers category
				return service.getChildren(category, true);
			}
		}
		return Collections.emptyList();
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
