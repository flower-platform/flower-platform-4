package org.flowerplatform.freeplane;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.ParentProvider.PARENT_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RawNodeDataProvider.RAW_NODE_DATA_PROVIDER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.RootNodeProvider.ROOT_NODE_PROVIDER;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.freeplane.controller.MindMapBasicAddNodeController;
import org.flowerplatform.freeplane.controller.MindMapBasicChildrenProvider;
import org.flowerplatform.freeplane.controller.MindMapBasicParentProvider;
import org.flowerplatform.freeplane.controller.MindMapBasicPropertiesProvider;
import org.flowerplatform.freeplane.controller.MindMapBasicPropertySetter;
import org.flowerplatform.freeplane.controller.MindMapBasicRawNodeDataProvider;
import org.flowerplatform.freeplane.controller.MindMapBasicRemoveNodeController;
import org.flowerplatform.freeplane.controller.MindMapBasicRootNodeProvider;
import org.flowerplatform.freeplane.remote.FreeplaneService;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class FreeplanePlugin extends AbstractFlowerJavaPlugin {

	protected static FreeplanePlugin INSTANCE;
	
	public static FreeplanePlugin getInstance() {
		return INSTANCE;
	}
	
	private FreeplaneUtils freeplaneUtils = new FreeplaneUtils();

	public FreeplaneUtils getFreeplaneUtils() {
		return freeplaneUtils;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
	
		TypeDescriptor mmTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(ResourceTypeDynamicCategoryProvider.CATEGORY_RESOURCE_PREFIX + "mm");		
		mmTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new MindMapBasicPropertiesProvider());		
		mmTypeDescriptor.addAdditiveController(PROPERTY_SETTER, new MindMapBasicPropertySetter());
		mmTypeDescriptor.addSingleController(PARENT_PROVIDER, new MindMapBasicParentProvider());
		mmTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, new MindMapBasicChildrenProvider());
		mmTypeDescriptor.addSingleController(ROOT_NODE_PROVIDER, new MindMapBasicRootNodeProvider());
		mmTypeDescriptor.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapBasicRawNodeDataProvider());
		mmTypeDescriptor.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapBasicAddNodeController());
		mmTypeDescriptor.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapBasicRemoveNodeController());
		
		CorePlugin.getInstance().getServiceRegistry().registerService("freeplaneService", new FreeplaneService());
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