package org.flowerplatform.core.node.update.remote;

import java.util.Date;

/**
 * @author Cristina Constantinescu
 */
public class Update implements Comparable<Update> {

	private String fullNodeId;
	
	private long timestamp = new Date().getTime();
	
	public String getFullNodeId() {
		return fullNodeId;
	}

	public void setFullNodeId(String fullNodeId) {
		this.fullNodeId = fullNodeId;
	}

	public Update setFullNodeIdAs(String fullNodeId) {
		this.fullNodeId = fullNodeId;
		return this;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public int compareTo(Update o) {
		return Long.compare(getTimestamp(), o.getTimestamp());
	}

	@Override
	public String toString() {
		return "Update [node=" + fullNodeId + ", timestamp=" + timestamp + "]";
	}
		
}
