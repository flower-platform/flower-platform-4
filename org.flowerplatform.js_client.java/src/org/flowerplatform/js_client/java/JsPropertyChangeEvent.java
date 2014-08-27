package org.flowerplatform.js_client.java;

public class JsPropertyChangeEvent {
	
	public Object newValue;
	
	public Object oldValue;
	
	public Object property;
	
	public Object source;

	public JsPropertyChangeEvent(Object source, Object property, Object oldValue, Object newValue) {
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.property = property;
		this.source = source;
	}
	
}
