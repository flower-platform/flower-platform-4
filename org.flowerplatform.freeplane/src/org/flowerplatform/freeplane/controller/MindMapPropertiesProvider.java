package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertiesProvider extends MindMapBasicPropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		super.populateWithProperties(node);
		
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		if (NodeSizeModel.getModel(rawNodeData) == null) {
			NodeSizeModel.createNodeSizeModel(rawNodeData);
		}
		
		Integer value = NodeSizeModel.getModel(rawNodeData).getMinNodeWidth();
		// if NOT_SET -> don't show -1, show empty data
		node.getProperties().put("min_width", value == NodeSizeModel.NOT_SET ? "" : value);
			
		value = NodeSizeModel.getModel(rawNodeData).getMaxNodeWidth();		
		// if NOT_SET -> don't show -1, show empty data
		node.getProperties().put("max_width", value == NodeSizeModel.NOT_SET ? "" : value);		
	}

}
