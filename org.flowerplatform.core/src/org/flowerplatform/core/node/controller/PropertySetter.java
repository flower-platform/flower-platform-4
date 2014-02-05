package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public abstract class PropertySetter extends NodeController {

	public abstract void setProperty(Node node, String property, Object value);
	
	public abstract void unsetProperty(Node node, String property);
	
}
