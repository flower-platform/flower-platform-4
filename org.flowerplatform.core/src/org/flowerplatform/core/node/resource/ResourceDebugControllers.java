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
package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CorePlugin.ROOT_TYPE;
import static org.flowerplatform.core.RemoteMethodInvocationListener.LAST_UPDATE_TIMESTAMP;
import static org.flowerplatform.core.ServiceContext.DONT_PROCESS_OTHER_CONTROLLERS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.NodePropertiesConstants;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * @author Mariana Gheorghe
 */
public class ResourceDebugControllers {

	private final String DEBUG = "_debug";
	
	private final String SESSION_CATEGORY = TypeDescriptor.CATEGORY_PREFIX + "_debugSession";
	private final String RESOURCE_NODE_INFO_CATEGORY = TypeDescriptor.CATEGORY_PREFIX + "_debugResourceNodeInfo";
	
	private final String SESSION = "_debugSession";
	private final String RESOURCE_NODE_INFO2 = "_debugResourceNodeInfo2";
	private final String SESSION2 = "_debugSession2";
	
	private final String RESOURCE_NODE_INFO = "_debugResourceNodeInfo";
	private final String SESSION3 = "_debugSession3";
	
	public void registerControllers() {
		
		// add _debug node to root
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ROOT_TYPE)
		.addAdditiveController(ChildrenProvider.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext context) {
				return Collections.singletonList(new Node("(" + DEBUG + Node.FULL_NODE_ID_SEPARATOR + Node.FULL_NODE_ID_SEPARATOR + ")"));
			}
		}.setOrderIndexAs(10000));
		
		// _debug node
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(DEBUG)
		.addAdditiveController(ChildrenProvider.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext context) {
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext context) {
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				List<Node> children = new ArrayList<Node>();
				
				// sessions
				for (String sessionId : CorePlugin.getInstance().getResourceInfoService().getSubscribedSessions()) {
					Node session = new Node(SESSION, node.getResource(), sessionId, null);
					children.add(session);
				}
				
				// resources
				for (String resourceId : CorePlugin.getInstance().getResourceInfoService().getResources()) {
					Node resource = new Node(RESOURCE_NODE_INFO, node.getResource(), resourceId.replace("|", "+"), null);
					children.add(resource);
				}
				
				return children;
			}
		})
		.addAdditiveController(PropertiesProvider.PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, ServiceContext context) {
				node.getProperties().put(NodePropertiesConstants.NAME, DEBUG);
			}
		});
		
		// properties for session
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(SESSION_CATEGORY)
		.addAdditiveController(PropertiesProvider.PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, ServiceContext context) {
				String sessionId = node.getIdWithinResource().split(" ")[0];
				node.getProperties().put(NodePropertiesConstants.NAME, "Session " + sessionId);
				node.getProperties().put("ip", CorePlugin.getInstance().getResourceInfoService().getSessionProperty(sessionId, "ip"));
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
			}
		}.setOrderIndexAs(-500000))
		.addAdditiveController(PropertyDescriptor.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("ip"));
		
		// session as child of _debug; children are resources
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION)
		.addCategory(SESSION_CATEGORY)
		.addAdditiveController(ChildrenProvider.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext context) {
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext context) {
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				List<Node> children = new ArrayList<Node>();
				String sessionId = node.getIdWithinResource().split(" ")[0];
				for (String resourceId : CorePlugin.getInstance().getResourceInfoService().getResourcesSubscribedBySession(sessionId)) {
					Node resource = new Node(RESOURCE_NODE_INFO2, node.getResource(), resourceId.replace("|", "+"), null);
					children.add(resource);
				}
				return children;
			}
		});
		
		// properties for resource
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(RESOURCE_NODE_INFO_CATEGORY)
		.addAdditiveController(PropertiesProvider.PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, ServiceContext context) {
				String resourceId = node.getIdWithinResource().replace("+", "|").split(" ")[0];
				node.getProperties().put(NodePropertiesConstants.NAME, "Resource " + resourceId);
				long timestamp = CorePlugin.getInstance().getResourceInfoService().getUpdateRequestedTimestamp(resourceId);
				node.getProperties().put(LAST_UPDATE_TIMESTAMP, timestamp);
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
			}
		}.setOrderIndexAs(-500000))
		.addAdditiveController(PropertyDescriptor.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(LAST_UPDATE_TIMESTAMP));
		
		// resource as child of session; children are sessions
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCE_NODE_INFO2)
		.addCategory(RESOURCE_NODE_INFO_CATEGORY)
		.addAdditiveController(ChildrenProvider.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext context) {
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext context) {
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				
				List<Node> children = new ArrayList<Node>();
				String resourceId = node.getIdWithinResource().replace("+", "|");
				for (String sessionId : CorePlugin.getInstance().getResourceInfoService().getSessionsSubscribedToResource(resourceId)) {
					Node session = new Node(SESSION2, node.getResource(), sessionId, null);
					children.add(session);
				}
				return children;
			}
		});
		
		// session as child of resource
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION2)
		.addCategory(SESSION_CATEGORY);
		
		// resource as child of _debug; children are sessions
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCE_NODE_INFO)
		.addCategory(RESOURCE_NODE_INFO_CATEGORY)
		.addAdditiveController(ChildrenProvider.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext context) {
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext context) {
				context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
				
				List<Node> children = new ArrayList<Node>();
				String resourceId = node.getIdWithinResource().replace("+", "|");
				for (String sessionId : CorePlugin.getInstance().getResourceInfoService().getSessionsSubscribedToResource(resourceId)) {
					Node session = new Node(SESSION3, node.getResource(), sessionId, null);
					children.add(session);
				}
				return children;
			}
		});
		
		// session as child of resource
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION3)
		.addCategory(SESSION_CATEGORY);
	}
	
}
