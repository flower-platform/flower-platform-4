package org.flowerplatform.core.node.resource.in_memory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.resource.ResourceService2;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryResourceService extends ResourceService2 {

	protected Map<String, InMemoryResourceInfo> resourceInfos = new HashMap<String, InMemoryResourceInfo>();
	
	@Override
	public InMemoryResourceInfo getResourceInfo(URI resourceUri) {
		return resourceInfos.get(Utils.getString(resourceUri));
	}

	@Override
	public void doSessionSubscribedToResource(String sessionId, URI resourceUri) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
		if (resourceInfo != null && !resourceInfo.getSessionIds().contains(sessionId)) {
			resourceInfo.getSessionIds().add(sessionId);
		}
	}
	
	@Override
	public void doSessionUnsubscribedFromResource(String sessionId, URI resourceUri) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
		if (resourceInfo != null) {
			resourceInfo.getSessionIds().remove(sessionId);
		}
	}

	@Override
	public List<String> getResources() {
		return new ArrayList<>(resourceInfos.keySet());
	}

	@Override
	public List<String> getSessionsSubscribedToResource(String resourceUri) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(Utils.getUri(resourceUri));
		if (resourceInfo == null) {
			return Collections.emptyList();
		}
		return resourceInfo.getSessionIds();
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
