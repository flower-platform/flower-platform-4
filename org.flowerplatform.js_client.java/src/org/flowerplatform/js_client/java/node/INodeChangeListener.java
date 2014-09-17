package org.flowerplatform.js_client.java.node;
	
/**
 * @author Cristina Constantinescu
 */
public interface INodeChangeListener {
		
	void nodeRemoved(Object node);
	
	void nodeAdded(Object node);
	
	void nodeUpdated(Object node, String property, Object oldValue, Object newValue);
	
}