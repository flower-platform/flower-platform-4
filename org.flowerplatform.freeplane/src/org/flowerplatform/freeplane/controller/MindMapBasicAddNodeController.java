package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node node, Node child, Node insertBeforeNode) {
		NodeModel parentModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getIdWithinResource());
		NodeModel currentModelAtInsertionPoint = null;
		if (insertBeforeNode != null) {
			currentModelAtInsertionPoint = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(insertBeforeNode.getIdWithinResource());
		}
		NodeModel newNodeModel = new NodeModel("", parentModel.getMap());
		newNodeModel.setLeft(false);
		
		parentModel.insert(newNodeModel, currentModelAtInsertionPoint != null ? parentModel.getChildPosition(currentModelAtInsertionPoint) : parentModel.getChildCount());
		
		// set the id on the node instance
		child.setIdWithinResource(newNodeModel.createID());
	}

}
