package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicPropertiesProvider extends PropertiesProvider<NodeModel> {

	@Override
	public void populateWithProperties(Node node, NodeModel rawNodeData) {
		node.getOrCreateProperties().put("body", rawNodeData.getText());
		node.getOrCreateProperties().put("hasChildren", rawNodeData.hasChildren());
	}

}
