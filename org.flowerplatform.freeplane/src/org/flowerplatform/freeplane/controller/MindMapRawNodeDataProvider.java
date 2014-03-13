package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.CorePlugin;
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
	public NodeModel getRawNodeData(Node node) {
		MapModel model = (MapModel) CorePlugin.getInstance().getResourceInfoService()
				.getRawResourceData(node.getFullNodeId());
		if (CorePlugin.RESOURCE_TYPE.equals(node.getType())) {
			// return the root node for the resource
			return model.getRootNode();
		} else {
			// return the node with this id
			return model.getNodeForID(node.getIdWithinResource());
		}
	}

}
