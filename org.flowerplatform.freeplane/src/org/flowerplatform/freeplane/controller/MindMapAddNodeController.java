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
package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapAddNodeController extends AbstractController implements IAddNodeController {

	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {
		NodeModel parentRawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		NodeModel currentModelAtInsertionPoint = null;
		String insertBeforeFullNodeId = (String) context.get(CoreConstants.INSERT_BEFORE_FULL_NODE_ID);
		if (insertBeforeFullNodeId != null) {
			Node insertBeforeNode = new Node(insertBeforeFullNodeId);
			currentModelAtInsertionPoint = insertBeforeNode != null ? (NodeModel) insertBeforeNode.getOrRetrieveRawNodeData() : null;
		}
		NodeModel newNodeModel = new NodeModel("", parentRawNodeData.getMap());
		newNodeModel.setLeft(parentRawNodeData.isLeft());
		
		parentRawNodeData.insert(newNodeModel, currentModelAtInsertionPoint != null ? parentRawNodeData.getChildPosition(currentModelAtInsertionPoint) : parentRawNodeData.getChildCount());
		parentRawNodeData.getMap().setSaved(false);
		
		// set the id on the node instance
		child.setIdWithinResource(newNodeModel.createID());	
		
		// populate also with initial properties
		child.getOrPopulateProperties();
	}

}
