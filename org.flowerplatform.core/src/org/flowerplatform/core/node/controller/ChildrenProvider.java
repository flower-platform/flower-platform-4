package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.util.Pair;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristian Spiescu
 */
public abstract class ChildrenProvider extends NodeController {

	public abstract List<Pair<Node, Object>> getChildren(Node node);
	
}
