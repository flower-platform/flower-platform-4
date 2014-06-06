package org.flowerplatform.core.node.resource;

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

	protected FileResourceService resourceService = (FileResourceService) CorePlugin.getInstance().getResourceService();
	
	public Node getNode(String nodeUri) {
		String resourceUri = nodeUri;
		if (Utils.getFragment(nodeUri) != null) {
			resourceUri = CoreUtils.getResourceUri(nodeUri);
		}
		Object resource = getResource(resourceUri);
		Node node = new Node(nodeUri);
		Object resourceData = getResourceData(resource, nodeUri);
		node.setRawNodeData(resourceData);
		node.setType(getType(resourceData, nodeUri));
		return node;
	}

	public Object getResource(String resourceUri) {
		return resourceService.getResource(resourceUri);
	}
	
	/**
	 * Retrieve the resource data for this <code>nodeUri</code>.
	 */
	public abstract Object getResourceData(Object resource, String nodeUri);
	
	/**
	 * Retrieve the node type from the resource data.
	 */
	public abstract String getType(Object resourceData, String nodeUri);
	
	/**
	 * Load and register the resource data.
	 */
	public void load(String resourceUri) {
		try {
			resourceService.registerResource(resourceUri, doLoad(resourceUri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected abstract Object doLoad(String resourceUri) throws Exception;
	
	public void unload(String resourceUri) {
		resourceService.unregisterResource(resourceUri);
	}
	
	public boolean isDirty(String nodeUri) {
		String resourceUri = nodeUri;
		if (Utils.getFragment(nodeUri) != null) {
			resourceUri = CoreUtils.getResourceUri(nodeUri);
		}
		return isDirty(resourceService.getResource(resourceUri));
	}

	protected abstract boolean isDirty(Object resource);
	
	public void save(String resourceUri) {
		try {
			doSave(resourceService.getResource(resourceUri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected abstract void doSave(Object resource) throws Exception;

	public void reload(String resourceUri) {
		resourceService.getResource(resourceUri);
		load(resourceUri);
	}
	
}
