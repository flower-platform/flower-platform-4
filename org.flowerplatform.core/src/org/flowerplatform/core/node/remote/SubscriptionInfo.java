/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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

/**
 * @author Mariana Gheorghe
 */
public class SubscriptionInfo {

	private Node rootNode;
	
	private Node resourceNode;
	
	private String resourceSet;

	public SubscriptionInfo() {	
	}

	/**
	 *@author see class
	 **/
	public SubscriptionInfo(Node rootNode) {
		this(rootNode, null, null);
	}

	/**
	 *@author see class
	 **/
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
