package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.RawNodeDataProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicRawNodeDataProvider extends RawNodeDataProvider<NodeModel> {

	@Override
	public NodeModel getRawNodeData(Node node) {
		return FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
	}

}
