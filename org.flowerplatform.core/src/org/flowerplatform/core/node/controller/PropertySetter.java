package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class PropertySetter extends AbstractController {
	
	public static final String PROPERTY_SETTER = "propertySetter";

	public abstract void setProperty(Node node, String property, Object value);
	
	public abstract void unsetProperty(Node node, String property);
	
}
