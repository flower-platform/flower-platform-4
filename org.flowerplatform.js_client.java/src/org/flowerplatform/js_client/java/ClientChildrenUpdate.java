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

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Elena Posea
 */
public class ClientChildrenUpdate extends Update {
	
	private ClientNode targetNode;
	
	private String fullTargetNodeAddedBeforeId;

	private transient List<ClientChildrenUpdate> removedNodes = new ArrayList<>();
	
	public ClientNode getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(ClientNode targetNode) {
		this.targetNode = targetNode;
	}

	public ClientChildrenUpdate setTargetNodeAs(ClientNode targetNode) {
		this.targetNode = targetNode;
		return this;
	}
	
	public String getFullTargetNodeAddedBeforeId() {
		return fullTargetNodeAddedBeforeId;
	}

	public void setFullTargetNodeAddedBeforeId(String fullTargetNodeAddedBeforeId) {
		this.fullTargetNodeAddedBeforeId = fullTargetNodeAddedBeforeId;
	}

	public ClientChildrenUpdate setFullTargetNodeAddedBeforeIdAs(String fullTargetNodeAddedBeforeId) {
		this.fullTargetNodeAddedBeforeId = fullTargetNodeAddedBeforeId;
		return this;
	}
		
	public List<ClientChildrenUpdate> getRemovedNodes() {
		return removedNodes;
	}
	

	public void setRemovedNodes(List<ClientChildrenUpdate> removedNodes) {
		this.removedNodes = removedNodes;
	}

	
	public ClientChildrenUpdate setRemovedNodesAs(List<ClientChildrenUpdate> removedNodes) {
		this.removedNodes = removedNodes;
		return this;
	}
	
	public String toString() {
		return "ChildrenUpdate [type=" + getType() + " targetNode=" + targetNode + " node=" + getFullNodeId() + ", timestamp=" + getTimestamp() + "]";
	}
	
}
