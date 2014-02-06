package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.util.Pair;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristian Spiescu
 */
public abstract class ChildrenProvider extends AbstractController {

	public static final String CHILDREN_PROVIDER = "childrenProvider";
	
	public abstract List<Pair<Node, Object>> getChildren(Node node);
	
}
