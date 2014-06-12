package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceHandler {

	String getResourceUri(String nodeUri);
	
	Object getRawNodeDataFromResource(String nodeUri, Object resourceData);
	
	/**
	 * Creates a new {@link Node} and sets it <code>rawNodeData</code>,
	 * URI and type.
	 */
	Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData);

	Object load(String resourceUri) throws Exception;
	
	void save(Object resourceData) throws Exception;
	
	boolean isDirty(Object resourceData);
	
	void unload(Object resourceData) throws Exception;
	
}
