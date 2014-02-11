/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.getOriginalPropertyName;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.isCodeSyncFlagConstant;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.isConflictPropertyName;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.isOriginalPropertyName;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.setSyncFalseAndPropagateToParents;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.setSyncTrueAndPropagateToParents;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncPropertySetter extends PropertySetter {

	public CodeSyncPropertySetter() {
		setOrderIndex(-100000);
	}
	
	@Override
	public void setProperty(Node node, String property, Object value) {
		if (property.equals("timestamp") || property.equals("icon")) {
			return; // TODO skipping all non-sync props
		}
		
		NodeService service = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
		
		// if the node is newly added or marked removed => propagate sync flag false
		if (CodeSyncPlugin.REMOVED.equals(property) || CodeSyncPlugin.ADDED.equals(property)) {
			setSyncFalseAndPropagateToParents(node, service);
		}
		
		if (isOriginalPropertyName(property) || isConflictPropertyName(property) || isCodeSyncFlagConstant(property)) {
			return;
		}
		
		boolean isOriginalPropertySet = false;
		Object originalValue = null;
		String originalProperty = getOriginalPropertyName(property);
		// get the original value from property.original or property
		if (node.getOrCreateProperties().containsKey(originalProperty)) {
			isOriginalPropertySet = true;
			originalValue = node.getOrCreateProperties().get(originalProperty);
		} else if (node.getOrCreateProperties().containsKey(property)) {
			originalValue = node.getOrCreateProperties().get(property);
		} else {
			originalValue = value;
		}
		
		if (!Utils.safeEquals(originalValue, value)) {
			if (!isOriginalPropertySet) {
				// trying to set a different value; keep the old value in property.original if it does not exist
				service.setProperty(node, originalProperty, originalValue);
				setSyncFalseAndPropagateToParents(node, service);
			}
		} else {
			if (isOriginalPropertySet) {
				// trying to set the same value as the original (a revert operation); unset the original value
				service.unsetProperty(node, originalProperty);
				setSyncTrueAndPropagateToParents(node, service);
			}
		}
	}

	@Override
	public void unsetProperty(Node node, String property) {
		// nothing to do
	}
	
}
