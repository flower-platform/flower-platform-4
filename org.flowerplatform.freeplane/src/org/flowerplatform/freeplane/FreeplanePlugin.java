package org.flowerplatform.freeplane;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.ParentProvider.PARENT_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RawNodeDataProvider.RAW_NODE_DATA_PROVIDER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.RootNodeProvider.ROOT_NODE_PROVIDER;
import static org.flowerplatform.core.node.resource.ResourceSubscriptionListener.RESOURCE_SUBSCRIPTION_LISTENER;
import static org.flowerplatform.mindmap.MindMapPlugin.FREEPLANE_MINDMAP_CATEGORY;
import static org.flowerplatform.mindmap.MindMapPlugin.FREEPLANE_PERSISTENCE_CATEGORY;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.freeplane.controller.FreeplaneResourceSubscriptionListener;
import org.flowerplatform.freeplane.controller.MindMapAddNodeController;
import org.flowerplatform.freeplane.controller.MindMapChildrenProvider;
import org.flowerplatform.freeplane.controller.MindMapParentProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertiesProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertySetter;
import org.flowerplatform.freeplane.controller.MindMapRawNodeDataProvider;
import org.flowerplatform.freeplane.controller.MindMapRemoveNodeController;
import org.flowerplatform.freeplane.controller.MindMapRootNodeProvider;
import org.flowerplatform.freeplane.controller.PersistenceAddNodeProvider;
import org.flowerplatform.freeplane.controller.PersistencePropertiesProvider;
import org.flowerplatform.freeplane.controller.PersistencePropertySetter;
import org.flowerplatform.freeplane.remote.FreeplaneService;
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
	
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_MINDMAP_CATEGORY)		
		.addAdditiveController(PROPERTIES_PROVIDER, new MindMapPropertiesProvider())		
		.addAdditiveController(PROPERTY_SETTER, new MindMapPropertySetter())
		.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
		.addSingleController(ROOT_NODE_PROVIDER, new MindMapRootNodeProvider())
		.addAdditiveController(RESOURCE_SUBSCRIPTION_LISTENER, new FreeplaneResourceSubscriptionListener())
		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapAddNodeController())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_PERSISTENCE_CATEGORY)
		.addAdditiveController(PROPERTIES_PROVIDER, new PersistencePropertiesProvider())		
		.addAdditiveController(PROPERTY_SETTER, new PersistencePropertySetter())
		.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
		.addSingleController(ROOT_NODE_PROVIDER, new MindMapRootNodeProvider())
		.addAdditiveController(RESOURCE_SUBSCRIPTION_LISTENER, new FreeplaneResourceSubscriptionListener())
		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new PersistenceAddNodeProvider())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CorePlugin.RESOURCE_TYPE)
		.addAdditiveController(RESOURCE_SUBSCRIPTION_LISTENER, new FreeplaneResourceSubscriptionListener())
		.addSingleController(ROOT_NODE_PROVIDER, new MindMapRootNodeProvider())
		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider());
		
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
