package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class PropertySetter extends AbstractController {
	
	public abstract void setProperty(Node node, String property, PropertyValueWrapper value, ServiceContext context);
	
	public abstract void unsetProperty(Node node, String property, ServiceContext context);
	
}
