package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

/**
 * @author Cristian Spiescu
 */
public interface IChildrenProvider extends IController {

	List<Node> getChildren(Node node, ServiceContext<NodeService> context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	boolean hasChildren(Node node, ServiceContext<NodeService> context);
	
}
