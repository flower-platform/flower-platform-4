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
public class ClientSubscriptionInfo {

	private ClientNode rootNode;
	
	private ClientNode resourceNode;
	
	private String resourceSet;
	
	public ClientSubscriptionInfo() {	
	}
	
	public ClientSubscriptionInfo(ClientNode rootNode) {
		this(rootNode, null, null);
	}

	public ClientSubscriptionInfo(ClientNode rootNode, ClientNode resourceNode, String resourceSet) {
		super();
		this.rootNode = rootNode;
		this.resourceNode = resourceNode;
		this.resourceSet = resourceSet;
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