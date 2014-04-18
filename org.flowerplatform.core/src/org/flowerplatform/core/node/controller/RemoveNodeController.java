package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class RemoveNodeController extends AbstractController {

	public abstract void removeNode(Node node, Node child, ServiceContext<NodeService> context);
	
}
