package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class RawNodeDataProvider<RAW_NODE_DATA_TYPE> extends AbstractController {

	public abstract RAW_NODE_DATA_TYPE getRawNodeData(Node node, ServiceContext context);
	
}
