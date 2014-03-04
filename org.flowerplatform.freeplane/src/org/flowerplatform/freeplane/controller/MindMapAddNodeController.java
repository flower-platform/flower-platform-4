package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node node, Node child, Node insertBeforeNode) {
		NodeModel parentRawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		NodeModel currentModelAtInsertionPoint = null;
		if (insertBeforeNode != null) {
			currentModelAtInsertionPoint = ((NodeModel) insertBeforeNode.getOrRetrieveRawNodeData());
		}
		NodeModel newNodeModel = new NodeModel("", parentRawNodeData.getMap());
		newNodeModel.setLeft(false);
		
		parentRawNodeData.insert(newNodeModel, currentModelAtInsertionPoint != null ? parentRawNodeData.getChildPosition(currentModelAtInsertionPoint) : parentRawNodeData.getChildCount());
		
		// set the id on the node instance
		child.setIdWithinResource(newNodeModel.createID());		
	}

}
