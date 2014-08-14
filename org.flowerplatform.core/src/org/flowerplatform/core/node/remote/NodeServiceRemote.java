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

import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;
import static org.flowerplatform.core.CoreConstants.TYPE_KEY;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.util.controller.TypeDescriptorRemote;

/**
 * @see NodeService
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 */
public class NodeServiceRemote {
	
	/**
	 * @author see class
	 */
	public List<Node> getChildren(String nodeUri, ServiceContext<NodeService> context) {
		if (context == null) {
			context = new ServiceContext<NodeService>(getNodeService());
		} else {
			context.setService(getNodeService());
		}
		return getNodeService().getChildren(CorePlugin.getInstance().getResourceService().getNode(nodeUri), context);		
	}
	
	/**
	 * @author see class
	 */
	public void setProperty(String fullNodeId, String property, Object value) {
		getNodeService().setProperty(CorePlugin.getInstance().getResourceService().getNode(fullNodeId), property, value, new ServiceContext<NodeService>(getNodeService()));	
	}
		
	/**
	 * @author see class
	 */
	public void unsetProperty(String fullNodeId, String property) {
		getNodeService().unsetProperty(CorePlugin.getInstance().getResourceService().getNode(fullNodeId), property, new ServiceContext<NodeService>(getNodeService()));	
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 */
	public String addChild(String parentFullNodeId, ServiceContext<NodeService> context) {
		if (context == null) {
			context = new ServiceContext<NodeService>(getNodeService());
		} else {
			context.setService(getNodeService());
		}
				
		Node parent = CorePlugin.getInstance().getResourceService().getNode(parentFullNodeId);
		String childType = (String) context.get(TYPE_KEY);
		if (childType == null) {
			throw new RuntimeException("Type for new child node must be provided in context!");
		}
		Node child = new Node(null, childType);
		
		getNodeService().addChild(parent, child, context);
		
		child.getOrPopulateProperties(new ServiceContext<NodeService>(getNodeService()));
		
		return child.getNodeUri();
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	public void removeChild(String parentFullNodeId, String childFullNodeId) {
		getNodeService().removeChild(CorePlugin.getInstance().getResourceService().getNode(parentFullNodeId), 
				CorePlugin.getInstance().getResourceService().getNode(childFullNodeId), new ServiceContext<NodeService>(getNodeService()));
	}
	
	public List<TypeDescriptorRemote> getRegisteredTypeDescriptors() {
		return CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getTypeDescriptorsRemote();
	}
		
	/**
	 * Sends a subtree to the client, based on the status of the client. Status of the client (i.e. <code>query</code> parameter)
	 * means the tree that the client is actually seeing (based on what nodes are expanded and collapsed).
	 */
	public NodeWithChildren refresh(FullNodeIdWithChildren query) {
		NodeWithChildren response = new NodeWithChildren();
		Node node = getNode(query.getFullNodeId());
		response.setNode(node);
		
		if (query.getVisibleChildren() == null) { 
			// no visible children on client => node not expanded, so don't continue
			// OR dummy query created for a child that isn't found in query's list of children
			return response;
		}
		
		for (Node child : getChildren(query.getFullNodeId(), null)) { // get new children list
			// search corresponding child in query
			FullNodeIdWithChildren childQuery = getChildQueryFromQuery(query, child.getNodeUri());
			if (childQuery == null) { 
				// not found, so this node is probably newly added;
				// create a dummy query and populate it only with the fullNodeId
				// this way, the recursive algorithm will go only one level deep,
				// the node's properties will be populated, and the recursion will stop
				childQuery = new FullNodeIdWithChildren();
				childQuery.setFullNodeId(child.getNodeUri());
			}
			NodeWithChildren childResponse = refresh(childQuery);
			if (response.getChildren() == null) {
				response.setChildren(new ArrayList<NodeWithChildren>());
			}
			response.getChildren().add(childResponse);
		}
		return response;
	}
	
	/**
	 * @author see class
	 */
	public Node getNode(String fullNodeId) {	
		return CorePlugin.getInstance().getResourceService().getNode(fullNodeId, new ServiceContext<ResourceService>().add(POPULATE_WITH_PROPERTIES, true));
	}
	
	private FullNodeIdWithChildren getChildQueryFromQuery(FullNodeIdWithChildren query, String fullChildNodeId) {
		for (FullNodeIdWithChildren child : query.getVisibleChildren()) {
			if (child.getFullNodeId().equals(fullChildNodeId)) {
				return child;
			}
		}
		return null;
	}
	
	private NodeService getNodeService() {
		return CorePlugin.getInstance().getNodeService();
	}
	
}
