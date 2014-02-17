package org.flowerplatform.freeplane;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RawNodeDataProvider.RAW_NODE_DATA_PROVIDER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.RootNodeProvider.ROOT_NODE_PROVIDER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.freeplane.controller.FreeplaneAddNodeController;
import org.flowerplatform.freeplane.controller.FreeplanePropertiesProvider;
import org.flowerplatform.freeplane.controller.FreeplanePropertySetter;
import org.flowerplatform.freeplane.controller.MindMapBasicAddNodeController;
import org.flowerplatform.freeplane.controller.MindMapBasicChildrenProvider;
import org.flowerplatform.freeplane.controller.MindMapBasicRawNodeDataProvider;
import org.flowerplatform.freeplane.controller.MindMapBasicRemoveNodeController;
import org.flowerplatform.freeplane.controller.MindMapPropertiesProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertySetter;
import org.flowerplatform.freeplane.controller.MindMapRootNodeProvider;
import org.flowerplatform.freeplane.remote.FreeplaneService;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class FreeplanePlugin extends AbstractFlowerJavaPlugin {

	public static final String FREEPLANE_NODE_TYPE = "freeplaneNode";
	
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
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FREEPLANE_NODE_TYPE);
		
		TypeDescriptor csTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor("category.persistence-codeSync");
		csTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new FreeplanePropertiesProvider());
		csTypeDescriptor.addAdditiveController(ADD_NODE_CONTROLLER, new FreeplaneAddNodeController());
		csTypeDescriptor.addAdditiveController(PROPERTY_SETTER, new FreeplanePropertySetter());
		csTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("type"));
		csTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("body"));
		addControllers(csTypeDescriptor);
		
		TypeDescriptor mmTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(MindMapPlugin.MINDMAP_CATEGORY);		
		mmTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new MindMapPropertiesProvider());		
		mmTypeDescriptor.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapBasicAddNodeController());
		mmTypeDescriptor.addAdditiveController(PROPERTY_SETTER, new MindMapPropertySetter());
		addControllers(mmTypeDescriptor);
		
		CorePlugin.getInstance().getServiceRegistry().registerService("freeplaneService", new FreeplaneService());
	}
	
	private void addControllers(TypeDescriptor nodeTypeDescriptor) {
		nodeTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, new MindMapBasicChildrenProvider());
		nodeTypeDescriptor.addAdditiveController(ROOT_NODE_PROVIDER, new MindMapRootNodeProvider());
		nodeTypeDescriptor.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapBasicRawNodeDataProvider());
		nodeTypeDescriptor.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapBasicRemoveNodeController());		
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
	
	public void registerMessageBundle() throws Exception {
		// not used
	}
	
}
