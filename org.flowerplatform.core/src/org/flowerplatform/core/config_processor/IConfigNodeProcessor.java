package org.flowerplatform.core.config_processor;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

public interface IConfigNodeProcessor extends IController {
	/**
	 * @param node
	 *            the node that you want to process/ instantiate
	 * @param parentProcessedDataStructure
	 * @param context
	 * @return a reference to the data structure of the instantiated node; if
	 *         this node has children that need to be processed, in with field
	 *         of the instance should I add them?
	 */
	public Object processConfigNode(Node node, Object parentProcessedDataStructure, ServiceContext<NodeService> context);

}
