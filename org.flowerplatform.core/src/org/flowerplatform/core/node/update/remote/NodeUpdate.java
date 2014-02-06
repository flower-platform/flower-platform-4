package org.flowerplatform.core.node.update.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public class NodeUpdate {

	private Node node;
	
	private Map<String, Object> updatedProperties;
	
	private List<Node> fullChildrenList;
	
	private List<ChildrenListUpdate> childrenListUpdates;
	
	private List<NodeUpdate> nodeUpdatesForChildren;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Map<String, Object> getUpdatedProperties() {
		return updatedProperties;
	}

	public void setUpdatedProperties(Map<String, Object> updatedProperties) {
		this.updatedProperties = updatedProperties;
	}

	public List<Node> getFullChildrenList() {
		return fullChildrenList;
	}

	public void setFullChildrenList(List<Node> fullChildrenList) {
		this.fullChildrenList = fullChildrenList;
	}

	public List<ChildrenListUpdate> getChildrenListUpdates() {
		return childrenListUpdates;
	}

	public void setChildrenListUpdates(List<ChildrenListUpdate> childrenListUpdates) {
		this.childrenListUpdates = childrenListUpdates;
	}

	public List<NodeUpdate> getNodeUpdatesForChildren() {
		return nodeUpdatesForChildren;
	}

	public void setNodeUpdatesForChildren(List<NodeUpdate> nodeUpdatesForChildren) {
		this.nodeUpdatesForChildren = nodeUpdatesForChildren;
	}	
	
	public void addNodeUpdatesForChild(NodeUpdate childNodeUpdate) {
		if (nodeUpdatesForChildren == null) {
			nodeUpdatesForChildren = new ArrayList<NodeUpdate>();
		}
		nodeUpdatesForChildren.add(childNodeUpdate);
	}
	
}
