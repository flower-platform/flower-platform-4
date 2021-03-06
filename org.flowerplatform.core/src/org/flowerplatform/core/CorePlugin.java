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
package org.flowerplatform.core;

import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_BACKGROUND_COLOR;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_FONT_BOLD;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_FONT_FAMILY;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_FONT_ITALIC;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_FONT_SIZE;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_ICONS;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_MAX_WIDTH;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_MIN_WIDTH;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_TEXT;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_TEXT_COLOR;
import static org.flowerplatform.core.CoreConstants.DEFAULT_LOG_PATH;
import static org.flowerplatform.core.CoreConstants.DEFAULT_PROPERTY_PROVIDER;
import static org.flowerplatform.core.CoreConstants.LOGBACK_CONFIG_FILE;
import static org.flowerplatform.core.CoreConstants.MIND_MAP_RENDERER_CLOUD_COLOR;
import static org.flowerplatform.core.CoreConstants.MIND_MAP_RENDERER_CLOUD_TYPE;
import static org.flowerplatform.core.CoreConstants.MIND_MAP_RENDERER_HAS_CHILDREN;
import static org.flowerplatform.core.CoreConstants.MIND_MAP_RENDERER_SIDE;
import static org.flowerplatform.core.CoreConstants.MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE;
import static org.flowerplatform.core.CoreConstants.REPOSITORY_TYPE;
import static org.flowerplatform.core.CoreConstants.ROOT_TYPE;
import static org.flowerplatform.core.CoreConstants.VIRTUAL_NODE_SCHEME;
import static org.flowerplatform.util.UtilConstants.EXTRA_INFO_VALUE_CONVERTER;
import static org.flowerplatform.util.UtilConstants.VALUE_CONVERTER_CSV_TO_LIST;
import static org.flowerplatform.util.UtilConstants.VALUE_CONVERTER_STRING_HEX_TO_UINT;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.config_processor.ConfigSettingsPropertiesController;
import org.flowerplatform.core.file.FileSystemControllers;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.file.download.remote.DownloadService;
import org.flowerplatform.core.file.upload.remote.UploadService;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.DelegateToResourceController;
import org.flowerplatform.core.node.controller.PropertyDescriptorDefaultPropertyValueProvider;
import org.flowerplatform.core.node.controller.TypeDescriptorRegistryDebugControllers;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.resource.CommandStackChildrenProvider;
import org.flowerplatform.core.node.resource.CommandStackPropertiesProvider;
import org.flowerplatform.core.node.resource.CommandStackResourceHandler;
import org.flowerplatform.core.node.resource.ResourceDebugControllers;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.core.node.resource.ResourceUnsubscriber;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceService;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceSetService;
import org.flowerplatform.core.node.resource.in_memory.InMemorySessionService;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.core.preference.PreferencePropertiesProvider;
import org.flowerplatform.core.preference.PreferencePropertySetter;
import org.flowerplatform.core.preference.remote.PreferencesServiceRemote;
import org.flowerplatform.core.repository.RepositoryChildrenProvider;
import org.flowerplatform.core.repository.RepositoryPropertiesProvider;
import org.flowerplatform.core.repository.RootChildrenProvider;
import org.flowerplatform.core.repository.RootPropertiesProvider;
import org.flowerplatform.core.session.ComposedSessionListener;
import org.flowerplatform.core.session.ISessionListener;
import org.flowerplatform.core.session.SessionService;
import org.flowerplatform.core.users.UserService;
import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.GenericDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.util.servlet.ServletUtils;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class CorePlugin extends AbstractFlowerJavaPlugin {

	protected static CorePlugin instance;

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
	protected ResourceSetService resourceSetService;
	protected SessionService sessionService;
	
	protected VirtualNodeResourceHandler virtualNodeResourceHandler = new VirtualNodeResourceHandler();
	protected CommandStackResourceHandler commandStackResourceHandler = new CommandStackResourceHandler();
		
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
		return instance;
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
	
	public ResourceSetService getResourceSetService() {
		return resourceSetService;
	}
	
	public SessionService getSessionService() {
		return sessionService;
	}
	
	public VirtualNodeResourceHandler getVirtualNodeResourceHandler() {
		return virtualNodeResourceHandler;
	}
	
	public CommandStackResourceHandler getCommandStackResourceHandler() {
		return commandStackResourceHandler;
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

	/**
	 *@author Cristina Constantinescu
	 **/
	public void addSessionListener(ISessionListener sessionListener) {
		composedSessionListener.add(sessionListener);
	}
	
	public FlowerProperties getFlowerProperties() {
		return flowerProperties;
	}

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Brinza
	 */
	public String getCustomResourceUrl(String resource) {
		return CoreConstants.LOAD_FILE_SERVLET + "/" + resource;
	}
	
	/**
	 *@author see class
	 **/
	public CorePlugin() {
		super();

		getFlowerProperties().addProperty(new FlowerProperties
				.AddBooleanProperty(PROP_DELETE_TEMPORARY_DIRECTORY_AT_SERVER_STARTUP, PROP_DEFAULT_DELETE_TEMPORARY_DIRECTORY_AT_SERVER_STARTUP));
		getFlowerProperties().addProperty(new FlowerProperties
				.AddBooleanProperty(ServletUtils.PROP_USE_FILES_FROM_TEMPORARY_DIRECTORY, ServletUtils.PROP_DEFAULT_USE_FILES_FROM_TEMPORARY_DIRECTORY));	
	
		String customLogPath = CoreConstants.FLOWER_PLATFORM_HOME + LOGBACK_CONFIG_FILE; 
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(loggerContext);
			loggerContext.reset();
			if (new File(customLogPath).exists()) {
				configurator.doConfigure(customLogPath);
			} else {
				configurator.doConfigure(this.getClass().getClassLoader().getResourceAsStream(DEFAULT_LOG_PATH));
			}
		} catch (JoranException je) {
			throw new RuntimeException("Error while loading logback config", je);
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
			
		System.getProperties().put("flower.version", CoreConstants.APP_VERSION);
	
		resourceService = new InMemoryResourceService();
		resourceSetService = new InMemoryResourceSetService();
		sessionService = new InMemorySessionService();
		
		getServiceRegistry().registerService("nodeService", new NodeServiceRemote());
		getServiceRegistry().registerService("resourceService", new ResourceServiceRemote());
		getServiceRegistry().registerService("coreService", new CoreService());
		getServiceRegistry().registerService("downloadService", new DownloadService());
		getServiceRegistry().registerService("uploadService", new UploadService());
		getServiceRegistry().registerService("preferenceService", new PreferencesServiceRemote());
		getServiceRegistry().registerService("userService", new UserService());
		
		new ResourceUnsubscriber().start();
		
		getResourceService().addResourceHandler(VIRTUAL_NODE_SCHEME, virtualNodeResourceHandler);
		virtualNodeResourceHandler.addVirtualNodeType(ROOT_TYPE);
		virtualNodeResourceHandler.addVirtualNodeType(REPOSITORY_TYPE);
		
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ROOT_TYPE)
			.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new RootPropertiesProvider())
			.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new RootChildrenProvider());

		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.REPOSITORY_TYPE)
			.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new RepositoryPropertiesProvider())
			.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new RepositoryChildrenProvider());

		UpdateController updateController = new UpdateController();
		DelegateToResourceController delegateToResourceController = new DelegateToResourceController();
		getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(UtilConstants.CATEGORY_ALL)
			.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, delegateToResourceController)
			.addAdditiveController(CoreConstants.PROPERTY_SETTER, delegateToResourceController)
			.addAdditiveController(CoreConstants.DEFAULT_PROPERTY_PROVIDER, delegateToResourceController)
			.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, delegateToResourceController)
			.addSingleController(CoreConstants.PARENT_PROVIDER, delegateToResourceController)
			.addAdditiveController(CoreConstants.ADD_NODE_CONTROLLER, delegateToResourceController)
			.addAdditiveController(CoreConstants.REMOVE_NODE_CONTROLLER, delegateToResourceController)
		
			.addAdditiveController(CoreConstants.PROPERTY_SETTER, updateController)
			.addAdditiveController(CoreConstants.ADD_NODE_CONTROLLER, updateController)
			.addAdditiveController(CoreConstants.REMOVE_NODE_CONTROLLER, updateController)
		
			.addAdditiveController(DEFAULT_PROPERTY_PROVIDER, new PropertyDescriptorDefaultPropertyValueProvider());
		
		
		ConfigSettingsPropertiesController configSettingsPropertiesController = new ConfigSettingsPropertiesController();
		getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CoreConstants.CATEGORY_CONFIG_SETTINGS)
			.addAdditiveController(UtilConstants.FEATURE_PROPERTY_DESCRIPTORS, 
					new PropertyDescriptor().setNameAs(CoreConstants.CONFIG_SETTING_DISABLED)
					.setTypeAs(UtilConstants.PROPERTY_EDITOR_TYPE_BOOLEAN).setContributesToCreationAs(true))
			.addAdditiveController(PROPERTIES_PROVIDER, configSettingsPropertiesController)
			.addAdditiveController(CoreConstants.PROPERTY_SETTER, configSettingsPropertiesController);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CoreConstants.PREFERENCE_CATEGORY_TYPE)
			.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new PreferencePropertiesProvider().setOrderIndexAs(1000)); // after persistence props provider
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.PREFERENCE_TYPE)
			.addCategory(CoreConstants.PREFERENCE_CATEGORY_TYPE)
			.addAdditiveController(CoreConstants.PROPERTY_SETTER, new PreferencePropertySetter())
			// TODO CC: to remove when working at preferences persistence
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(UtilConstants.PROPERTY_EDITOR_TYPE_STRING)
					.setNameAs("value").setPropertyLineRendererAs(PROPERTY_LINE_RENDERER_TYPE_PREFERENCE).setReadOnlyAs(true));
			
		new FileSystemControllers().registerControllers();
		new ResourceDebugControllers().registerControllers();
		new TypeDescriptorRegistryDebugControllers().registerControllers();
		if (Boolean.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(PROP_DELETE_TEMPORARY_DIRECTORY_AT_SERVER_STARTUP))) {
			FileUtils.deleteDirectory(UtilConstants.TEMP_FOLDER);
		}
	
		// Controllers for Command Stack
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.COMMAND_STACK_TYPE)
				.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new CommandStackPropertiesProvider())
				.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new CommandStackChildrenProvider());
		
		getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.COMMAND_TYPE);

		CorePlugin.getInstance().getResourceService().addResourceHandler(CoreConstants.COMMAND_STACK_SCHEME, commandStackResourceHandler);
		
		// define the properties that feed the graphical properties/capabilities of the mind map renderer
		// every node that has one of the properties below will be rendered. Probably the nodes that come from general persistence:
		// we'll be able to modify them as well. The Freeplane plugin overrides them.
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(UtilConstants.CATEGORY_ALL);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_TEXT);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_ICONS)
					.addExtraInfoProperty(EXTRA_INFO_VALUE_CONVERTER, VALUE_CONVERTER_CSV_TO_LIST);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + MIND_MAP_RENDERER_HAS_CHILDREN);
		
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_FONT_FAMILY);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_FONT_SIZE);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_FONT_BOLD);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_FONT_ITALIC);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_TEXT_COLOR)
					.addExtraInfoProperty(EXTRA_INFO_VALUE_CONVERTER, VALUE_CONVERTER_STRING_HEX_TO_UINT);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_BACKGROUND_COLOR)
					.addExtraInfoProperty(EXTRA_INFO_VALUE_CONVERTER, VALUE_CONVERTER_STRING_HEX_TO_UINT);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + MIND_MAP_RENDERER_CLOUD_TYPE);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + MIND_MAP_RENDERER_CLOUD_COLOR)
					.addExtraInfoProperty(EXTRA_INFO_VALUE_CONVERTER, VALUE_CONVERTER_STRING_HEX_TO_UINT);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_MIN_WIDTH);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_MAX_WIDTH);
				addGenericDescriptorWithSimilarNameAsFeature(descriptor, MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + MIND_MAP_RENDERER_SIDE);
	}	
	
	/**
	 * 
	 * @param feature
	 * @return
	 */
	public String getPropertyNameForVisualFeatureSupportedByMindMapRenderer(String feature) {
		if (feature.endsWith(BASE_RENDERER_TEXT)) {
			return CoreConstants.NAME;
		} else if (feature.endsWith(BASE_RENDERER_ICONS)) {
			// in this case it's the same property (i.e. shouldn't put a special case); but we use .ICONS, because it exists already
			return CoreConstants.ICONS;
		} else if (feature.endsWith(MIND_MAP_RENDERER_HAS_CHILDREN)) {
			return CoreConstants.HAS_CHILDREN;
		}
		return Utils.substringFrom(feature, ".");
	}
	
	private GenericDescriptor addGenericDescriptorWithSimilarNameAsFeature(TypeDescriptor descriptor, String  feature) {
		GenericDescriptor d = new GenericDescriptor(getPropertyNameForVisualFeatureSupportedByMindMapRenderer(feature));
		descriptor.addSingleController(feature, d);
		return d;
	}
	
	/**
	 *@author Cristian Spiescu
	 **/
	public void stop(BundleContext bundleContext) throws Exception {
		scheduledExecutorServiceFactory.dispose();
		super.stop(bundleContext);
		instance = null;
	}
}
