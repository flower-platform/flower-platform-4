package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.NodeService;

/**
 * The property value is wrapped to allow other {@link PropertySetter} to modify it.
 *  
 * @see NodeService#setProperty(org.flowerplatform.core.node.remote.Node, String, Object)
 * @author Cristina Constantinescu
 */
public class PropertyValueWrapper {

	private Object propertyValue;

	public PropertyValueWrapper(Object value) {
		this.propertyValue = value;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}
		
}
