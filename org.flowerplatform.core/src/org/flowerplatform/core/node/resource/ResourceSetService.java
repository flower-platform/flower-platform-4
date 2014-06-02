package org.flowerplatform.core.node.resource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mariana Gheorghe
 */
public class ResourceSetService {

	private Map<String, ResourceSetInfo> resourceSetInfos = new HashMap<String, ResourceSetInfo>();
	
	public void addToResourceSet(String resourceSet, URI resourceUri) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info == null) {
			info = new ResourceSetInfo();
			resourceSetInfos.put(resourceSet, info);
		}
		info.getResourceUris().add(resourceUri.toString());
	}
	
	public void save(String resourceSet) {
		
	}
	
}
