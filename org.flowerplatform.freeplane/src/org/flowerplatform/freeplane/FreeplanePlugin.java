package org.flowerplatform.freeplane;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.ParentProvider.PARENT_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RawNodeDataProvider.RAW_NODE_DATA_PROVIDER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.RootNodeProvider.ROOT_NODE_PROVIDER;
import static org.flowerplatform.core.node.controller.StylePropertyProvider.STYLE_PROPERTY_PROVIDER;
import static org.flowerplatform.mindmap.MindMapPlugin.FREEPLANE_MINDMAP_CATEGORY;
import static org.flowerplatform.mindmap.MindMapPlugin.FREEPLANE_PERSISTENCE_CATEGORY;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
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
import org.flowerplatform.freeplane.style.controller.MindMapStyleChildrenProvider;
import org.flowerplatform.freeplane.style.controller.MindMapStylePropertiesProvider;
import org.flowerplatform.freeplane.style.controller.MindMapStyleRootProvider;
import org.flowerplatform.freeplane.style.controller.MindMapStyleStylePropertyProvider;
import org.flowerplatform.freeplane.style.controller.StyleChildrenProvider;
import org.flowerplatform.freeplane.style.controller.StylePropertiesProvider;
import org.flowerplatform.freeplane.style.controller.StylePropertySetter;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class FreeplanePlugin extends AbstractFlowerJavaPlugin {

	protected static FreeplanePlugin INSTANCE;
	
	public static final String STYLE_ROOT_NODE = "styleRootNode";
	
	public static final String MIND_MAP_STYLE = "mindMapStyle";
	
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
		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapAddNodeController())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController())
		.addAdditiveController(CHILDREN_PROVIDER, new StyleChildrenProvider())
		.addAdditiveController(STYLE_PROPERTY_PROVIDER, new MindMapStyleStylePropertyProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_PERSISTENCE_CATEGORY)
		.addAdditiveController(PROPERTIES_PROVIDER, new PersistencePropertiesProvider())		
		.addAdditiveController(PROPERTY_SETTER, new PersistencePropertySetter())
		.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
		.addSingleController(ROOT_NODE_PROVIDER, new MindMapRootNodeProvider())
		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new PersistenceAddNodeProvider())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController())
		.addAdditiveController(PropertyDescriptor.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(MIN_WIDTH))
		.addAdditiveController(CHILDREN_PROVIDER, new StyleChildrenProvider());
		
		//TODO StyleRootNode
		TypeDescriptor stylesTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STYLE_ROOT_NODE);
		stylesTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new StylePropertiesProvider())
							.addAdditiveController(STYLE_PROPERTY_PROVIDER, new MindMapStyleStylePropertyProvider())
							.addAdditiveController(PropertyDescriptor.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("styleName"))
							.addAdditiveController(PropertyDescriptor.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(MIN_WIDTH).setReadOnlyAs(false))
							.addAdditiveController(PropertyDescriptor.PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(MAX_WIDTH).setReadOnlyAs(false))
							.addAdditiveController(CHILDREN_PROVIDER, new MindMapStyleChildrenProvider())
							.addSingleController(ROOT_NODE_PROVIDER, new MindMapStyleRootProvider())
							.addAdditiveController(PROPERTY_SETTER, new StylePropertySetter());

		TypeDescriptor mindMapStyleTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MIND_MAP_STYLE);
		mindMapStyleTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new MindMapStylePropertiesProvider());
		
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
