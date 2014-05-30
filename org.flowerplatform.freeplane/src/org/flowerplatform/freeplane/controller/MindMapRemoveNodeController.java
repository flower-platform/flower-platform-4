package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapRemoveNodeController extends RemoveNodeController {

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) child.getOrRetrieveRawNodeData());
		rawNodeData.getParentNode().remove(rawNodeData.getParentNode().getChildPosition(rawNodeData));
		rawNodeData.getMap().unregistryNodes(rawNodeData);
		rawNodeData.getMap().setSaved(false);
	}

}
