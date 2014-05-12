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
package org.flowerplatform.core.node.update.controller;

import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;

/**
 * @author Cristina Constantinescu
 */
public class UpdatePropertySetterController extends PropertySetter {

	public UpdatePropertySetterController() {
		// must be invoked last; otherwise the modification may not be fully/correctly recorded
		setOrderIndex(100000);
	}
	
	@Override
	public void setProperty(Node node, String key, PropertyValueWrapper wrapper, ServiceContext<NodeService> context) {	
		setUnsetProperty(node, key, wrapper.getPropertyValue(), false, context);
	}

	@Override
	public void unsetProperty(Node node, String key, ServiceContext<NodeService> context) {
		setUnsetProperty(node, key, node.getOrPopulateProperties().get(key), false, context);
	}
	
	private void setUnsetProperty(Node node, String key, Object value, boolean isUnset, ServiceContext<NodeService> context) {		
		Node resourceNode;
		if (context.getBooleanValue(NODE_IS_RESOURCE_NODE)) {
			resourceNode = node;
		} else {
			resourceNode = CoreUtils.getResourceNode(node);
			if (resourceNode == null) {
				return;
			}
		}

		CorePlugin.getInstance().getResourceService()
			.addUpdate(resourceNode.getFullNodeId(), new PropertyUpdate().setKeyAs(key).setValueAs(value).setUnsetAs(isUnset).setFullNodeIdAs(node.getFullNodeId()));		
	}

}