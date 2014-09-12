/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.core.node.remote;

import java.util.Map;

import org.flowerplatform.js_client.java.JsList;
import org.mozilla.javascript.NativeObject;

/**
 * <p>
 * This is a remote class (transferable to client). But only server -> client.
 * 
 * @see NodeService
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class Node {
	
	private String type;
	
	private String nodeUri;
	
	private Node parent;
	
	private JsList<Node> children;
	
	private NativeObject properties;
	
	public Node() {
		super();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNodeUri() {
		return nodeUri;
	}
	
	public void setNodeUri(String nodeUri) {
		this.nodeUri = nodeUri;
	}
		
	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public JsList<Node> getChildren() {
		return children;
	}

	public void setChildren(JsList<Node> children) {
		this.children = children;
	}

	public NativeObject getProperties() {		
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {		
		// Convert it to a NativeObject (yes, this could have been done directly)
		NativeObject nobj = new NativeObject();
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
		    nobj.defineProperty(entry.getKey(), entry.getValue(), NativeObject.EMPTY);
		}
		this.properties = nobj;
	}

	@Override
	public String toString() {
		return String.format("Node [fullNodeId = %s]", getNodeUri());
	}

}
