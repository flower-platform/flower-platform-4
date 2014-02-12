package org.flowerplatform.core.node.update.remote;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public class Update implements Comparable<Update> {

	private Node node;
	
	private long timestamp;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Update setNodeAs(Node node) {
		this.node = node;
		return this;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public Update setTimestampAs(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	@Override
	public int compareTo(Update o) {
		return Long.compare(getTimestamp(), o.getTimestamp());
	}

	@Override
	public String toString() {
		return "Update [node=" + node + ", timestamp=" + timestamp + "]";
	}
		
}
