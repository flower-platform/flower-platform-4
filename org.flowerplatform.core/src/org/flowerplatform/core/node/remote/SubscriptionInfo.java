package org.flowerplatform.core.node.remote;

/**
 * @author Mariana Gheorghe
 */
public class SubscriptionInfo {

	private Node rootNode;
	
	private Node resourceNode;
	
	private String resourceSet;
	
	public SubscriptionInfo(Node rootNode) {
		this(rootNode, null, null);
	}

	public SubscriptionInfo(Node rootNode, Node resourceNode, String resourceSet) {
		super();
		this.rootNode = rootNode;
		this.resourceNode = resourceNode;
		this.resourceSet = resourceSet;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}
	
	public Node getResourceNode() {
		return resourceNode;
	}

	public void setResourceNode(Node resourceNode) {
		this.resourceNode = resourceNode;
	}

	public String getResourceSet() {
		return resourceSet;
	}

	public void setResourceSet(String resourceSet) {
		this.resourceSet = resourceSet;
	}
	
}
