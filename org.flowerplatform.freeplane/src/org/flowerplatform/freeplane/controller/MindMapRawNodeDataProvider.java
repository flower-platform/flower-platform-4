package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.RawNodeDataProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class MindMapRawNodeDataProvider extends RawNodeDataProvider<NodeModel> {

	@Override
	public NodeModel getRawNodeData(Node node, ServiceContext context) {
		// get the raw resource data for the resource node
		Node resourceNode = CoreUtils.getResourceNode(node);
		MapModel model = (MapModel) CorePlugin.getInstance().getResourceService()
				.getRawResourceData(resourceNode.getFullNodeId());
		// return the node with this id
		return model.getNodeForID(node.getIdWithinResource());
	}

}
