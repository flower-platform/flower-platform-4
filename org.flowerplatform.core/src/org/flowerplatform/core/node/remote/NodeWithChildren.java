package org.flowerplatform.core.node.remote;

import java.util.List;

/**
 * @author Cristina Constantinescu
 */
public class NodeWithChildren {

	private Node node;
	
	private List<NodeWithChildren> children;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public List<NodeWithChildren> getChildren() {
		return children;
	}

	public void setChildren(List<NodeWithChildren> children) {
		this.children = children;
	}	
	
}
