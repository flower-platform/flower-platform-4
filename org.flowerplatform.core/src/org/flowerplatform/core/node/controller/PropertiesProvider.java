package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.type_descriptor.OrderedElement;

/**
 * @author Cristian Spiescu
 *
 * @param <RAW_NODE_DATA_TYPE>
 */
public abstract class PropertiesProvider<RAW_NODE_DATA_TYPE> extends OrderedElement {
	
	public static final String PROPERTIES_PROVIDER = "propertiesProvider";
	
	public abstract void populateWithProperties(Node node, RAW_NODE_DATA_TYPE rawNodeData);
	
}
