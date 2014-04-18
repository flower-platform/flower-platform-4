package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public abstract class PropertiesProvider extends AbstractController {
	
	public abstract void populateWithProperties(Node node, ServiceContext<NodeService> context);
	
}
