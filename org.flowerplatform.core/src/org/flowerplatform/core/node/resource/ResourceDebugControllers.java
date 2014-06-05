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

import static org.flowerplatform.core.CoreConstants.DEBUG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class ResourceDebugControllers {

	private final String RESOURCES = DEBUG + "ServerResources";
	
	private final String RESOURCES_CLIENT = DEBUG + "ClientResources";
	
	private final String SESSION = DEBUG + "Session";
	private final String RESOURCE_NODE_INFO2 = DEBUG + "ResourceNodeInfo2";
	private final String SESSION2 = DEBUG + "Session2";
	
	private final String RESOURCE_NODE_INFO = DEBUG + "ResourceNodeInfo";
	private final String SESSION3 = DEBUG + "Session3";
	
	private final String SESSION_CATEGORY = UtilConstants.CATEGORY_PREFIX + SESSION;
	private final String RESOURCE_NODE_INFO_CATEGORY = UtilConstants.CATEGORY_PREFIX + RESOURCE_NODE_INFO;
	
	class RootDebugController extends AbstractController implements IChildrenProvider {
		
		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Collections.singletonList(new Node(DEBUG, DEBUG, null));
		}
	}
	
	class DebugController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Arrays.asList(new Node(RESOURCES, DEBUG, null), new Node(RESOURCES_CLIENT, DEBUG, null));
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, DEBUG);
		}
	}
	
	class ResourcesClientDebugController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, "Client Resources");
		}

		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return null;
		}

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
	}
	
	class ResourcesDebugController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			List<Node> children = new ArrayList<Node>();
			
			// sessions
			for (String sessionId : CorePlugin.getInstance().getSessionService().getSubscribedSessions()) {
				Node session = new Node(SESSION, node.getSchemeSpecificPart(), sessionId);
				children.add(session);
			}
			
			// resources
			for (String resourceId : CorePlugin.getInstance().getResourceService().getResources()) {
				Node resource = new Node(RESOURCE_NODE_INFO, node.getSchemeSpecificPart(), resourceId);
				children.add(resource);
			}
			
			return children;
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, "Resources");
		}
	}
	
	class SessionCategoryDebugController extends AbstractController implements IPropertiesProvider {
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			String sessionId = node.getFragment();
			node.getProperties().put(CoreConstants.NAME, "Session " + sessionId);
			node.getProperties().put("ip", CorePlugin.getInstance().getSessionService().getSessionProperty(sessionId, "ip"));
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		}
	}
	
	class SessionDebugController extends AbstractController implements IChildrenProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
			List<Node> children = new ArrayList<Node>();
			String sessionId = node.getFragment();
			for (String resourceId : CorePlugin.getInstance().getSessionService().getResourcesSubscribedBySession(sessionId)) {
				Node resource = new Node(RESOURCE_NODE_INFO2, node.getSchemeSpecificPart(), resourceId);
				children.add(resource);
			}
			return children;
		}
	}
	
	class ResourceNodeInfoCategoryDebugController extends AbstractController implements IPropertiesProvider {
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			String resourceId = node.getFragment();
			node.getProperties().put(CoreConstants.NAME, "Resource " + resourceId);
			long timestamp = CorePlugin.getInstance().getResourceService().getUpdateRequestedTimestamp(resourceId);
			node.getProperties().put(CoreConstants.LAST_UPDATE_TIMESTAMP, timestamp);
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		}
	}
	
	class ResourceNodeInfoDebugController extends AbstractController implements IChildrenProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
			
			List<Node> children = new ArrayList<Node>();
			String resourceId = node.getFragment();
			for (String sessionId : CorePlugin.getInstance().getResourceService().getSessionsSubscribedToResource(resourceId)) {
				Node session = new Node(SESSION3, node.getSchemeSpecificPart(), sessionId);
				children.add(session);
			}
			return children;
		}
	}
	
	class ResourceNodeInfo2DebugController extends AbstractController implements IChildrenProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
			
			List<Node> children = new ArrayList<Node>();
			String resourceId = node.getFragment();
			for (String sessionId : CorePlugin.getInstance().getResourceService().getSessionsSubscribedToResource(resourceId)) {
				Node session = new Node(SESSION2, node.getSchemeSpecificPart(), sessionId);
				children.add(session);
			}
			return children;
		}
	}
	
	public void registerControllers() {
		
		// add debug node to root
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.ROOT_TYPE)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new RootDebugController().setOrderIndexAs(10000));
		
		DebugController debugController = new DebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(DEBUG)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, debugController)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, debugController);
		
		// debug node
		
		ResourcesDebugController resourcesDebugController = new ResourcesDebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCES)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, resourcesDebugController)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, resourcesDebugController);
		
		ResourcesClientDebugController resourcesClientDebugController = new ResourcesClientDebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCES_CLIENT)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, resourcesClientDebugController)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, resourcesClientDebugController);
		
		// properties for session
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(SESSION_CATEGORY)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new SessionCategoryDebugController().setOrderIndexAs(-500000))
		.addAdditiveController(CoreConstants.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("ip"));
		
		// session as child of debug; children are resources
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION)
		.addCategory(SESSION_CATEGORY)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new SessionDebugController());
		
		// properties for resource
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(RESOURCE_NODE_INFO_CATEGORY)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ResourceNodeInfoCategoryDebugController().setOrderIndexAs(-500000))
		.addAdditiveController(CoreConstants.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CoreConstants.LAST_UPDATE_TIMESTAMP));
		
		// resource as child of session; children are sessions
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCE_NODE_INFO2)
		.addCategory(RESOURCE_NODE_INFO_CATEGORY)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new ResourceNodeInfo2DebugController());
		
		// session as child of resource
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION2)
		.addCategory(SESSION_CATEGORY);
		
		// resource as child of debug; children are sessions
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCE_NODE_INFO)
		.addCategory(RESOURCE_NODE_INFO_CATEGORY)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new ResourceNodeInfoDebugController());
		
		// session as child of resource
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION3)
		.addCategory(SESSION_CATEGORY);
	}
	
}
