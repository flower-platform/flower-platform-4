package org.flowerplatform.freeplane;

import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.DEFAULT_PROPERTY_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PARENT_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_CATEGORY;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileExtensionSetting;
import org.flowerplatform.core.node.controller.DefaultPropertiesProvider;
import org.flowerplatform.freeplane.controller.MindMapAddNodeController;
import org.flowerplatform.freeplane.controller.MindMapChildrenProvider;
import org.flowerplatform.freeplane.controller.MindMapDefaultPropertyValueProvider;
import org.flowerplatform.freeplane.controller.MindMapParentProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertiesProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertySetter;
import org.flowerplatform.freeplane.controller.MindMapRemoveNodeController;
import org.flowerplatform.freeplane.controller.PersistenceAddNodeProvider;
import org.flowerplatform.freeplane.controller.PersistencePropertiesProvider;
import org.flowerplatform.freeplane.controller.PersistencePropertySetter;
import org.flowerplatform.freeplane.remote.MindMapServiceRemote;
import org.flowerplatform.freeplane.resource.FreeplanePersistenceResourceHandler;
import org.flowerplatform.freeplane.style.controller.MindMapStyleChildrenProvider;
import org.flowerplatform.freeplane.style.controller.MindMapStyleResourceHandler;
import org.flowerplatform.freeplane.style.controller.StyleRootChildrenProvider;
import org.flowerplatform.freeplane.style.controller.StyleRootPropertiesProvider;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.freeplane.main.headlessmode.HeadlessMModeControllerFactory;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class FreeplanePlugin extends AbstractFlowerJavaPlugin {

	protected static FreeplanePlugin INSTANCE;
	
	public static final String STYLE_ROOT_NODE = "styleRootNode";
	
	public static final String MIND_MAP_STYLE = "mindMapStyle";
	
	static {
		// configure Freeplane starter
		new FreeplaneHeadlessStarter().createController().setMapViewManager(new HeadlessMapViewController());		
		HeadlessMModeControllerFactory.createModeController();	
	}
	
	public static FreeplanePlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
	
		CorePlugin.getInstance().addFileExtensionSetting("mm", new FileExtensionSetting("fpp", MindMapConstants.MINDMAP_CONTENT_TYPE), true);
		CorePlugin.getInstance().getResourceService().addResourceHandler("fpp", new FreeplanePersistenceResourceHandler());
		CorePlugin.getInstance().getResourceService().addResourceHandler(MIND_MAP_STYLE, new MindMapStyleResourceHandler());
		
//		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_MINDMAP_CATEGORY)
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor("category.resource.fpp")
		.addAdditiveController(PROPERTIES_PROVIDER, new MindMapPropertiesProvider())
		.addAdditiveController(DEFAULT_PROPERTY_PROVIDER, new MindMapDefaultPropertyValueProvider())
		.addAdditiveController(PROPERTY_SETTER, new MindMapPropertySetter())
		.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
//		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapAddNodeController())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController())
		.addAdditiveController(CHILDREN_PROVIDER, new StyleRootChildrenProvider())
		.addAdditiveController(PROPERTIES_PROVIDER, new DefaultPropertiesProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_PERSISTENCE_CATEGORY)
		.addAdditiveController(PROPERTIES_PROVIDER, new PersistencePropertiesProvider())		
		.addAdditiveController(PROPERTY_SETTER, new PersistencePropertySetter())
		.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
//		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new PersistenceAddNodeProvider())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STYLE_ROOT_NODE)
		.addAdditiveController(PROPERTIES_PROVIDER, new StyleRootPropertiesProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapStyleChildrenProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor("category.resource.mindMapStyle")
		.addAdditiveController(PROPERTIES_PROVIDER, new MindMapPropertiesProvider())
		.addAdditiveController(DEFAULT_PROPERTY_PROVIDER, new MindMapDefaultPropertyValueProvider());
		
		CorePlugin.getInstance().getServiceRegistry().registerService("mindmapService", new MindMapServiceRemote());		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// not used
	}	
	
}
