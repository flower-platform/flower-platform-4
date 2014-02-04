package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public abstract class RemoveNodeController extends NodeController {

	public abstract void removeNode(Node node, Node child);
	
}
