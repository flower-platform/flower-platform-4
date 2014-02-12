package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class RootNodeProvider extends AbstractController {

	public static final String ROOT_NODE_PROVIDER = "rootNodeProvider";
	
	public abstract Node getRootNode(Node node);
	
}
