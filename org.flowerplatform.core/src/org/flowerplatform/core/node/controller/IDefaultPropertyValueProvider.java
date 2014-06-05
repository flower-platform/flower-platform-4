package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.IController;

/**
 * @author Sebastian Solomon
 */
public interface IDefaultPropertyValueProvider extends IController {
	
	Object getDefaultValue(Node node, String property, ServiceContext<NodeService> context); 

}
