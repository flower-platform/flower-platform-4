package org.flowerplatform.js_client.java;

public interface INodeRegistryManagerListener {
		
	void nodeRegistryRemoved(Object nodeRegistry);

	void resourceNodeRemoved(String resourceNodeUri, Object nodeRegistry);
		
}