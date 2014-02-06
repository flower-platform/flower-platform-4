package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.type_descriptor.OrderedElement;

/**
 * @author Cristina Constantinescu
 */
public abstract class RemoveNodeController extends OrderedElement {

	public static final String REMOVE_NODE_CONTROLLER = "removeNodeController";
	
	public abstract void removeNode(Node node, Node child);
	
}
