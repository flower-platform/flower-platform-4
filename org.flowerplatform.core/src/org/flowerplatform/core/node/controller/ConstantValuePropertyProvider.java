package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * Adds a constant value for {@link #property} to the properties map of a node (e.g. icon).
 * 
 * @author Mariana Gheorghe
 */
public class ConstantValuePropertyProvider extends PropertiesProvider {

	private String property;

	private Object value;
	
	public ConstantValuePropertyProvider(String property, Object value) {
		this.property = property;
		this.value = value;
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(property, value);
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("ConstantValueProvider [property = %s, value = %s, orderIndex = %d]", 
				property, value, getOrderIndex());
	}
}
