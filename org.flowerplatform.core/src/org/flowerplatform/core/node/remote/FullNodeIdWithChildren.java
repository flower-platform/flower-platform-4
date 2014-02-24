package org.flowerplatform.core.node.remote;

import java.util.List;

/**
 * @see NodeServiceRemote#refresh(FullNodeIdWithChildren)
 * @author Cristina Constantinescu
 */
public class FullNodeIdWithChildren {

	private String fullNodeId;
	
	private List<FullNodeIdWithChildren> visibleChildren;

	public String getFullNodeId() {
		return fullNodeId;
	}

	public void setFullNodeId(String fullNodeId) {
		this.fullNodeId = fullNodeId;
	}

	public List<FullNodeIdWithChildren> getVisibleChildren() {
		return visibleChildren;
	}

	public void setVisibleChildren(List<FullNodeIdWithChildren> visibleChildren) {
		this.visibleChildren = visibleChildren;
	}
	
}
