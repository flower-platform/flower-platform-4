package org.flowerplatform.core.node.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryRootNodeInfo extends RootNodeInfo {

	private List<String> sessions = new ArrayList<String>();
	
	private Map<String, Object> resourceIdToResource = new HashMap<String, Object>();
	
	@Override
	public List<String> getSessions() {
		return sessions;
	}
	
	@Override
	public void addSession(String sessionId) {
		sessions.add(sessionId);
	}
	
	@Override
	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}
	
	public Map<String, Object> getResourceIdToResource() {
		return resourceIdToResource;
	}

	public void setResourceIdToResource(Map<String, Object> resourceIdToResource) {
		this.resourceIdToResource = resourceIdToResource;
	}
	
}
