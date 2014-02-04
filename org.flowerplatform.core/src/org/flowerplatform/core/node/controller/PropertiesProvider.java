package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.Property;

/**
 * @author Cristian Spiescu
 *
 * @param <RAW_NODE_DATA_TYPE>
 */
public abstract class PropertiesProvider<RAW_NODE_DATA_TYPE> extends NodeController {
	
	public abstract void populateWithProperties(Node node, RAW_NODE_DATA_TYPE rawNodeData);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract List<Property> getPropertiesToDisplay(Node node);
	
}
