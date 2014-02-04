package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public abstract class AddNodeController extends NodeController {

	public abstract void addNode(Node node, Node child);
	
}
