package org.flowerplatform.freeplane;

import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.DEFAULT_PROPERTY_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PARENT_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.RAW_NODE_DATA_PROVIDER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.RESOURCE_ACCESS_CONTROLLER;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_MINDMAP_CATEGORY;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_CATEGORY;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.DefaultPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.controller.FreeplaneIsSubscribablePropertyProvider;
import org.flowerplatform.freeplane.controller.FreeplaneResourceAccessController;
import org.flowerplatform.freeplane.controller.FreeplaneResourceChildrenProvider;
import org.flowerplatform.freeplane.controller.FreeplaneResourceRawNodeDataProvider;
import org.flowerplatform.freeplane.controller.MindMapAddNodeController;
import org.flowerplatform.freeplane.controller.MindMapChildrenProvider;
import org.flowerplatform.freeplane.controller.MindMapDefaultPropertyValueProvider;
import org.flowerplatform.freeplane.controller.MindMapFileAddNodeController;
import org.flowerplatform.freeplane.controller.MindMapFileContentTypeProvider;
import org.flowerplatform.freeplane.controller.MindMapParentProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertiesProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertySetter;
import org.flowerplatform.freeplane.controller.MindMapRawNodeDataProvider;
import org.flowerplatform.freeplane.controller.MindMapRemoveNodeController;
import org.flowerplatform.freeplane.controller.PersistenceAddNodeProvider;
import org.flowerplatform.freeplane.controller.PersistencePropertiesProvider;
import org.flowerplatform.freeplane.controller.PersistencePropertySetter;
import org.flowerplatform.freeplane.remote.MindMapServiceRemote;
import org.flowerplatform.freeplane.style.controller.MindMapStyleChildrenProvider;
import org.flowerplatform.freeplane.style.controller.StyleRootChildrenProvider;
import org.flowerplatform.freeplane.style.controller.StyleRootPropertiesProvider;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;
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
	
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_MINDMAP_CATEGORY)		
		.addAdditiveController(PROPERTIES_PROVIDER, new MindMapPropertiesProvider())
		.addAdditiveController(DEFAULT_PROPERTY_PROVIDER, new MindMapDefaultPropertyValueProvider())
		.addAdditiveController(PROPERTY_SETTER, new MindMapPropertySetter())
		.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapAddNodeController())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController())
		.addAdditiveController(CHILDREN_PROVIDER, new StyleRootChildrenProvider())
		.addAdditiveController(PROPERTIES_PROVIDER, new DefaultPropertiesProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_PERSISTENCE_CATEGORY)
		.addAdditiveController(PROPERTIES_PROVIDER, new PersistencePropertiesProvider())		
		.addAdditiveController(PROPERTY_SETTER, new PersistencePropertySetter())
		.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
		.addSingleController(RAW_NODE_DATA_PROVIDER, new MindMapRawNodeDataProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new PersistenceAddNodeProvider())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController());
		
		TypeDescriptor stylesRootTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STYLE_ROOT_NODE);
		stylesRootTypeDescriptor
		.addAdditiveController(PROPERTIES_PROVIDER, new StyleRootPropertiesProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new MindMapStyleChildrenProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.FILE_NODE_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new FreeplaneIsSubscribablePropertyProvider())
		.addAdditiveController(RESOURCE_ACCESS_CONTROLLER, new FreeplaneResourceAccessController(FREEPLANE_MINDMAP_CATEGORY))
		.addAdditiveController(CHILDREN_PROVIDER, new FreeplaneResourceChildrenProvider())
		.addSingleController(RAW_NODE_DATA_PROVIDER, new FreeplaneResourceRawNodeDataProvider())
		.addAdditiveController(PROPERTIES_PROVIDER, new MindMapFileContentTypeProvider())
		.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapFileAddNodeController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CoreConstants.CODE_TYPE)
		.addAdditiveController(RESOURCE_ACCESS_CONTROLLER, new FreeplaneResourceAccessController(FREEPLANE_PERSISTENCE_CATEGORY))
		.addAdditiveController(CHILDREN_PROVIDER, new FreeplaneResourceChildrenProvider())
		.addSingleController(RAW_NODE_DATA_PROVIDER, new FreeplaneResourceRawNodeDataProvider());		
		
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
	
	public Node getStandardNode(NodeModel nodeModel, String resource) {
		String resourceCategory = CorePlugin.getInstance().getResourceService().getResourceCategory(resource);
		
		String type = null;
		if (MindMapConstants.FREEPLANE_MINDMAP_CATEGORY.equals(resourceCategory)) {
			type = MindMapConstants.MINDMAP_NODE_TYPE;	
		} else if (MindMapConstants.FREEPLANE_PERSISTENCE_CATEGORY.equals(resourceCategory)) {
			// get type from attributes table
			NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);
			if (attributeTable != null) {
				for (Attribute attribute : attributeTable.getAttributes()) {
					if (attribute.getName().equals(MindMapConstants.FREEPLANE_PERSISTENCE_NODE_TYPE_KEY)) {
						type = (String) attribute.getValue();
						break;
					}
				}
			}
		}
		return new Node(type, resource, nodeModel.createID(), nodeModel);
	}
	
}
