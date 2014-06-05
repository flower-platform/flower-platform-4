package org.flowerplatform.core.node.resource;

import java.net.URI;

import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceInfo;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceService;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class FileResourceService extends InMemoryResourceService {

	public Object getResource(URI resourceUri) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
		if (resourceInfo == null) {
			throw new RuntimeException("Resource is not loaded: " + resourceUri);
		}
		return resourceInfo.getResourceData();
	}
	
	public void registerResource(URI resourceUri, Object resource) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
		if (resourceInfo == null) {
			resourceInfo = new InMemoryResourceInfo();
			resourceInfos.put(Utils.getString(resourceUri), resourceInfo);
		}
		resourceInfo.setResourceData(resource);
	}

	public void unregisterResource(URI resourceUri) {
		
	}
	
}
