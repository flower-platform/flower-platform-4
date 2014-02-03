/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.core.mindmap.remote;
		
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.mindmap.INodeDAO;
import org.flowerplatform.core.mindmap.MindMapNodeDAO;
import org.flowerplatform.core.mindmap.NodeUpdateDAO;
import org.flowerplatform.core.mindmap.remote.update.ChildrenListUpdate;
import org.flowerplatform.core.mindmap.remote.update.ClientNodeStatus;
import org.flowerplatform.core.mindmap.remote.update.NodeUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class MindMapService {
	
	private static final String FULL_CHILDREN_LIST_KEY = "fullChildrenList";
	
	private final static Logger logger = LoggerFactory.getLogger(MindMapService.class);
		
	private INodeDAO dao = new MindMapNodeDAO();	
	private NodeUpdateDAO nodeUpdateDAO = new NodeUpdateDAO();
	
	///////////////////////////////////////////////////////////////
	// Normal methods
	///////////////////////////////////////////////////////////////
	
	private long updateNodeTimestamp(Node node) {		
		return updateNodeTimestamp(node, false);
	}
	
	private long updateNodeTimestamp(Node node, boolean addOneMsIfSameAsLastTimestamp) {
		long timestamp = dao.updateNodeTimestamp(node, addOneMsIfSameAsLastTimestamp);
		
		nodeUpdateDAO.addPropertyUpdate(node.getId(), timestamp, "timestamp", timestamp);
		return timestamp;
	}
		
	private NodeUpdate checkForNodeUpdates(ClientNodeStatus clientNodeStatus, Map<String, Object> context, List<ChildrenListUpdate> listUpdates) {
		NodeUpdate nodeUpdate = new NodeUpdate();
		nodeUpdate.setNodeId(clientNodeStatus.getNodeId());
		
		if (context != null && context.containsKey(FULL_CHILDREN_LIST_KEY)) {
			nodeUpdate.setFullChildrenList(getChildrenForNodeId(nodeUpdate.getNodeId()));
			return nodeUpdate;
		}
				
		nodeUpdate.setUpdatedProperties(
				nodeUpdateDAO.getPropertyUpdates(
						clientNodeStatus.getNodeId(), 
						clientNodeStatus.getTimestamp()));
					
		if (clientNodeStatus.getVisibleChildren() != null) {
			listUpdates.addAll(
					nodeUpdateDAO.getChildrenListUpdates(
							clientNodeStatus.getNodeId(), 
							clientNodeStatus.getTimestamp()));
			
			for (ClientNodeStatus childNodeStatus : clientNodeStatus.getVisibleChildren()) {
				nodeUpdate.addNodeUpdatesForChild(checkForNodeUpdates(childNodeStatus, context, listUpdates));
			}
		}
		return nodeUpdate;
	}
	
	///////////////////////////////////////////////////////////////
	// Remote methods
	///////////////////////////////////////////////////////////////
	
	public void reload() throws Exception {
		try {
			dao.load(null);			
		} catch (Exception e) {
			logger.error("Exception thrown while loading resource!", e);
			throw e;	
		}		
	}

	public void save() throws Exception {		
		try {
			dao.save();
		} catch (Exception e) {
			logger.error("Exception thrown while saving resource!", e);
			throw e;
		}		
	}	
	
	public List<Node> getChildrenForNodeId(String nodeId) {		
		return dao.getChildrenForNodeId(nodeId);
	}
	
	public void addNode(String parentNodeId, String type) {
		Node parentNode = dao.getNodeFromId(parentNodeId);
		Node childNode = dao.addNode(parentNode, type);
		
		int index = dao.getNodeChildPosition(parentNode, childNode);
		
		nodeUpdateDAO.addChildrenListUpdate(parentNodeId, updateNodeTimestamp(parentNode), ChildrenListUpdate.ADDED, index, childNode);
		nodeUpdateDAO.addPropertyUpdate(parentNodeId, updateNodeTimestamp(parentNode), "hasChildren", true);		
	}
	
	public void removeNode(String nodeId) {
		Node childNode = dao.getNodeFromId(nodeId);		
		Node parentNode = dao.getParentNode(childNode);
		
		int index = dao.getNodeChildPosition(parentNode, childNode);
		
		dao.removeNode(childNode);
			
		nodeUpdateDAO.addChildrenListUpdate(parentNode.getId(), updateNodeTimestamp(parentNode), ChildrenListUpdate.REMOVED, index, nodeId);
		nodeUpdateDAO.addPropertyUpdate(parentNode.getId(), updateNodeTimestamp(parentNode), "hasChildren", dao.getNodeChildCount(parentNode) != 0);		
	}	
	
	public void moveNode(String nodeId, String newParentId, int newIndex) {		
		if (newIndex != -1) {
			Node childNode = dao.getNodeFromId(nodeId);
			Node oldParentNode = dao.getParentNode(childNode);		
			Node newParentNode = dao.getNodeFromId(newParentId);
			
			String oldParentId = oldParentNode.getId();
			if (newIndex == -2) {
				newIndex = dao.getNodeChildCount(newParentNode);				
			}
			
			removeNode(nodeId);
			
			if (oldParentId.equals(newParentId) && newIndex > dao.getNodeChildCount(oldParentNode) - 1) {
				newIndex--;
			}	
			
			dao.insertNode(newParentNode, childNode, newIndex);
			
			nodeUpdateDAO.addChildrenListUpdate(newParentId, updateNodeTimestamp(newParentNode, true), ChildrenListUpdate.ADDED, newIndex, childNode);
			nodeUpdateDAO.addPropertyUpdate(newParentId, updateNodeTimestamp(newParentNode), "hasChildren", dao.getNodeChildCount(newParentNode) != 0);	
		}		
	}
	
	public List<Property> getPropertiesData(String nodeType) {		
		return dao.getPropertiesData(nodeType);		
	}
	
	public void setProperty(String nodeId, String propertyName, String propertyValue) {
		Node node = dao.getNodeFromId(nodeId);
		
		dao.setProperty(node, propertyName, propertyValue);
		
		nodeUpdateDAO.addPropertyUpdate(nodeId, updateNodeTimestamp(node), propertyName, propertyValue);
	}

	public NodeUpdate checkForNodeUpdates(ClientNodeStatus clientNodeStatus, Map<String, Object> context) {				
		List<ChildrenListUpdate> listUpdates = new ArrayList<ChildrenListUpdate>();
		
		NodeUpdate nodeUpdate = checkForNodeUpdates(clientNodeStatus, context, listUpdates);
		
		Collections.sort(listUpdates, new Comparator<ChildrenListUpdate>() {
			public int compare(ChildrenListUpdate u1, ChildrenListUpdate u2) {
				return Long.compare(u1.getTimestamp1(), u2.getTimestamp1());
            }
		});		
		
		nodeUpdate.setChildrenListUpdates(listUpdates);
		
		return nodeUpdate;
	}	
	
}
