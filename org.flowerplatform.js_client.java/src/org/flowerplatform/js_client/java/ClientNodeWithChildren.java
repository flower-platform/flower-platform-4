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
package org.flowerplatform.js_client.java;

/**
 * @author Elena Posea
 */
public class ClientNodeWithChildren {

	private ClientNode node;
	
	private JsList<ClientNodeWithChildren> children;

	public ClientNode getNode() {
		return node;
	}

	public void setNode(ClientNode node) {
		this.node = node;
	}

	public JsList<ClientNodeWithChildren> getChildren() {
		return children;
	}

	public void setChildren(JsList<ClientNodeWithChildren> children) {
		this.children = children;
	}	
	
	public String getNodeUri() {
		return node.getNodeUri();
	}
	
	public String getType() {
		return node.getType();
	}

	public void setNodeUri(String nodeUri) {
		this.node.setNodeUri(nodeUri);		
	}

	public void setType(String type) {
		this.node.setType(type);		
	}


}