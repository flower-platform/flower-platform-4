package org.flowerplatform.core.node.update.remote;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public class ChildrenListUpdate {

	public static final String ADDED = "ADDED";
	public static final String REMOVED = "REMOVED";
	
	private Node parentNode;
	
	private String type;
	
	private int index;
	
	private Object node;

	private long timestamp;
	
	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public ChildrenListUpdate setParentNodeAs(Node parentNode) {
		this.parentNode = parentNode;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ChildrenListUpdate setTypeAs(String type) {
		this.type = type;
		return this;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ChildrenListUpdate setIndexAs(int index) {
		this.index = index;
		return this;
	}
	
	public Object getNode() {
		return node;
	}

	public void setNode(Object node) {
		this.node = node;
	}
		
	public ChildrenListUpdate setNodeAs(Object node) {
		this.node = node;
		return this;
	}

	public long getTimestamp1() {
		return timestamp;
	}

	public ChildrenListUpdate setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}
		
}
