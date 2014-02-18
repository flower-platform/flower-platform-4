package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		node.getProperties().put(TEXT, ((NodeModel) node.getOrRetrieveRawNodeData()).getText());
		// TODO CC: temporary code
		node.getProperties().put(HAS_CHILDREN, ((NodeModel) node.getOrRetrieveRawNodeData()).hasChildren());
	}

}
