package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristian Spiescu
 */
public abstract class ChildrenProvider extends AbstractController {

	public abstract List<Node> getChildren(Node node, ServiceContext<NodeService> context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract boolean hasChildren(Node node, ServiceContext<NodeService> context);
	
}
