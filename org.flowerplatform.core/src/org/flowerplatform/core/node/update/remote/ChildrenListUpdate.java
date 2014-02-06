package org.flowerplatform.core.mindmap.remote.update;

/**
 * @author Cristina Constantinescu
 */
public class ChildrenListUpdate {

	public static final String ADDED = "ADDED";
	public static final String REMOVED = "REMOVED";
	
	private String parentNodeId;
	
	private String type;
	
	private int index;
	
	private Object node;

	private long timestamp;
	
	public String getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public ChildrenListUpdate setParentNodeIdAs(String parentNodeId) {
		this.parentNodeId = parentNodeId;
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
