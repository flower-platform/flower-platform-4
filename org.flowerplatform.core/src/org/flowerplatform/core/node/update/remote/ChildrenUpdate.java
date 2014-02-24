package org.flowerplatform.core.node.update.remote;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public class ChildrenUpdate extends Update {

	public static final String ADDED = "ADDED";
	public static final String REMOVED = "REMOVED";
		
	private String type;
	
	private Node targetNode;
	
	private String fullTargetNodeAddedBeforeId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ChildrenUpdate setTypeAs(String type) {
		this.type = type;
		return this;
	}
		
	public Node getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	public ChildrenUpdate setTargetNodeAs(Node targetNode) {
		this.targetNode = targetNode;
		return this;
	}
	
	public String getFullTargetNodeAddedBeforeId() {
		return fullTargetNodeAddedBeforeId;
	}

	public void setFullTargetNodeAddedBeforeId(String fullTargetNodeAddedBeforeId) {
		this.fullTargetNodeAddedBeforeId = fullTargetNodeAddedBeforeId;
	}

	public ChildrenUpdate setFullTargetNodeAddedBeforeIdAs(String fullTargetNodeAddedBeforeId) {
		this.fullTargetNodeAddedBeforeId = fullTargetNodeAddedBeforeId;
		return this;
	}
	
	@Override
	public String toString() {
		return "ChildrenUpdate [type=" + type + " targetNode=" + targetNode + " node=" + getFullNodeId() + ", timestamp=" + getTimestamp() + "]";
	}
	
}
