package org.flowerplatform.core.node.controller;

import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class AddNodeController extends AbstractController {

	public static final String ADD_NODE_CONTROLLER = "addNodeController";
	
	public abstract void addNode(Node node, Map<String, Object> properties, Node insertBeforeNode);
	
}
