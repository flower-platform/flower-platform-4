package org.flowerplatform.core.node.resource;

import java.net.URI;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public abstract class ResourceHandler {

	private FileResourceService resourceService = (FileResourceService) CorePlugin.getInstance().getResourceService2();
	
	public Node getNode(URI nodeUri) {
		Object resource = resourceService.getResource(nodeUri);
		if (resource == null) {
			// load and register
			try {
				resource = load(nodeUri);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			resourceService.registerResource(nodeUri, resource);
		}
		
		Node resourceNode = new Node(nodeUri.toString());
		resourceNode.setRawNodeData(resource);
		resourceNode.setType(getType(resource));
		return resourceNode;
	}

	protected abstract Object load(URI resourceUri) throws Exception;
	
	protected abstract String getType(Object resource);
	
}
