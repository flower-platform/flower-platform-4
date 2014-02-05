package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.type_descriptor.OrderedElement;

/**
 * @author Cristina Constantinescu
 */
public abstract class PropertySetter extends OrderedElement {
	
	public static final String PROPERTY_SETTER = "propertySetter";

	public abstract void setProperty(Node node, String property, Object value);
	
	public abstract void unsetProperty(Node node, String property);
	
}
