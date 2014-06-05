package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public interface IPropertiesProvider extends IController {
	
	void populateWithProperties(Node node, ServiceContext<NodeService> context);
	
}
