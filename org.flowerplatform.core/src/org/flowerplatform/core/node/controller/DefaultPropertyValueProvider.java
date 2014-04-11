package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public abstract class DefaultPropertyValueProvider extends AbstractController {
	
	public abstract Object getDefaultValue(Node node, String property, ServiceContext context); 

}
