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

import static org.flowerplatform.core.CoreConstants.DEFAULT_PROPERTY_PROVIDER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.flowerplatform.core.file.FileSystemControllers;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.file.download.remote.DownloadService;
import org.flowerplatform.core.file.upload.remote.UploadService;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.controller.PropertyDescriptorDefaultPropertyValueProvider;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.controller.TypeDescriptorRegistryDebugControllers;
import org.flowerplatform.core.node.remote.GenericValueDescriptor;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceDebugControllers;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.resource.ResourceUnsubscriber;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceDAO;
import org.flowerplatform.core.node.update.Command;
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
import org.flowerplatform.util.servlet.ServletUtils;
import org.osgi.framework.BundleContext;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class CorePlugin extends AbstractFlowerJavaPlugin {

	protected static CorePlugin INSTANCE;

	protected static final String PROP_DELETE_TEMPORARY_DIRECTORY_AT_SERVER_STARTUP = "deleteTemporaryDirectoryAtServerStartup"; 
	protected static final String PROP_DEFAULT_DELETE_TEMPORARY_DIRECTORY_AT_SERVER_STARTUP = "true"; 
		
	protected IFileAccessController fileAccessController = new PlainFileAccessController();
	
	protected ComposedSessionListener composedSessionListener = new ComposedSessionListener();
	
	private FlowerProperties flowerProperties = new FlowerProperties();
	
	/**
	 * @author Sebastian Solomon
	 */
	protected RemoteMethodInvocationListener remoteMethodInvocationListener = new RemoteMethodInvocationListener();

	protected ServiceRegistry serviceRegistry = new ServiceRegistry();
	protected TypeDescriptorRegistry nodeTypeDescriptorRegistry = new TypeDescriptorRegistry();
	protected NodeService nodeService = new NodeService(nodeTypeDescriptorRegistry);
	protected ResourceService resourceService;
		
	private ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
	private ScheduledExecutorServiceFactory scheduledExecutorServiceFactory = new ScheduledExecutorServiceFactory();

	/**
	 * @author Claudiu Matei
	 */
	private ThreadLocal<ContextThreadLocal> contextThreadLocal = new ThreadLocal<ContextThreadLocal>();

	/**
	 * @author Claudiu Matei
	 */
	private ILockManager lockManager = new InMemoryLockManager(); 
	
	public static CorePlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// messages come from .resources
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
	
	/**
	 * @author Claudiu Matei
	 */
	public ThreadLocal<ContextThreadLocal> getContextThreadLocal() {
		return contextThreadLocal;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	public ILockManager getLockManager() {
		return lockManager;
	}

	/**
	 * @author Claudiu Matei
	 */
	public void setLockManager(ILockManager lockManager) {
		this.lockManager = lockManager;
	}

	public ComposedSessionListener getComposedSessionListener() {
		return composedSessionListener;
	}

	public void addSessionListener(ISessionListener sessionListener) {
		composedSessionListener.add(sessionListener);
	}
	
	public FlowerProperties getFlowerProperties() {
		return flowerProperties;
	}

	/**
	 * @return workspace location from OSGI property
	 * @author Cristina Constantinescu
	 */
	@SuppressWarnings("restriction")
	public String getWorkspaceLocation() {
		String location = FrameworkProperties.getProperty("osgi.instance.area");
		
		// if property value starts with "file:/", remove it
		if (location.startsWith("file:/")) {
			location = location.substring("file:/".length());
		}
		return location;
	}
	
	public CorePlugin() {
		super();
			    
		getFlowerProperties().addProperty(new FlowerProperties.AddBooleanProperty(PROP_DELETE_TEMPORARY_DIRECTORY_AT_SERVER_STARTUP, PROP_DEFAULT_DELETE_TEMPORARY_DIRECTORY_AT_SERVER_STARTUP));
		getFlowerProperties().addProperty(new FlowerProperties.AddBooleanProperty(ServletUtils.PROP_USE_FILES_FROM_TEMPORARY_DIRECTORY, ServletUtils.PROP_DEFAULT_USE_FILES_FROM_TEMPORARY_DIRECTORY));	
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
			
		System.getProperties().put("flower.version", CoreConstants.APP_VERSION);
	
		resourceService = new ResourceService(nodeTypeDescriptorRegistry, new InMemoryResourceDAO());
		
		getServiceRegistry().registerService("nodeService", new NodeServiceRemote());
		getServiceRegistry().registerService("resourceService", new ResourceServiceRemote());
		getServiceRegistry().registerService("coreService", new CoreService());
		getServiceRegistry().registerService("downloadService", new DownloadService());
		getServiceRegistry().registerService("uploadService", new UploadService());
		
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
		.addAdditiveController(DEFAULT_PROPERTY_PROVIDER, new PropertyDescriptorDefaultPropertyValueProvider())
		.addAdditiveController(CoreConstants.REMOVE_NODE_CONTROLLER, new UpdateRemoveNodeController())
		.addAdditiveController(CoreConstants.PROPERTY_SETTER, new UpdatePropertySetterController())
		.addSingleController(CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(CoreConstants.NAME))
		.addSingleController(CoreConstants.PROPERTY_FOR_SIDE_DESCRIPTOR, new GenericValueDescriptor(CoreConstants.SIDE))
		.addSingleController(CoreConstants.PROPERTY_FOR_ICON_DESCRIPTOR, new GenericValueDescriptor(CoreConstants.ICONS));
		
		new FileSystemControllers().registerControllers();
		new ResourceDebugControllers().registerControllers();
		new TypeDescriptorRegistryDebugControllers().registerControllers();
		
		if (Boolean.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(PROP_DELETE_TEMPORARY_DIRECTORY_AT_SERVER_STARTUP))) {
			FileUtils.deleteDirectory(UtilConstants.TEMP_FOLDER);
		}
		
		// Controllers for Command Stack
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor("commandStackDummyRoot")
				.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, "commandStackDummyRoot"))
				.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new ChildrenProvider() {

					@Override
					public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
						return true;
					}

					@Override
					public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
						return Collections.singletonList(new Node(CoreConstants.COMMAND_STACK_TYPE, "self", node.getIdWithinResource(), null));
					}
				});
		
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.COMMAND_STACK_TYPE)
				.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, CoreConstants.COMMAND_STACK_TYPE))
				.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.IS_SUBSCRIBABLE, true))
				.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new ChildrenProvider() {

					@Override
					public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
//						List<Command> commands = CorePlugin.getInstance().getResourceService().getCommands(RemoteMethodInvocationListener.unescapeFullNodeId(node.getIdWithinResource()));
//						return !commands.isEmpty();
						return true;
					}

					@Override
					public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
						List<Command> commands = CorePlugin.getInstance().getResourceService().getCommands(RemoteMethodInvocationListener.unescapeFullNodeId(node.getIdWithinResource()));
						ArrayList<Node> children = new ArrayList<>();
						for (Command command : commands) {
							Node childNode = new Node(CoreConstants.COMMAND_TYPE, node.getFullNodeId(), command.getId(), null);
							childNode.getProperties().put(CoreConstants.NAME, command.getTitle());
							children.add(childNode);
						}
						return children;
					}
				});
		
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.COMMAND_TYPE);
		
		TempDeleteAfterGH279AndCo.INSTANCE.init();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		scheduledExecutorServiceFactory.dispose();
		super.stop(bundleContext);
		INSTANCE = null;
	}

}
