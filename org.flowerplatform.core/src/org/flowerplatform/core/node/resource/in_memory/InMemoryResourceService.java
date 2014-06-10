package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.resource.ResourceService;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryResourceService extends ResourceService {

	protected Map<String, InMemoryResourceInfo> resourceInfos = new HashMap<String, InMemoryResourceInfo>();
	
	@Override
	public InMemoryResourceInfo getResourceInfo(String resourceUri) {
		return resourceInfos.get(resourceUri);
	}

	@Override
	public void doSessionSubscribedToResource(String sessionId, String resourceUri) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
		if (resourceInfo != null && !resourceInfo.getSessionIds().contains(sessionId)) {
			// add only if not yet added
			resourceInfo.getSessionIds().add(sessionId);
		}
	}
	
	@Override
	public void doSessionUnsubscribedFromResource(String sessionId, String resourceUri) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
		if (resourceInfo != null) {
			resourceInfo.getSessionIds().remove(sessionId);
			if (resourceInfo.getSessionIds().isEmpty()) {
				// remove the info from the map if it was the last session
				resourceInfos.remove(resourceUri);
			}
		}
	}

	@Override
	public List<String> getResources() {
		return new ArrayList<>(resourceInfos.keySet());
	}

	@Override
	public List<String> getSessionsSubscribedToResource(String resourceUri) {
		InMemoryResourceInfo resourceInfo = getResourceInfo(resourceUri);
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
