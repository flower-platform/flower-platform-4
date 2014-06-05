package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

/**
 * @author Cristina Constantinescu
 */
public interface IRawNodeDataProvider<RAW_NODE_DATA_TYPE> extends IController {

	RAW_NODE_DATA_TYPE getRawNodeData(Node node, ServiceContext<NodeService> context);
	
}
