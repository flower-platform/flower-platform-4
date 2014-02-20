package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.ParentProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.util.Pair;
import org.freeplane.features.map.NodeModel;

/**
 * @author Mariana Gheorghe
 */
public class MindMapBasicParentProvider extends ParentProvider {

	@Override
	public Pair<Node, Object> getParent(Node node) {
		NodeModel nodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getIdWithinResource());
		NodeModel parentNodeModel = nodeModel.getParentNode();
		if (parentNodeModel == null) {
			return null;
		}
		return new Pair<Node, Object>(FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(parentNodeModel), parentNodeModel);
	}

}
