package org.flowerplatform.js_client.java.node;

import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.mozilla.javascript.NativeObject;

/**
 * @author Cristina Constantinescu
 */
public class ClientNode extends Node {

	private ClientNode parent;
	
	private List<ClientNode> children;
	
	public NativeObject properties;
					
	public ClientNode() {
		super();
		this.properties = new NativeObject();
	}

	public NativeObject getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {	
		super.setProperties(properties);
		NativeObject nobj = new NativeObject();
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			this.properties.defineProperty(entry.getKey(), entry.getValue(), NativeObject.EMPTY);
		}
		this.properties = nobj;
	}
	
	public List<ClientNode> getChildren() {
		return children;
	}

	public void setChildren(List<ClientNode> children) {
		this.children = children;
	}

	public ClientNode getParent() {
		return parent;
	}

	public void setParent(ClientNode parent) {
		this.parent = parent;
	}

}
