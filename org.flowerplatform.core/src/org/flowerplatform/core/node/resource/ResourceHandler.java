package org.flowerplatform.core.node.resource;

import java.net.URI;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public abstract class ResourceHandler {

	private FileResourceService resourceService = (FileResourceService) CorePlugin.getInstance().getResourceService();
	
	public Node getNode(URI nodeUri) {
		URI resourceUri = nodeUri;
		if (nodeUri.getFragment() != null) {
			resourceUri = Utils.getUriWithoutFragment(nodeUri);
		}
		Object resource = resourceService.getResource(resourceUri);
		if (resource == null) {
			// load and register
			try {
				resource = load(nodeUri);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			resourceService.registerResource(nodeUri, resource);
		}
		
		Node node = new Node(Utils.getString(nodeUri));
		Object resourceData = getResourceData(resource, nodeUri);
		node.setRawNodeData(resourceData);
		node.setType(getType(resourceData, nodeUri));
		return node;
	}

	protected abstract Object load(URI resourceUri) throws Exception;
	
	protected abstract Object getResourceData(Object resource, URI nodeUri);
	
	public abstract String getType(Object resourceData, URI nodeUri);

	public boolean isDirty(URI nodeUri) {
		URI resourceUri = nodeUri;
		if (nodeUri.getFragment() != null) {
			resourceUri = Utils.getUriWithoutFragment(nodeUri);
		}
		Object resource = resourceService.getResource(resourceUri);
		if (resource == null) {
			throw new RuntimeException("Resource is not loaded: " + resourceUri);
		}
		
		return isDirty(resource);
	}

	protected abstract boolean isDirty(Object resource);
	
	public void save(URI resourceUri) {
		Object resource = resourceService.getResource(resourceUri);
		if (resource == null) {
			throw new RuntimeException("Resource is not loaded: " + resourceUri);
		}
		
		try {
			doSave(resource);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected abstract void doSave(Object resource) throws Exception;

	public void reload(URI resourceUri) {
		Object resource = resourceService.getResource(resourceUri);
		if (resource == null) {
			throw new RuntimeException("Resource is not loaded: " + resourceUri);
		}
		
		try {
			resourceService.registerResource(resourceUri, load(resourceUri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
