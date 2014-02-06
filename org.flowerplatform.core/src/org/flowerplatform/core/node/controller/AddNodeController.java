package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.type_descriptor.OrderedElement;

/**
 * @author Cristina Constantinescu
 */
public abstract class AddNodeController extends OrderedElement {

	public static final String ADD_NODE_CONTROLLER = "addNodeController";
	
	public abstract void addNode(Node node, Node child);
	
}
