package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		node.getProperties().put("body", ((NodeModel) node.getOrRetrieveRawNodeData()).getText());
		// TODO CC: temporary code
		node.getProperties().put("hasChildren", ((NodeModel) node.getOrRetrieveRawNodeData()).hasChildren());
	}

}
