package org.flowerplatform.js_client.java;

/**
 * @author Cristina Constantinescu
 */
public interface INodeRegistryManagerListener {
		
	void nodeRegistryRemoved(Object nodeRegistry);

	void resourceNodeRemoved(String resourceNodeUri, Object nodeRegistry);
		
}