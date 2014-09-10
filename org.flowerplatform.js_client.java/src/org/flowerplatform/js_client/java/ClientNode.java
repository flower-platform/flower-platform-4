package org.flowerplatform.js_client.java;

import org.mozilla.javascript.NativeObject;

/**
 * @author Cristina Constantinescu
 */
public class ClientNode {

	private String nodeUri;
	
	private String type;
	
	private ClientNode parent;
	
	private JsList<ClientNode> children;
	
	private NativeObject properties;
		
	public ClientNode() {
		properties = new NativeObject();
	}

	public String getNodeUri() {
		return nodeUri;
	}
	
	public String getType() {
		return type;
	}

	public void setNodeUri(String nodeUri) {
		this.nodeUri = nodeUri;		
	}

	public void setType(String type) {
		this.type = type;		
	}

	public JsList<?> getChildren() {
		return children;
	}

	public void setChildren(JsList<ClientNode> children) {
		this.children = children;
	}

	public Object getProperties() {
		return properties;
	}

	public void setProperties(NativeObject properties) {
		this.properties = properties;
	}

	public ClientNode getParent() {
		return parent;
	}

	public void setParent(ClientNode parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "ClientNode [nodeUri=" + nodeUri + "]";
	}
		
}
