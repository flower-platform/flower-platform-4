package org.flowerplatform.core.mindmap;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class FreeplaneAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node node, Node child) {
		NodeModel parentModel = CorePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
		NodeModel newNodeModel = new NodeModel("", parentModel.getMap());
		newNodeModel.setLeft(false);

		parentModel.insert(newNodeModel, parentModel.getChildCount());
		child.setId(newNodeModel.createID());
	}

}
