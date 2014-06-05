package org.flowerplatform.core.node.resource;

import java.net.URI;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Utils;

/**
 * Responsible with working with raw resource data: load, dirty state, save, reload.
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceHandler {

	private FileResourceService resourceService = (FileResourceService) CorePlugin.getInstance().getResourceService();
	
	public Node getNode(URI nodeUri) {
		URI resourceUri = nodeUri;
		if (nodeUri.getFragment() != null) {
			resourceUri = CoreUtils.getResourceUri(Utils.getString(nodeUri));
		}
		Object resource = resourceService.getResource(resourceUri);
		Node node = new Node(Utils.getString(nodeUri));
		Object resourceData = getResourceData(resource, nodeUri);
		node.setRawNodeData(resourceData);
		node.setType(getType(resourceData, nodeUri));
		return node;
	}

	/**
	 * Retrieve the resource data for this <code>nodeUri</code>.
	 */
	public abstract Object getResourceData(Object resource, URI nodeUri);
	
	/**
	 * Retrieve the node type from the resource data.
	 */
	public abstract String getType(Object resourceData, URI nodeUri);
	
	/**
	 * Load and register the resource data.
	 */
	public void load(URI resourceUri) {
		try {
			resourceService.registerResource(resourceUri, doLoad(resourceUri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected abstract Object doLoad(URI resourceUri) throws Exception;
	
	public void unload(URI resourceUri) {
		resourceService.unregisterResource(resourceUri);
	}
	
	public boolean isDirty(URI nodeUri) {
		URI resourceUri = nodeUri;
		if (nodeUri.getFragment() != null) {
			resourceUri = CoreUtils.getResourceUri(Utils.getString(nodeUri));
		}
		return isDirty(resourceService.getResource(resourceUri));
	}

	protected abstract boolean isDirty(Object resource);
	
	public void save(URI resourceUri) {
		try {
			doSave(resourceService.getResource(resourceUri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected abstract void doSave(Object resource) throws Exception;

	public void reload(URI resourceUri) {
		resourceService.getResource(resourceUri);
		load(resourceUri);
	}
	
}
