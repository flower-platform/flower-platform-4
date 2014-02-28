package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicRemoveNodeController extends RemoveNodeController {

	@Override
	public void removeNode(Node node, Node child) {
		NodeModel childModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(child.getIdWithinResource());
		childModel.removeFromParent();
		childModel.getMap().unregistryNodes(childModel);
	}

}