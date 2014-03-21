package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapRemoveNodeController extends RemoveNodeController {

	@Override
	public void removeNode(Node node, Node child) {
		NodeModel rawNodeData = ((NodeModel) child.getOrRetrieveRawNodeData());
		rawNodeData.removeFromParent();
		rawNodeData.getMap().unregistryNodes(rawNodeData);
		rawNodeData.getMap().setSaved(false);
	}

}
