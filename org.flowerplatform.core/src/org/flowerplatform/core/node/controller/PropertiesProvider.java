package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristian Spiescu
 *
 * @param <RAW_NODE_DATA_TYPE>
 */
public abstract class PropertiesProvider<RAW_NODE_DATA_TYPE> extends NodeController {
	
	public abstract void populateWithProperties(Node node, RAW_NODE_DATA_TYPE rawNodeData);
	
}
