package org.flowerplatform.core.node.update.remote;

import java.util.List;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public class ClientNodeStatus {

	private Node node;
	
	private long timestamp;
	
	private List<ClientNodeStatus> visibleChildren;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
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
