package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class FreeplaneAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node node, Node child) {
		NodeModel parentModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
		NodeModel newNodeModel = new NodeModel("", parentModel.getMap());
		newNodeModel.setLeft(false);

		// persist the type
		NodeService nodeService = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
		nodeService.setProperty(child, "type", child.getType());
		
		parentModel.insert(newNodeModel, parentModel.getChildCount());
		
		// set the id on the node instance
		child.setId(newNodeModel.createID());
	}

}
