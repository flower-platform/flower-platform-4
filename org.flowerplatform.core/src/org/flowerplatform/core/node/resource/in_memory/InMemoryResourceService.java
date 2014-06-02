package org.flowerplatform.core.node.resource.in_memory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.core.node.resource.ResourceService2;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryResourceService extends ResourceService2 {

	private Map<String, InMemoryResourceInfo> resourceInfos = new HashMap<String, InMemoryResourceInfo>();
	
	@Override
	public Object getResourceInfo(URI resourceUri) {
		return resourceInfos.get(resourceUri.toString());
	}

	@Override
	protected void doSessionSubscribedToResource(String sessionId, URI resourceUri) {
		InMemoryResourceInfo resourceInfo = (InMemoryResourceInfo) getResourceInfo(resourceUri);
		if (resourceInfo == null) {
			resourceInfo = new InMemoryResourceInfo();
			resourceInfo.getSessionIds().add(sessionId);
			resourceInfos.put(resourceUri.toString(), resourceInfo);
		}
	}

}
