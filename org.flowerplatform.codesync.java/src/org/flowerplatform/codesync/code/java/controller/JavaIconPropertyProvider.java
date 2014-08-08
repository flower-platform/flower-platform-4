/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.DECORATOR_ABSTRACT;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.DECORATOR_FINAL;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.DECORATOR_NATIVE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.DECORATOR_STATIC;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.DECORATOR_SYNCHRONIZED;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.DECORATOR_TRANSIENT;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.DECORATOR_VOLATILE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.VISIBILITY_DEFAULT;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.VISIBILITY_PRIVATE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.VISIBILITY_PROTECTED;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.getImagePath;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.codesync.code.java.CodeSyncJavaPlugin;
import org.flowerplatform.codesync.controller.CodeSyncControllerUtils;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

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
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		int flags = getModifiersFlags(node, context);
		
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
		return CodeSyncJavaPlugin.getInstance().getImageComposerUrl(icon, getImagePath(decorator));
	}

	protected int getModifiersFlags(Node node, ServiceContext<NodeService> context) {
		int flags = 0;
		for (Node modifier : getModifiers(node, context)) {
			if (CodeSyncControllerUtils.isRemoved(modifier)) {
				// don't decorate if the modifier was marked removed
				continue;
			}
			String keyword = (String) modifier.getPropertyValue(CoreConstants.NAME);
			if (keyword == null || keyword.length() == 0) {
				continue;
			}
			flags |= ModifierKeyword.toKeyword(keyword).toFlagValue();
		}
		return flags;
	}
	
	protected List<Node> getModifiers(Node node, ServiceContext<NodeService> context) {	
		List<Node> modifiers = new ArrayList<Node>();
		for (Node child : context.getService().getChildren(node, new ServiceContext<NodeService>(context.getService()).add(POPULATE_WITH_PROPERTIES, true))) {
			if (CodeSyncJavaConstants.MODIFIER.equals(child.getType())) {
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
