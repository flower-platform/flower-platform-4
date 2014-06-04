package org.flowerplatform.core.node.resource;

import java.net.URI;

import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceInfo;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceService;

/**
 * @author Mariana Gheorghe
 */
public class FileResourceService extends InMemoryResourceService {

	public Object getResource(URI resourceUri) {
		InMemoryResourceInfo resourceInfo = (InMemoryResourceInfo) getResourceInfo(resourceUri);
		return resourceInfo.getResourceData();
	}
	
	public void registerResource(URI resourceUri, Object resource) {
		InMemoryResourceInfo resourceInfo = (InMemoryResourceInfo) getResourceInfo(resourceUri);
		resourceInfo.setResourceData(resource);
	}

	public void unregisterResource(URI resourceUri) {
		
	}
	
}
