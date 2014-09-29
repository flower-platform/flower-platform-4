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

import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;
import static org.flowerplatform.core.CoreConstants.TYPE_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.TypeDescriptorRemote;

/**
 * @see NodeService
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 */
public class NodeServiceRemote {
	
	/**
	 *@author see class
	 **/
	public List<Node> getChildren(String nodeUri, Map<String, Object> context) {
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>(getNodeService());
		if (context != null) {			
			serviceContext.setContext(context);	
		}
		return getNodeService().getChildren(CorePlugin.getInstance().getResourceService().getNode(nodeUri), serviceContext);		
	}

	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 * @author Claudiu Matei
	 */
	public void setProperty(String fullNodeId, String property, Object value) {
		ResourceSetService rss = CorePlugin.getInstance().getResourceSetService();
		Node node = CorePlugin.getInstance().getResourceService().getNode(fullNodeId);
		node.getOrPopulateProperties(new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		String typeLabel = ResourcesPlugin.getInstance().getLabelForNodeType(node.getType());
		String nodeName = (String) node.getPropertyValue(getNodeTitleProperty(node.getType()));
		String propertyLabel = typeLabel + (nodeName != null ? "(" + nodeName + ")" : "") + "." + property;
		String commandTitle = ResourcesPlugin.getInstance().getMessage("commandStack.command.setProperty", propertyLabel, value.toString());
		rss.startCommand(rss.getResourceSet(fullNodeId), commandTitle);
		getNodeService().setProperty(node, property, value, new ServiceContext<NodeService>(getNodeService()));	
	}
	
	/**
	 * @author Valentina Bojan
	 */
	public void setProperties(String fullNodeId, Map<String, Object> properties) {
		// TODO VB:
		Node node = CorePlugin.getInstance().getResourceService().getNode(fullNodeId);
		getNodeService().setProperties(node, properties, new ServiceContext<NodeService>(getNodeService()));
	}
		
	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 * @author Claudiu Matei
	 */
	public void unsetProperty(String fullNodeId, String property) {
		ResourceSetService rss = CorePlugin.getInstance().getResourceSetService();
		Node node = CorePlugin.getInstance().getResourceService().getNode(fullNodeId);
		node.getOrPopulateProperties(new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		String typeLabel = ResourcesPlugin.getInstance().getLabelForNodeType(node.getType());
		String nodeName = (String) node.getPropertyValue(getNodeTitleProperty(node.getType()));
		String propertyLabel = typeLabel + (nodeName != null ? "(" + nodeName + ")" : "") + "." + property;
		String commandTitle = ResourcesPlugin.getInstance().getMessage("commandStack.command.unsetProperty", propertyLabel);
		rss.startCommand(rss.getResourceSet(fullNodeId), commandTitle);
		getNodeService().unsetProperty(node, property, new ServiceContext<NodeService>(getNodeService()));	
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 * @author Claudiu Matei
	 */
	public String addChild(String parentNodeUri, Map<String, Object> context) {
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>(getNodeService());
		if (context != null) {			
			serviceContext.setContext(context);	
		}
				
		Node parent = CorePlugin.getInstance().getResourceService().getNode(parentNodeUri);
		String childType = (String) context.get(TYPE_KEY);
		if (childType == null) {
			throw new RuntimeException("Type for new child node must be provided in context!");
		}
		Node child = new Node(null, childType);
		
		ResourceSetService rss = CorePlugin.getInstance().getResourceSetService();
		String childTypeLabel = ResourcesPlugin.getInstance().getLabelForNodeType(childType);
		String childName = (String) context.get(getNodeTitleProperty(childType));
		String nodeLabel = childTypeLabel + (childName != null ? "(" + childName + ")" : "");
		String commandTitle = ResourcesPlugin.getInstance().getMessage("commandStack.command.addChild", nodeLabel);
		rss.startCommand(rss.getResourceSet(parentNodeUri), commandTitle);
		
		getNodeService().addChild(parent, child, serviceContext);
		
		child.getOrPopulateProperties(new ServiceContext<NodeService>(getNodeService()));
		
		return child.getNodeUri();
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 * @author Claudiu Matei
	 */
	public void removeChild(String parentNodeUri, String childNodeUri) {
		ResourceSetService rss = CorePlugin.getInstance().getResourceSetService();
		Node childNode = CorePlugin.getInstance().getResourceService().getNode(childNodeUri);
		childNode.getOrPopulateProperties(new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		String childTypeLabel;
		try {
			childTypeLabel = ResourcesPlugin.getInstance().getLabelForNodeType(childNode.getType());
		} catch (MissingResourceException e) {
			childTypeLabel = childNode.getType();
		}
		String childName = (String) childNode.getPropertyValue(getNodeTitleProperty(childNode.getType()));
		String nodeLabel = childTypeLabel + (childName != null ? "(" + childName + ")" : "");
		String commandTitle = ResourcesPlugin.getInstance().getMessage("commandStack.command.removeChild", nodeLabel);
		rss.startCommand(rss.getResourceSet(parentNodeUri), commandTitle);
		
		getNodeService().removeChild(CorePlugin.getInstance().getResourceService().getNode(parentNodeUri), 
				CorePlugin.getInstance().getResourceService().getNode(childNodeUri), new ServiceContext<NodeService>(getNodeService()));
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
	 *@author see class
	 **/
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
	
	private String getNodeTitleProperty(String nodeType) {
		GenericValueDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(nodeType)
				.getSingleController(CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR, null);
		return (String) descriptor.getValue();
	}
}
