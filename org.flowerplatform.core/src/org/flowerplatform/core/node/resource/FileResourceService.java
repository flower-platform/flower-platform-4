package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceInfo;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceService;

/**
 * @author Mariana Gheorghe
 */
public class FileResourceService extends InMemoryResourceService {

	public Object getResource(String resourceUri) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
		if (resourceInfo == null) {
			return null;
		}
		return resourceInfo.getResourceData();
	}
	
	public void registerResource(String resourceUri, Object resource) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
		if (resourceInfo == null) {
			resourceInfo = new InMemoryResourceInfo();
			resourceInfos.put(resourceUri, resourceInfo);
		}
		resourceInfo.setResourceData(resource);
	}

	public void unregisterResource(String resourceUri) {
		
	}
	
}
