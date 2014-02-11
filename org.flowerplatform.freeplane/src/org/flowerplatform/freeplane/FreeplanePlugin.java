package org.flowerplatform.freeplane;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.fileSystem.FileSystemChildrenProvider;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.freeplane.controller.FreeplaneAddNodeController;
import org.flowerplatform.freeplane.controller.FreeplaneChildrenProvider;
import org.flowerplatform.freeplane.controller.FreeplanePropertiesProvider;
import org.flowerplatform.freeplane.controller.FreeplanePropertySetter;
import org.flowerplatform.freeplane.controller.FreeplaneRemoveNodeController;
import org.flowerplatform.freeplane.remote.FreeplaneService;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.util.type_descriptor.TypeDescriptor;
import org.osgi.framework.BundleContext;

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
		
		createNodeTypeDescriptor("category.freeplaneNode");
		TypeDescriptor nodeTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateNodeTypeDescriptor("freeplaneNode");
		nodeTypeDescriptor.addCategory("category.freeplaneNode");
		
		nodeTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateNodeTypeDescriptor("freeplaneRootNode");
		nodeTypeDescriptor.addCategory("category.freeplaneNode");
		nodeTypeDescriptor.addControllerToList(CHILDREN_PROVIDER, new FileSystemChildrenProvider());
		
 		createNodeTypeDescriptor("category.persistence-codeSync");
		
		CorePlugin.getInstance().getServiceRegistry().registerService("freeplaneService", new FreeplaneService());
	}
	
	private TypeDescriptor createNodeTypeDescriptor(String type) {
		TypeDescriptor nodeTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateNodeTypeDescriptor(type);
		nodeTypeDescriptor.addControllerToList(CHILDREN_PROVIDER, new FreeplaneChildrenProvider());
		nodeTypeDescriptor.addControllerToList(PROPERTIES_PROVIDER, new FreeplanePropertiesProvider());
		nodeTypeDescriptor.addControllerToList(ADD_NODE_CONTROLLER, new FreeplaneAddNodeController());
		nodeTypeDescriptor.addControllerToList(REMOVE_NODE_CONTROLLER, new FreeplaneRemoveNodeController());
		nodeTypeDescriptor.addControllerToList(PROPERTY_SETTER, new FreeplanePropertySetter());
		nodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("type"));
		nodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("body"));
		return nodeTypeDescriptor;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
	
	public void registerMessageBundle() throws Exception {
		// not used
	}
	
}
