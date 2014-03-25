package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class ResourceNodeInfo {

	private Object rawResourceData;
	
	private String resourceCategory;
	
	private List<String> sessions = new ArrayList<String>();
	
	private List<Update> updates = new ArrayList<Update>();
	
	private long updateRequestedTimestamp;

	public Object getRawResourceData() {
		return rawResourceData;
	}
	
	public void setRawResourceData(Object rawResourceData) {
		this.rawResourceData = rawResourceData;
	}
	
	public String getResourceCategory() {
		return resourceCategory;
	}

	public void setResourceCategory(String resourceCategory) {
		this.resourceCategory = resourceCategory;
	}
	
	public List<String> getSessions() {
		return sessions;
	}
	
	public List<Update> getUpdates() {
		return updates;
	}
	
	public long getUpdateRequestedTimestamp() {
		return updateRequestedTimestamp;
	}
	
	public void setUpdateRequestedTimestamp(long updateRequestedTimestamp) {
		this.updateRequestedTimestamp = updateRequestedTimestamp;
	}
	
}
