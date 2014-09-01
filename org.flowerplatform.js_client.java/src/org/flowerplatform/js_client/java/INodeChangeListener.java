package org.flowerplatform.js_client.java;
		
public interface INodeChangeListener {
		
	void nodeRemoved(JsNode node);
	
	void nodeAdded(JsNode node);
	
	void nodeUpdated(JsNode node, String property, Object oldValue, Object newValue);
	
}