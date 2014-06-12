package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

/**
 * @author Mariana Gheorghe
 */
public interface IParentProvider extends IController {

	Node getParent(Node node, ServiceContext<NodeService> context);
	
}
