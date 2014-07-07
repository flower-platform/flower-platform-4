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
package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.getOriginalPropertyName;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.setSyncFalseAndPropagateToParents;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.setSyncTrueAndPropagateToParents;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncPropertySetter extends AbstractController implements IPropertySetter {

	public CodeSyncPropertySetter() {
		// invoked before the persistence controllers
		// to cache the current value of the property before it is overwritten
		setOrderIndex(-100000);
	}
	
	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
		// if the node is newly added or marked removed => propagate sync flag false
		if (CodeSyncConstants.REMOVED.equals(property) || CodeSyncConstants.ADDED.equals(property)) {
			setSyncFalseAndPropagateToParents(node, context.getService());
			return;
		}
		
		// check if property is synchronizable
		if (!isSyncProperty(node, property)) {
			return;
		}
		
		boolean isOriginalPropertySet = false;
		Object originalValue = null;
		String originalProperty = getOriginalPropertyName(property);
		// get the original value from property.original or property
		if (node.getOrPopulateProperties().containsKey(originalProperty)) {
			isOriginalPropertySet = true;
			originalValue = node.getPropertyValue(originalProperty);
		} else if (node.getOrPopulateProperties().containsKey(property)) {
			originalValue = node.getPropertyValue(property);
		} else {
			originalValue = value;
		}
		
		if (!Utils.safeEquals(originalValue, value)) {
			if (!isOriginalPropertySet) {
				// trying to set a different value; keep the old value in property.original if it does not exist
				context.getService().setProperty(node, originalProperty, originalValue, new ServiceContext<NodeService>(context.getService()));
				setSyncFalseAndPropagateToParents(node, context.getService());
			}
		} else {
			if (isOriginalPropertySet) {
				// trying to set the same value as the original (a revert operation); unset the original value
				context.getService().unsetProperty(node, originalProperty, new ServiceContext<NodeService>(context.getService()));
				setSyncTrueAndPropagateToParents(node, context.getService());
			}
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		// nothing to do
	}
	
	private boolean isSyncProperty(Node node, String property) {
		return !CodeSyncControllerUtils.isCodeSyncFlagConstant(property);
	}
	
}
