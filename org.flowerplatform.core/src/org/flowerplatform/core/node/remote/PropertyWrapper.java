package org.flowerplatform.core.node.remote;

/**
 * @author Cristina Constantinescu
 */
public class PropertyWrapper {

	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public PropertyWrapper setValueAs(Object value) {
		setValue(value);
		return this;
	}
	
	public PropertyWrapper() {
		super();
	}	
	
}
