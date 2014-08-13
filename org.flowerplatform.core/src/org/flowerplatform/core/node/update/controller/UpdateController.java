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
		String insertBeforeFullNodeId = (String) context.get(CoreConstants.INSERT_BEFORE_FULL_NODE_ID);
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				node, 
				new ChildrenUpdate().setTargetNodeAs(child).setFullTargetNodeAddedBeforeIdAs(insertBeforeFullNodeId)
				.setTypeAs(UPDATE_CHILD_ADDED).setFullNodeIdAs(node.getNodeUri()), 
				context);	
	}
	
	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				node, 
				new ChildrenUpdate().setTargetNodeAs(child).setTypeAs(UPDATE_CHILD_REMOVED).setFullNodeIdAs(node.getNodeUri()), 
				context);	
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
	
	private void setUnsetProperty(Node node, String key, Object value, boolean isUnset, ServiceContext<NodeService> context) {		
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				node, 
				new PropertyUpdate().setKeyAs(key).setValueAs(value).setUnsetAs(isUnset).setFullNodeIdAs(node.getNodeUri()), 
				context);		
	}

}
