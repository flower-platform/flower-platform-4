package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceHolder {

	IResourceHandler getResourceHandler(String scheme);
	
	Node getNode(String nodeUri);
	
	Object getResourceData(String resourceUri);
	
	void registerResourceData(String resourceUri, Object resourceData);
	
}
