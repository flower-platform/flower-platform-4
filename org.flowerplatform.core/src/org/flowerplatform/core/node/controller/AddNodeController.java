package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class AddNodeController extends AbstractController {

	public abstract void addNode(Node node, Node child, ServiceContext context);
	
}
