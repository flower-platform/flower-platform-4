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
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class ResourceDebugControllers {

	private final String RESOURCES_SERVER = DEBUG + "ServerResources";
	
	private final String RESOURCES_CLIENT = DEBUG + "ClientResources";
	
	private final String SESSIONS = DEBUG + "Sessions";
	
	private final String SESSION = DEBUG + "Session";
	private final String RESOURCE2 = DEBUG + "Resource2";
	
	private final String RESOURCE_SET = DEBUG + "ResourceSet";
	private final String RESOURCE = DEBUG + "Resource";
	private final String SESSION2 = DEBUG + "Session2";
	
	private final String SESSION_CATEGORY = UtilConstants.CATEGORY_PREFIX + SESSION;
	private final String RESOURCE_CATEGORY = UtilConstants.CATEGORY_PREFIX + RESOURCE;
	
	class RootDebugController extends AbstractController implements IChildrenProvider {
		
		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Collections.singletonList(new Node(Utils.getUri(DEBUG, DEBUG), DEBUG));
		}
	}
	
	class DebugController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Arrays.asList(
					new Node(Utils.getUri(RESOURCES_SERVER, DEBUG), RESOURCES_SERVER), 
					new Node(Utils.getUri(SESSIONS, DEBUG), SESSIONS), 
					new Node(Utils.getUri(RESOURCES_CLIENT, DEBUG), RESOURCES_CLIENT));
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, DEBUG);
		}
	}
	
	class SessionsDebugController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			List<Node> children = new ArrayList<Node>();
			for (String sessionId : CorePlugin.getInstance().getSessionService().getSubscribedSessions()) {
				Node session = new Node(Utils.getUri(SESSION, Utils.getSchemeSpecificPart(node.getNodeUri()), sessionId), SESSION);
				children.add(session);
			}
			return children;
		}

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}

		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, "Sessions");
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
			for (String resourceId : CorePlugin.getInstance().getResourceSetService().getResourceSets()) {
				Node resource = new Node(Utils.getUri(RESOURCE_SET, Utils.getSchemeSpecificPart(node.getNodeUri()), resourceId), RESOURCE_SET);
				children.add(resource);
			}
			return children;
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, "Server Resources");
		}
	}
	
	class SessionCategoryDebugController extends AbstractController implements IPropertiesProvider {
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			String sessionId = Utils.getFragment(node.getNodeUri());
			node.getProperties().put(CoreConstants.NAME, "Session: " + sessionId);
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
			String sessionId = Utils.getFragment(node.getNodeUri());
			for (String resourceId : CorePlugin.getInstance().getSessionService().getResourcesSubscribedBySession(sessionId)) {
				Node resource = new Node(Utils.getUri(RESOURCE2, Utils.getFragment(node.getNodeUri()), resourceId), RESOURCE2);
				children.add(resource);
			}
			return children;
		}
	}
	
	class ResourceDebugController extends AbstractController implements IChildrenProvider {

		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			List<Node> children = new ArrayList<Node>();
			String resourceUri = Utils.getFragment(node.getNodeUri());
			for (String sessionId : CorePlugin.getInstance().getResourceService().getSessionsSubscribedToResource(resourceUri)) {
				Node session = new Node(Utils.getUri(SESSION2, Utils.getFragment(node.getNodeUri()), sessionId), SESSION2);
				children.add(session);
			}
			return children;
		}

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
	}
	
	class ResourceCategoryDebugController extends AbstractController implements IPropertiesProvider {
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			String resourceId = Utils.getFragment(node.getNodeUri());
			node.getProperties().put(CoreConstants.NAME, "Resource: " + resourceId);
			long timestamp = CorePlugin.getInstance().getResourceService().getUpdateRequestedTimestamp(resourceId);
			node.getProperties().put(CoreConstants.LAST_UPDATE_TIMESTAMP, timestamp);
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		}
	}
	
	class ResourceSetDebugController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
			
			List<Node> children = new ArrayList<Node>();
			String resourceSet = Utils.getFragment(node.getNodeUri());
			for (String resourceUri : CorePlugin.getInstance().getResourceSetService().getResourceUris(resourceSet)) {
				Node resource = new Node(Utils.getUri(RESOURCE, Utils.getSchemeSpecificPart(node.getNodeUri()), resourceUri), RESOURCE);
				children.add(resource); 
			}
			return children;
		}

		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, "ResourceSet: " + Utils.getFragment(node.getNodeUri()));
		}
	}
	
	private void addResourceHandler(String type) {
		CorePlugin.getInstance().getResourceService().addResourceHandler(type, new BaseResourceHandler(type));
	}
	
	public void registerControllers() {
		
		addResourceHandler(DEBUG);
		addResourceHandler(RESOURCES_SERVER);
		addResourceHandler(RESOURCES_CLIENT);
		addResourceHandler(SESSIONS);
		addResourceHandler(SESSION);
		addResourceHandler(RESOURCE2);
		addResourceHandler(RESOURCE_SET);
		addResourceHandler(RESOURCE);
		addResourceHandler(SESSION2);
		
		// add debug node to root
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.ROOT_TYPE)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new RootDebugController().setOrderIndexAs(10000));
		
		DebugController debugController = new DebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(DEBUG)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, debugController)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, debugController);
		
		// debug node
		
		ResourcesDebugController resourcesDebugController = new ResourcesDebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCES_SERVER)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, resourcesDebugController)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, resourcesDebugController);
		
		SessionsDebugController sessionsDebugController = new SessionsDebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSIONS)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, sessionsDebugController)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, sessionsDebugController);
		
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
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(RESOURCE_CATEGORY)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ResourceCategoryDebugController().setOrderIndexAs(-500000))
		.addAdditiveController(CoreConstants.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CoreConstants.LAST_UPDATE_TIMESTAMP));
		
		// resource as child of session; children are sessions
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCE2)
		.addCategory(RESOURCE_CATEGORY);
		
		// resourceSet as child of debug; children are sessions
		ResourceSetDebugController resourceSetDebugController = new ResourceSetDebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCE_SET)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, resourceSetDebugController)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, resourceSetDebugController);
		
		// resource as child of resourceSet; children are sessions
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(RESOURCE)
		.addCategory(RESOURCE_CATEGORY)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new ResourceDebugController());
		
		// session as child of resource
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION2)
		.addCategory(SESSION_CATEGORY);
	}
	
}
