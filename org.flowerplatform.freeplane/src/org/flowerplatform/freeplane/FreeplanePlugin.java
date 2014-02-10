package org.flowerplatform.freeplane;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.node.controller.ParentProvider.PARENT_PROVIDER;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.freeplane.controller.FreeplaneAddNodeController;
import org.flowerplatform.freeplane.controller.FreeplaneChildrenProvider;
import org.flowerplatform.freeplane.controller.FreeplaneParentProvider;
import org.flowerplatform.freeplane.controller.FreeplanePropertiesProvider;
import org.flowerplatform.freeplane.controller.FreeplanePropertySetter;
import org.flowerplatform.freeplane.controller.FreeplaneRemoveNodeController;
import org.flowerplatform.freeplane.remote.FreeplaneService;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

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
		
		addControllers(CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FREEPLANE_NODE_TYPE));
		addControllers(CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor("category.persistence-codeSync"));
		
		CorePlugin.getInstance().getServiceRegistry().registerService("freeplaneService", new FreeplaneService());
	}
	
	private void addControllers(TypeDescriptor nodeTypeDescriptor) {
		nodeTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, new FreeplaneChildrenProvider());
		nodeTypeDescriptor.addSingleController(PARENT_PROVIDER, new FreeplaneParentProvider());
		nodeTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new FreeplanePropertiesProvider());
		nodeTypeDescriptor.addAdditiveController(ADD_NODE_CONTROLLER, new FreeplaneAddNodeController());
		nodeTypeDescriptor.addAdditiveController(REMOVE_NODE_CONTROLLER, new FreeplaneRemoveNodeController());
		nodeTypeDescriptor.addAdditiveController(PROPERTY_SETTER, new FreeplanePropertySetter());
		nodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("type"));
		nodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("body"));
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
	
	public void registerMessageBundle() throws Exception {
		// not used
	}
	
}
