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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.update.InMemoryRootNodeInfoDAO;
import org.flowerplatform.core.node.update.InMemoryUpdateDAO;
import org.flowerplatform.core.node.update.UpdateService;
import org.flowerplatform.core.node.update.controller.UpdateAddNodeController;
import org.flowerplatform.core.node.update.controller.UpdatePropertySetterController;
import org.flowerplatform.core.node.update.controller.UpdateRemoveNodeController;
import org.flowerplatform.core.node.update.remote.UpdateServiceRemote;
import org.flowerplatform.util.controller.AllDynamicCategoryProvider;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.util.servlet.ResourcesServlet;
import org.osgi.framework.BundleContext;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class CorePlugin extends AbstractFlowerJavaPlugin {

	public static final String RESOURCE_KEY = "resource";
	public static final String TYPE_KEY = "type";
	
	protected static CorePlugin INSTANCE;
	
	private ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
	
	public static CorePlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		getServiceRegistry().registerService("nodeServiceInternal", new NodeService(nodeTypeDescriptorRegistry, new InMemoryRootNodeInfoDAO()));
		getServiceRegistry().registerService("nodeService", new NodeServiceRemote());
		
		getServiceRegistry().registerService("updateServiceInternal", new UpdateService(new InMemoryUpdateDAO()));
		getServiceRegistry().registerService("updateService", new UpdateServiceRemote());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().addDynamicCategoryProvider(new ResourceTypeDynamicCategoryProvider());
				
		TypeDescriptor updaterDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(AllDynamicCategoryProvider.CATEGORY_ALL);
		updaterDescriptor.addAdditiveController(AddNodeController.ADD_NODE_CONTROLLER, new UpdateAddNodeController());
		updaterDescriptor.addAdditiveController(RemoveNodeController.REMOVE_NODE_CONTROLLER, new UpdateRemoveNodeController());
		updaterDescriptor.addAdditiveController(PropertySetter.PROPERTY_SETTER, new UpdatePropertySetterController());
			
		setFileAccessController(new PlainFileAccessController());

		setRemoteMethodInvocationListener(new RemoteMethodInvocationListener());
		
		//TODO use Flower property
		boolean isDeleteTempFolderAtStartProperty = true;
		if (isDeleteTempFolderAtStartProperty) {
			FileUtils.deleteDirectory(ResourcesServlet.TEMP_FOLDER);
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	private ServiceRegistry serviceRegistry = new ServiceRegistry();

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	
	private TypeDescriptorRegistry nodeTypeDescriptorRegistry = new TypeDescriptorRegistry();

	public TypeDescriptorRegistry getNodeTypeDescriptorRegistry() {
		return nodeTypeDescriptorRegistry;
	}

	protected IFileAccessController fileAccessController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public IFileAccessController getFileAccessController() {
		return fileAccessController;
	}

	public void setFileAccessController(IFileAccessController fileAccessController) {
		this.fileAccessController = fileAccessController;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// no messages yet
	}

	/**
	 * @author Sebastian Solomon
	 */
	protected RemoteMethodInvocationListener remoteMethodInvocationListener;
	
	/**
	 * @author Sebastian Solomon
	 */
	public RemoteMethodInvocationListener getRemoteMethodInvocationListener() {
		return remoteMethodInvocationListener;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void setRemoteMethodInvocationListener(RemoteMethodInvocationListener remoteMethodInvocationListener) {
		this.remoteMethodInvocationListener = remoteMethodInvocationListener;
	}

	public UpdateService getUpdateService() {
		return (UpdateService) serviceRegistry.getService("updateServiceInternal");
	}
	
	public NodeService getNodeService() {
		return (NodeService) serviceRegistry.getService("nodeServiceInternal");
	}

	/**
	 * Must be called from a try/finally block to make sure that the request is cleared, i.e.
	 * 
	 * <pre>
	 * try {
	 * 	CorePlugin.getInstance().setRequest(request);
	 * 	
	 * 	// specific logic here
	 * 
	 * } finally {
	 * 	CorePlugin.getInstance().clearRequest();
	 * }
	 * </pre> 
	 * 
	 * @see FlowerMessageBrokerServlet
	 * 
	 * @author Mariana Gheorghe
	 */
	public void setRequest(HttpServletRequest request) {
		requests.set(request);
	}
	
	/**
	 * @see #setRequest(HttpServletRequest)
	 * 
	 * @author Mariana Gheorghe
	 */
	public void clearRequest() {
		requests.remove();
	}
	
	public HttpServletRequest getRequest() {
		return requests.get();
	}
	
	public String getSessionId() {
		return getRequest().getSession().getId();
	}
	
}
