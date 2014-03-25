package org.flowerplatform.core.node.controller;

import java.util.Map;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class RemoveNodeController extends AbstractController {

	public static final String REMOVE_NODE_CONTROLLER = "removeNodeController";
	
	public abstract void removeNode(Node node, Node child, ServiceContext context);
	
}
