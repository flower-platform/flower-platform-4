package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryResourceInfo {

	private Object resourceData;
	
	private List<String> sessionIds = new ArrayList<String>();
	
	private long lastPing;

	public Object getResourceData() {
		return resourceData;
	}

	public void setResourceData(Object resourceData) {
		this.resourceData = resourceData;
	}

	public List<String> getSessionIds() {
		return sessionIds;
	}

	public void setSessionIds(List<String> sessionIds) {
		this.sessionIds = sessionIds;
	}

	public long getLastPing() {
		return lastPing;
	}

	public void setLastPing(long lastPing) {
		this.lastPing = lastPing;
	}
	
}
