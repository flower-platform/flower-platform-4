package org.flowerplatform.core.node.controller;

import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public abstract class PropertySetter extends AbstractController {
	
	public static final String PROPERTY_SETTER = "propertySetter";

	public abstract void setProperty(Node node, String property, PropertyValueWrapper value, Map<String, Object> options);
	
	public abstract void unsetProperty(Node node, String property, Map<String, Object> options);
	
}
