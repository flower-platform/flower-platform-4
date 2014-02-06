package org.flowerplatform.core.mindmap.remote.update;

import java.util.List;

/**
 * @author Cristina Constantinescu
 */
public class ClientNodeStatus {

	private String nodeId;
	
	private long timestamp;
	
	private List<ClientNodeStatus> visibleChildren;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public List<ClientNodeStatus> getVisibleChildren() {
		return visibleChildren;
	}

	public void setVisibleChildren(List<ClientNodeStatus> visibleChildren) {
		this.visibleChildren = visibleChildren;
	}
	
}
