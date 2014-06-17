package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

/**
 * @author Cristina Constantinescu
 */
public interface IPropertySetter extends IController {
	
	public abstract void setProperty(Node node, String property, PropertyValueWrapper value, ServiceContext<NodeService> context);
	
	public abstract void unsetProperty(Node node, String property, ServiceContext<NodeService> context);
	
}
