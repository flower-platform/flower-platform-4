package org.flowerplatform.core.mindmap.remote.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.mindmap.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public class NodeUpdate {

	private String nodeId;
	
	private Map<String, Object> updatedProperties;
	
	private List<Node> fullChildrenList;
	
	private List<ChildrenListUpdate> childrenListUpdates;
	
	private List<NodeUpdate> nodeUpdatesForChildren;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
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
