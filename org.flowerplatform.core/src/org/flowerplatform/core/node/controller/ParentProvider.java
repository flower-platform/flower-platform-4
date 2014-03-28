package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public abstract class ParentProvider extends AbstractController {

	public abstract Node getParent(Node node, ServiceContext context);
	
}
