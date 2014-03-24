package org.flowerplatform.core.node.controller;

import java.util.Map;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public abstract class ParentProvider extends AbstractController {

	public static final String PARENT_PROVIDER = "parentProvider";
	
	public abstract Node getParent(Node node, ServiceContext context);
	
}
