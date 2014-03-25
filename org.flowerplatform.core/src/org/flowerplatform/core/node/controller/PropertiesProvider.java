package org.flowerplatform.core.node.controller;

import java.util.Map;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public abstract class PropertiesProvider extends AbstractController {
	
	public static final String PROPERTIES_PROVIDER = "propertiesProvider";
	
	public abstract void populateWithProperties(Node node, ServiceContext context);
	
}
