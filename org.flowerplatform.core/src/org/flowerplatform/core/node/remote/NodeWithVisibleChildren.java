package org.flowerplatform.core.node.remote;

import java.util.List;

/**
 * @author Cristina Constantinescu
 */
public class NodeWithVisibleChildren {

	private String fullNodeId;
	
	private List<NodeWithVisibleChildren> visibleChildren;

	public String getFullNodeId() {
		return fullNodeId;
	}

	public void setFullNodeId(String fullNodeId) {
		this.fullNodeId = fullNodeId;
	}

	public List<NodeWithVisibleChildren> getVisibleChildren() {
		return visibleChildren;
	}

	public void setVisibleChildren(List<NodeWithVisibleChildren> visibleChildren) {
		this.visibleChildren = visibleChildren;
	}
		
}
