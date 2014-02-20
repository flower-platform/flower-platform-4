package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.ParentProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Mariana Gheorghe
 */
public class FreeplaneParentProvider extends ParentProvider {

	@Override
	public Node getParent(Node node) {
		NodeModel nodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
		NodeModel parentNodeModel = nodeModel.getParentNode();
		if (parentNodeModel == null) {
			return null;
		}
		return FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(parentNodeModel);
	}

}
