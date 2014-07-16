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
							.setTypeAs(UPDATE_CHILD_ADDED)
							.setTargetNodeAs(child)
							.setFullTargetNodeAddedBeforeIdAs(insertBeforeFullNodeId)
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
			ResourceSetService service = CorePlugin.getInstance().getResourceSetService();
			service.addUpdate(resourceSet, 
						new ChildrenUpdate()
							.setTypeAs(UPDATE_CHILD_REMOVED)
							.setTargetNodeAs((Node)context.get("removedNode"))
							.setFullTargetNodeAddedBeforeIdAs(insertBeforeFullNodeId)
							.setFullNodeIdAs(node.getNodeUri()));		
		}
	}
	
	@Override
	public void setProperty(Node node, String key, Object value, ServiceContext<NodeService> context) {	
		setUnsetProperty(node, key, value, false, context);
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
		
		if (context.getContext().containsKey(CoreConstants.OLD_VALUE)) {
			update.setHasOldValueAs(true);
			update.setOldValueAs(context.get(CoreConstants.OLD_VALUE));
		}
		
		service.addUpdate(resourceSet, update);		
	}

}
