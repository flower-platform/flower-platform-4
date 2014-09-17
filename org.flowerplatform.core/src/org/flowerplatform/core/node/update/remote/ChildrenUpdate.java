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
package org.flowerplatform.core.node.update.remote;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public class ChildrenUpdate extends Update {
	
	private Node targetNode;
	
	private String fullTargetNodeAddedBeforeId;

	/**
	 * @author Claudiu Matei
	 */
	private transient List<ChildrenUpdate> removedNodes = new ArrayList<>();
	
	public Node getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	//CHECKSTYLE:OFF

	public ChildrenUpdate setTargetNodeAs(Node targetNode) {
		this.targetNode = targetNode;
		return this;
	}
	//CHECKSTYLE:ON

	public String getFullTargetNodeAddedBeforeId() {
		return fullTargetNodeAddedBeforeId;
	}

	public void setFullTargetNodeAddedBeforeId(String fullTargetNodeAddedBeforeId) {
		this.fullTargetNodeAddedBeforeId = fullTargetNodeAddedBeforeId;
	}

	/**
	 * @author Claudiu Matei
	 */
	public ChildrenUpdate setFullTargetNodeAddedBeforeIdAs(String fullTargetNodeAddedBeforeIdValue) {
		this.fullTargetNodeAddedBeforeId = fullTargetNodeAddedBeforeIdValue;
		return this;
	}
		
	public List<ChildrenUpdate> getRemovedNodes() {
		return removedNodes;
	}
	

	public void setRemovedNodes(List<ChildrenUpdate> removedNodes) {
		this.removedNodes = removedNodes;
	}

	/**
	 * @author Claudiu Matei
	 */
	public ChildrenUpdate setRemovedNodesAs(List<ChildrenUpdate> nodesRemoved) {
		this.removedNodes = nodesRemoved;
		return this;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	public String toString() {
		return "ChildrenUpdate [type=" + getType() + " targetNode=" + targetNode + " node=" + getFullNodeId() + ", timestamp=" + getTimestamp() + "]";
	}
	
}
