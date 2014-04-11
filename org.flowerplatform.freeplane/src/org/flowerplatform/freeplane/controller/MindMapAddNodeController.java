package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node node, Node child, ServiceContext context) {
		NodeModel parentRawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		NodeModel currentModelAtInsertionPoint = null;
		Node insertBeforeNode = new Node((String)context.get(CoreConstants.INSERT_BEFORE_FULL_NODE_ID));
		if (insertBeforeNode != null) {
			currentModelAtInsertionPoint = ((NodeModel) insertBeforeNode.getOrRetrieveRawNodeData());
		}
		NodeModel newNodeModel = new NodeModel("", parentRawNodeData.getMap());
		newNodeModel.setLeft(false);
		
		parentRawNodeData.insert(newNodeModel, currentModelAtInsertionPoint != null ? parentRawNodeData.getChildPosition(currentModelAtInsertionPoint) : parentRawNodeData.getChildCount());
		parentRawNodeData.getMap().setSaved(false);
		
		// set the id on the node instance
		child.setIdWithinResource(newNodeModel.createID());		
	}

}
