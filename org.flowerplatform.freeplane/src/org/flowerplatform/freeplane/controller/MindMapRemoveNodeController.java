package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapRemoveNodeController extends AbstractController implements IRemoveNodeController {

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) child.getRawNodeData());
		rawNodeData.getParentNode().remove(rawNodeData.getParentNode().getChildPosition(rawNodeData));
		rawNodeData.getMap().unregistryNodes(rawNodeData);
		rawNodeData.getMap().setSaved(false);
	}

}
