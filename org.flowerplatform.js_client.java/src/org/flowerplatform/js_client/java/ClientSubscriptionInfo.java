package org.flowerplatform.js_client.java;

public class ClientSubscriptionInfo {

	private ClientNode rootNode;
	
	private ClientNode resourceNode;
	
	private String resourceSet;
	
	public ClientSubscriptionInfo() {	
	}
	
	public ClientNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(ClientNode rootNode) {
		this.rootNode = rootNode;
	}

	public ClientNode getResourceNode() {
		return resourceNode;
	}

	public void setResourceNode(ClientNode resourceNode) {
		this.resourceNode = resourceNode;
	}

	public String getResourceSet() {
		return resourceSet;
	}

	public void setResourceSet(String resourceSet) {
		this.resourceSet = resourceSet;
	}
	
}
