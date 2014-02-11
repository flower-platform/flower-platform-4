package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertiesProvider extends PropertiesProvider<NodeModel> {

	@Override
	public void populateWithProperties(Node node, NodeModel rawNodeData) {
		if (NodeSizeModel.getModel(rawNodeData) != null) {
			Integer value = NodeSizeModel.getModel(rawNodeData).getMinNodeWidth();
			// if NOT_SET -> don't show -1, show empty data
			node.getOrCreateProperties().put("min_width", value == NodeSizeModel.NOT_SET ? "" : value);
			
			value = NodeSizeModel.getModel(rawNodeData).getMinNodeWidth();		
			// if NOT_SET -> don't show -1, show empty data
			node.getOrCreateProperties().put("max_width", value == NodeSizeModel.NOT_SET ? "" : value);			
		}
	}

}
