package org.flowerplatform.core.node.resource.in_memory;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.resource.ResourceService2;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryResourceService extends ResourceService2 {

	private Map<String, InMemoryResourceInfo> resourceInfos = new HashMap<String, InMemoryResourceInfo>();
	
	@Override
	public Object getResourceInfo(URI resourceUri) {
		return resourceInfos.get(Utils.getString(resourceUri));
	}

	@Override
	protected void sessionSubscribedToResource(String sessionId, URI resourceUri) {
		InMemoryResourceInfo resourceInfo = (InMemoryResourceInfo) getResourceInfo(resourceUri);
		if (resourceInfo == null) {
			resourceInfo = new InMemoryResourceInfo();
			resourceInfos.put(Utils.getString(resourceUri), resourceInfo);
		}
		if (!resourceInfo.getSessionIds().contains(sessionId)) {
			resourceInfo.getSessionIds().add(sessionId);
		}
	}

	@Override
	public List<String> getResources() {
		return new ArrayList<>(resourceInfos.keySet());
	}

	@Override
	public List<String> getSessionsSubscribedToResource(String resourceNodeId) {
		return resourceInfos.get(resourceNodeId).getSessionIds();
	}

	@Override
	public long getUpdateRequestedTimestamp(String resourceNodeId) {
		return resourceInfos.get(resourceNodeId).getLastPing();
	}

	@Override
	public void setUpdateRequestedTimestamp(String resourceUri, long timestamp) {
		resourceInfos.get(resourceUri).setLastPing(timestamp);
	}

}
