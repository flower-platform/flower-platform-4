package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

/**
 * @author Cristina Constantinescu
 */
public interface IAddNodeController extends IController {

	void addNode(Node node, Node child, ServiceContext<NodeService> context);
	
}
