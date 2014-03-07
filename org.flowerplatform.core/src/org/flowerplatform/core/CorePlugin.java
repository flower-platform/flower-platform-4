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
package org.flowerplatform.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ResourceInfoServiceRemote;
import org.flowerplatform.core.node.resource.ResourceInfoService;
import org.flowerplatform.core.node.root_node.in_memory.InMemoryResourceInfoDAO;
import org.flowerplatform.core.node.root_node.in_memory.InMemoryUpdateDAO;
import org.flowerplatform.core.node.update.UpdateService;
import org.flowerplatform.core.node.update.controller.UpdateAddNodeController;
import org.flowerplatform.core.node.update.controller.UpdatePropertySetterController;
import org.flowerplatform.core.node.update.controller.UpdateRemoveNodeController;
import org.flowerplatform.util.controller.AllDynamicCategoryProvider;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.util.servlet.ResourcesServlet;
import org.osgi.framework.BundleContext;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class CorePlugin extends AbstractFlowerJavaPlugin {

	public static final String RESOURCE_KEY = "resource";
	public static final String TYPE_KEY = "type";
	
	public static final String RESOURCE_TYPE = "resource";
	
	protected static CorePlugin INSTANCE;

	protected IFileAccessController fileAccessController = new PlainFileAccessController();

	/**
	 * @author Sebastian Solomon
	 */
	protected RemoteMethodInvocationListener remoteMethodInvocationListener = new RemoteMethodInvocationListener();

	protected ServiceRegistry serviceRegistry = new ServiceRegistry();
	protected TypeDescriptorRegistry nodeTypeDescriptorRegistry = new TypeDescriptorRegistry();
	protected NodeService nodeService = new NodeService(nodeTypeDescriptorRegistry);
	protected UpdateService updateService = new UpdateService(new InMemoryUpdateDAO());
	protected ResourceInfoService resourceInfoService = new ResourceInfoService(nodeTypeDescriptorRegistry, new InMemoryResourceInfoDAO());

	private ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
	
	public static CorePlugin getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public RemoteMethodInvocationListener getRemoteMethodInvocationListener() {
		return remoteMethodInvocationListener;
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	public IFileAccessController getFileAccessController() {
		return fileAccessController;
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public TypeDescriptorRegistry getNodeTypeDescriptorRegistry() {
		return nodeTypeDescriptorRegistry;
	}

	public NodeService getNodeService() {
		return nodeService;
	}

	public UpdateService getUpdateService() {
		return updateService;
	}

	public ResourceInfoService getResourceInfoService() {
		return resourceInfoService;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		getServiceRegistry().registerService("nodeService", new NodeServiceRemote());
		getServiceRegistry().registerService("resourceInfoService", new ResourceInfoServiceRemote());
		
		getNodeTypeDescriptorRegistry().addDynamicCategoryProvider(new ResourceTypeDynamicCategoryProvider());
				
		getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(AllDynamicCategoryProvider.CATEGORY_ALL)
		.addAdditiveController(AddNodeController.ADD_NODE_CONTROLLER, new UpdateAddNodeController())
		.addAdditiveController(RemoveNodeController.REMOVE_NODE_CONTROLLER, new UpdateRemoveNodeController())
		.addAdditiveController(PropertySetter.PROPERTY_SETTER, new UpdatePropertySetterController());
		
		registerDebugControllers();
		
		//TODO use Flower property
		boolean isDeleteTempFolderAtStartProperty = true;
		if (isDeleteTempFolderAtStartProperty) {
			FileUtils.deleteDirectory(ResourcesServlet.TEMP_FOLDER);
		}
	}

	private void registerDebugControllers() {
		final int[] idx = new int[1];
		final String DEBUG = "_debug";
		final String SESSION = "_debugSession";
		final String ROOT_NODE_INFO = "_debugRootNodeInfo";
		
		
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(DEBUG)
		.addAdditiveController(ChildrenProvider.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, Map<String, Object> options) {
				options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, Map<String, Object> options) {
				options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
				List<Node> children = new ArrayList<Node>();
				for (String sessionId : getResourceInfoService().getSubscribedSessions()) {
					Node session = new Node(SESSION, node.getResource(), sessionId + " " + (++idx[0]), null);
					children.add(session);
				}
				return children;
			}
		});
		
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(SESSION)
		.addAdditiveController(ChildrenProvider.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, Map<String, Object> options) {
				options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, Map<String, Object> options) {
				options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
				List<Node> children = new ArrayList<Node>();
				String sessionId = node.getIdWithinResource().split(" ")[0];
				for (String resourceId : getResourceInfoService().getResourcesSubscribedBySession(sessionId)) {
					Node resource = new Node(ROOT_NODE_INFO, node.getResource(), resourceId.replace("|", "+") + " " + (++idx[0]), null);
					children.add(resource);
				}
				return children;
			}
		})
		.addAdditiveController(PropertiesProvider.PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, Map<String, Object> options) {
				String sessionId = node.getIdWithinResource().split(" ")[0];
				node.getProperties().put(NodePropertiesConstants.TEXT, "Session " + sessionId);
				node.getProperties().put("ip", getResourceInfoService().getSessionProperty(sessionId, "ip"));
				options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
			}
		}.setOrderIndexAs(-500000))
		.addAdditiveController(PropertyDescriptor.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("ip"));
		
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ROOT_NODE_INFO)
		.addAdditiveController(ChildrenProvider.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, Map<String, Object> options) {
				options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, Map<String, Object> options) {
				options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
				
				List<Node> children = new ArrayList<Node>();
				String resourceId = node.getIdWithinResource().replace("+", "|").split(" ")[0];
				for (String sessionId : getResourceInfoService().getSessionsSubscribedToResource(resourceId)) {
					Node session = new Node(SESSION, node.getResource(), sessionId + " " + (++idx[0]), null);
					children.add(session);
				}
				return children;
			}
		})
		.addAdditiveController(PropertiesProvider.PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, Map<String, Object> options) {
				String resourceId = node.getIdWithinResource().replace("+", "|").split(" ")[0];
				node.getProperties().put(NodePropertiesConstants.TEXT, "Resource " + resourceId);
				// TODO
				options.put(NodeService.STOP_CONTROLLER_INVOCATION, true);
			}
		}.setOrderIndexAs(-500000));
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// no messages yet
	}
	
	/**
	 * Setting/removing must be done from a try/finally block to make sure that 
	 * the request is cleared, i.e.
	 * 
	 * <pre>
	 * try {
	 * 	CorePlugin.getInstance().getRequestThreadLocal().set(request);
	 * 	
	 * 	// specific logic here
	 * 
	 * } finally {
	 * 	CorePlugin.getInstance().getRequestThreadLocal().remove()
	 * }
	 * </pre> 
	 * 
	 * @see FlowerMessageBrokerServlet
	 */
	public ThreadLocal<HttpServletRequest> getRequestThreadLocal() {
		return requestThreadLocal;
	}
	
}
