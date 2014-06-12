package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class BaseResourceHandler implements IResourceHandler {

	private String type;
	
	public BaseResourceHandler(String type) {
		this.type = type;
	}
	
	@Override
	public String getResourceUri(String nodeUri) {
		return null;
	}

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		return null;
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		return new Node(nodeUri, type);
	}

	@Override
	public Object load(String resourceUri) throws Exception {
		return null;
	}

	@Override
	public void save(Object resourceData) throws Exception {
		// nothing to do
	}

	@Override
	public boolean isDirty(Object resourceData) {
		return false;
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// nothing to do
	}

}
