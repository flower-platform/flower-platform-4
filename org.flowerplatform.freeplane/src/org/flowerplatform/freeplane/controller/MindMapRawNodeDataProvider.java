package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IRawNodeDataProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class MindMapRawNodeDataProvider extends AbstractController implements IRawNodeDataProvider<NodeModel> {

	@Override
	public NodeModel getRawNodeData(Node node, ServiceContext<NodeService> context) {
		// get the raw resource data for the resource node
		Node resourceNode = CoreUtils.getResourceNode(node);
		MapModel model = (MapModel) CorePlugin.getInstance().getResourceService()
				.getRawResourceData(resourceNode.getFullNodeId());
		// return the node with this id
		return model.getNodeForID(node.getIdWithinResource());
	}

}
