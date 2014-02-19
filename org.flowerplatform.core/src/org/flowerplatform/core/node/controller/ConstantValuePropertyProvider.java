package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;

/**
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
	public void populateWithProperties(Node node) {
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
}
