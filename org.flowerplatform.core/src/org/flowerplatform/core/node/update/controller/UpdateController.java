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
import static org.flowerplatform.core.CoreConstants.RESOURCE_SET;
import static org.flowerplatform.core.CoreConstants.UPDATE_CHILD_ADDED;
import static org.flowerplatform.core.CoreConstants.UPDATE_CHILD_REMOVED;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public class UpdateController extends AbstractController
		implements IAddNodeController, IRemoveNodeController, IPropertySetter {
	
	public UpdateController() {
		// must be invoked last; otherwise the modification may not be fully/correctly recorded
		setOrderIndex(100000);
	}
	
	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {		
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());
		String insertBeforeFullNodeId = (String) context.get(CoreConstants.INSERT_BEFORE_FULL_NODE_ID);
		if (resourceNode != null) {
			String resourceSet = (String) resourceNode.getProperties().get(RESOURCE_SET);
			if (resourceSet == null) {
				resourceSet = resourceNode.getNodeUri();
			}
			ResourceSetService service = CorePlugin.getInstance().getResourceSetService();
			service.addUpdate(resourceSet, 
						new ChildrenUpdate()
							.setTargetNodeAs(child)
							.setFullTargetNodeAddedBeforeIdAs(insertBeforeFullNodeId)
							.setTypeAs(UPDATE_CHILD_ADDED)
							.setFullNodeIdAs(node.getNodeUri()));
			
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Claudiu Matei
	 */
	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());
		String insertBeforeFullNodeId = (String) context.get(CoreConstants.INSERT_BEFORE_FULL_NODE_ID);
		if (resourceNode != null) {
			String resourceSet = (String) resourceNode.getProperties().get(RESOURCE_SET);
			if (resourceSet == null) {
				resourceSet = resourceNode.getNodeUri();
			}
			// otherwise the node will reference probably the whole mindmap file, that would lead to memory leaks
//			child.setRawNodeData(null);
			ResourceSetService service = CorePlugin.getInstance().getResourceSetService();
			@SuppressWarnings("unchecked")
			List<ChildrenUpdate> removedNodes = (List<ChildrenUpdate>) context.get("removedNodes");
			service.addUpdate(resourceSet, 
						new ChildrenUpdate()
							.setTargetNodeAs(child)
							.setRemovedNodesAs(removedNodes)
							.setFullTargetNodeAddedBeforeIdAs(insertBeforeFullNodeId)
							.setTypeAs(UPDATE_CHILD_REMOVED)
							.setFullNodeIdAs(node.getNodeUri()));		
		}
	}
	
	@Override
	public void setProperties(Node node, Map<String,Object> properties, ServiceContext<NodeService> context) {	
		for (String key : properties.keySet()) {
			setUnsetProperty(node, key, properties.get(key), false, context);
		}
	}

	@Override
	public void unsetProperty(Node node, String key, ServiceContext<NodeService> context) {
		Object propertyValue = node.getPropertyValueOrWrapper(key);
		// consider property "unset" if its value is null (doesn't exist in map)
		setUnsetProperty(node, key, propertyValue, propertyValue == null, context);
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Claudiu Matei
	 */
	private void setUnsetProperty(Node node, String key, Object value, boolean isUnset, ServiceContext<NodeService> context) {		
		String resourceSet = null;
		Node resourceNode;
		if (context.getBooleanValue(NODE_IS_RESOURCE_NODE)) {
			resourceNode = node;
		} else {
			resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());
			
		}
		if (resourceNode == null) {
			return;
		}
		resourceSet = (String) resourceNode.getProperties().get(RESOURCE_SET);
		if (resourceSet == null) {
			resourceSet = resourceNode.getNodeUri();
		}
		ResourceSetService service = CorePlugin.getInstance().getResourceSetService();
		PropertyUpdate update = new PropertyUpdate();
		update.setKeyAs(key).setValueAs(value).setUnsetAs(isUnset).setFullNodeIdAs(node.getNodeUri());
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> oldValues = (HashMap<String,Object>)context.getContext().get(CoreConstants.OLD_VALUES);
		if (oldValues.containsKey(key)) {
			update.setHasOldValueAs(true);
			update.setOldValueAs(oldValues.get(key));
		}
		
		service.addUpdate(resourceSet, update);		
	}

}
