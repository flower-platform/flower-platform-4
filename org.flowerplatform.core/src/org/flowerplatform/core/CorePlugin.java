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
import org.flowerplatform.core.file.FileSystemControllers;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.file.download.remote.DownloadService;
import org.flowerplatform.core.file.upload.remote.UploadService;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.GenericValueDescriptor;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.resource.ResourceDebugControllers;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.resource.ResourceUnsubscriber;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceDAO;
import org.flowerplatform.core.node.update.controller.UpdateAddNodeController;
import org.flowerplatform.core.node.update.controller.UpdatePropertySetterController;
import org.flowerplatform.core.node.update.controller.UpdateRemoveNodeController;
import org.flowerplatform.core.repository.RepositoryChildrenProvider;
import org.flowerplatform.core.repository.RepositoryPropertiesProvider;
import org.flowerplatform.core.repository.RootChildrenProvider;
import org.flowerplatform.core.repository.RootPropertiesProvider;
import org.flowerplatform.core.session.ComposedSessionListener;
import org.flowerplatform.core.session.ISessionListener;
import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class CorePlugin extends AbstractFlowerJavaPlugin {

	protected static CorePlugin INSTANCE;

	public static final String VERSION = "1.0.0.M1_2014-04-01";
	
	protected IFileAccessController fileAccessController = new PlainFileAccessController();
	
	protected ComposedSessionListener composedSessionListener = new ComposedSessionListener();
	
	/**
	 * @author Sebastian Solomon
	 */
	protected RemoteMethodInvocationListener remoteMethodInvocationListener = new RemoteMethodInvocationListener();

	protected ServiceRegistry serviceRegistry = new ServiceRegistry();
	protected TypeDescriptorRegistry nodeTypeDescriptorRegistry = new TypeDescriptorRegistry();
	protected NodeService nodeService = new NodeService(nodeTypeDescriptorRegistry);
	protected ResourceService resourceService;
	protected DownloadService downloadService;
	protected UploadService uploadService;
	
	private ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
	private ScheduledExecutorServiceFactory scheduledExecutorServiceFactory = new ScheduledExecutorServiceFactory();

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

	public ResourceService getResourceService() {
		return resourceService;
	}
	
	public DownloadService getDownloadService() {
		return downloadService;
	}
	
	public UploadService getUploadService() {
		return uploadService;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
			
		System.getProperties().put("flower.version", VERSION);
		
		resourceService = new ResourceService(nodeTypeDescriptorRegistry, new InMemoryResourceDAO());
		downloadService = new DownloadService();
		uploadService = new UploadService();
		
		getServiceRegistry().registerService("nodeService", new NodeServiceRemote());
		getServiceRegistry().registerService("resourceService", new ResourceServiceRemote());
		getServiceRegistry().registerService("coreService", new CoreService());
		getServiceRegistry().registerService("downloadService", downloadService);
		getServiceRegistry().registerService("uploadService", uploadService);
		
		new ResourceUnsubscriber().start();
		
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.ROOT_TYPE)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new RootPropertiesProvider())
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new RootChildrenProvider());

		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.REPOSITORY_TYPE)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new RepositoryPropertiesProvider())
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new RepositoryChildrenProvider());

		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.CODE_TYPE)
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, CoreConstants.CODE_TYPE))
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.IS_SUBSCRIBABLE, true));
		
		getNodeTypeDescriptorRegistry().addDynamicCategoryProvider(new ResourceTypeDynamicCategoryProvider());
				
		getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(UtilConstants.CATEGORY_ALL)
		.addAdditiveController(CoreConstants.ADD_NODE_CONTROLLER, new UpdateAddNodeController())
		.addAdditiveController(CoreConstants.REMOVE_NODE_CONTROLLER, new UpdateRemoveNodeController())
		.addAdditiveController(CoreConstants.PROPERTY_SETTER, new UpdatePropertySetterController())
		.addSingleController(CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(CoreConstants.NAME))
		.addSingleController(CoreConstants.PROPERTY_FOR_SIDE_DESCRIPTOR, new GenericValueDescriptor(CoreConstants.SIDE))
		.addSingleController(CoreConstants.PROPERTY_FOR_ICON_DESCRIPTOR, new GenericValueDescriptor(CoreConstants.ICONS));
		
		new FileSystemControllers().registerControllers();
		new ResourceDebugControllers().registerControllers();
		
		//TODO use Flower property
		boolean isDeleteTempFolderAtStartProperty = true;
		if (isDeleteTempFolderAtStartProperty) {
			FileUtils.deleteDirectory(UtilConstants.TEMP_FOLDER);
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		scheduledExecutorServiceFactory.dispose();
		super.stop(bundleContext);
		INSTANCE = null;
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
	
	public ScheduledExecutorServiceFactory getScheduledExecutorServiceFactory() {
		return scheduledExecutorServiceFactory;
	}
	
	public ComposedSessionListener getComposedSessionListener() {
		return composedSessionListener;
	}

	public void addSessionListener(ISessionListener sessionListener) {
		composedSessionListener.add(sessionListener);
	}
}
