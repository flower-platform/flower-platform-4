package org.flowerplatform.core.node.remote;

import java.util.List;

/**
 * @author Cristina Constantinescu
 */
public class NodeWithVisibleChildren {

	private Node node;
	
	private List<NodeWithVisibleChildren> visibleChildren;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public List<NodeWithVisibleChildren> getVisibleChildren() {
		return visibleChildren;
	}

	public void setVisibleChildren(List<NodeWithVisibleChildren> visibleChildren) {
		this.visibleChildren = visibleChildren;
	}
		
}
