package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristian Spiescu
 */
public abstract class ChildrenProvider extends AbstractController {

	public static final String CHILDREN_PROVIDER = "childrenProvider";
	
	public abstract List<Node> getChildren(Node node, ServiceContext context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract boolean hasChildren(Node node, ServiceContext context);
	
}
