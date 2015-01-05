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
package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CoreConstants.DEBUG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.DebugControllers;
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
public class ResourceDebugControllers extends DebugControllers {

	private static final String RESOURCES_SERVER = DEBUG + "ServerResources";
	
	private static final String RESOURCES_CLIENT = DEBUG + "ClientResources";
	
	private static final String SESSIONS = DEBUG + "Sessions";
	
	private static final String SESSION = DEBUG + "Session";
	private static final String RESOURCE2 = DEBUG + "Resource2";
	
	private static final String RESOURCE_SET = DEBUG + "ResourceSet";
	private static final String RESOURCE = DEBUG + "Resource";
	private static final String SESSION2 = DEBUG + "Session2";
	
	private static final String SESSION_CATEGORY = UtilConstants.CATEGORY_PREFIX + SESSION;
	private static final String RESOURCE_CATEGORY = UtilConstants.CATEGORY_PREFIX + RESOURCE;
	
	/**
	 *@author see class
	 **/
	class RootDebugController extends AbstractController implements IChildrenProvider {
		
		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Collections.singletonList(createVirtualNode(DEBUG, null));
		}
	}
	
	/**
	 *@author see class
	 **/
	class DebugController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Arrays.asList(
					createVirtualNode(RESOURCES_SERVER, null),
					createVirtualNode(SESSIONS, null),
					createVirtualNode(RESOURCES_CLIENT, null));
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, DEBUG);
		}
	}
	
	/**
	 *@author see class
	 **/
	class SessionsDebugController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			List<Node> children = new ArrayList<Node>();
			for (String sessionId : CorePlugin.getInstance().getSessionService().getSubscribedSessions()) {
				children.add(createVirtualNode(SESSION, sessionId));
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
	
	/**
	 *@author see class
	 **/
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
	
	/**
	 *@author see class
	 **/
	class ResourcesDebugController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			List<Node> children = new ArrayList<Node>();
			for (String resourceId : CorePlugin.getInstance().getResourceSetService().getResourceSets()) {
				children.add(createVirtualNode(RESOURCE_SET, resourceId));
			}
			return children;
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, "Server Resources");
		}
	}
	
	/**
	 *@author see class
	 **/
	class SessionCategoryDebugController extends AbstractController implements IPropertiesProvider {
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			String sessionId = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			node.getProperties().put(CoreConstants.NAME, "Session: " + sessionId);
			node.getProperties().put("ip", CorePlugin.getInstance().getSessionService().getSessionProperty(sessionId, "ip"));
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		}
	}
	
	/**
	 *@author see class
	 **/
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
			String sessionId = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			for (String resourceId : CorePlugin.getInstance().getSessionService().getResourcesSubscribedBySession(sessionId)) {
				children.add(createVirtualNode(RESOURCE2, resourceId));
			}
			return children;
		}
	}
	
	/**
	 *@author see class
	 **/
	class ResourceDebugController extends AbstractController implements IChildrenProvider {

		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			List<Node> children = new ArrayList<Node>();
			String resourceUri = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			for (String sessionId : CorePlugin.getInstance().getResourceService().getSessionsSubscribedToResource(resourceUri)) {
				Node session = createVirtualNode(SESSION2, sessionId + "#" + resourceUri);
				children.add(session);
			}
			return children;
		}

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
	}
	
	/**
	 *@author see class
	 **/
	class ResourceCategoryDebugController extends AbstractController implements IPropertiesProvider {
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			String resourceId = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			node.getProperties().put(CoreConstants.NAME, "Resource: " + resourceId);
			long timestamp = CorePlugin.getInstance().getResourceService().getUpdateRequestedTimestamp(resourceId);
			node.getProperties().put(CoreConstants.LAST_UPDATE_TIMESTAMP, timestamp);
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		}
	}
	
	/**
	 *@author see class
	 **/
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
			String resourceSet = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			for (String resourceUri : CorePlugin.getInstance().getResourceSetService().getResourceUris(resourceSet)) {
				children.add(createVirtualNode(RESOURCE, resourceUri));
			}
			return children;
		}

		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(CoreConstants.NAME, "ResourceSet: "
					+ getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri()));
		}
	}
	
	/**
	 *@author see class
	 **/
	public void registerControllers() {
		
		addVirtualDebugType(DEBUG);
		addVirtualDebugType(RESOURCES_SERVER);
		addVirtualDebugType(RESOURCES_CLIENT);
		addVirtualDebugType(SESSIONS);
		addVirtualDebugType(SESSION);
		addVirtualDebugType(RESOURCE2);
		addVirtualDebugType(RESOURCE_SET);
		addVirtualDebugType(RESOURCE);
		addVirtualDebugType(SESSION2);
		
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
		.addAdditiveController(UtilConstants.FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs("ip"));
		
		// session as child of debug; children are resources
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION)
		.addCategory(SESSION_CATEGORY)
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new SessionDebugController());
		
		// properties for resource
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(RESOURCE_CATEGORY)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ResourceCategoryDebugController().setOrderIndexAs(-500000))
		.addAdditiveController(UtilConstants.FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(CoreConstants.LAST_UPDATE_TIMESTAMP));
		
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
