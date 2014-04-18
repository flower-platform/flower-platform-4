package org.flowerplatform.core.node.controller;

import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.DEBUG;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.ICONS_SEPARATOR;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorDebugWrapper;

/**
 * @author Mariana Gheorghe
 */
public class TypeDescriptorRegistryDebugControllers {

	private final String TYPES = DEBUG + "NodeTypes";
	
	private final String TYPES_FLEX = DEBUG + "FlexTypes";
	
	private final String TYPES_JAVA = DEBUG + "JavaTypes";
	private final String TYPE = DEBUG + "JavaType";
	
	private final String CATEGORY = DEBUG + "JavaCategory";
	private final String CONTROLLER_KEY_SINGLE = DEBUG + "JavaControllerKeySingle";
	private final String CONTROLLER_KEY_ADDITIVE = DEBUG + "JavaControllerKeyAdditive";
	
	private final String CONTROLLER_SINGLE = DEBUG + "JavaControllerSingle";
	private final String CONTROLLER_ADDITIVE = DEBUG + "JavaControllerAdditive";
	
	public void registerControllers() {
		
		///////////////////////////////////////////////////
		// add types
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(DEBUG)
		.addAdditiveController(CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
				return Collections.singletonList(new Node(TYPES, DEBUG, null, null));
			}
		});
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(TYPES)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Node Types"))
		.addAdditiveController(CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
				return Arrays.asList(new Node(TYPES_FLEX, TYPES, null, null), new Node(TYPES_JAVA, TYPES, null, null));
			}
		});
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(TYPES_FLEX)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Flex"))
		.addAdditiveController(CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
				// should not be called from client
				return Collections.emptyList();
			}
		});
		
		///////////////////////////////////////////////////
		// add types to "Java Types"
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(TYPES_JAVA)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Java"))
		.addAdditiveController(CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
				List<Node> children = new ArrayList<Node>();
				List<String> types = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getRegisteredTypes();
				Collections.sort(types); // show them in alphabetical order for better readability
				for (String type : types) {
					children.add(new Node(TYPE, TYPES_JAVA, type, null));
				}
				return children;
			}
		});
		
		///////////////////////////////////////////////////
		// add categories and controller keys to type
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
				node.getProperties().put(NAME, node.getIdWithinResource());
				node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/idea.png"));
			}
		})
		.addAdditiveController(CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
				String type = node.getIdWithinResource();
				TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type);
				List<Node> children = new ArrayList<Node>();
				
				// get categories
				for (String category : descriptor.getCategories()) {
					children.add(new Node(CATEGORY, type, category, null));
				}
				
				TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
				
				// get single controllers keys
				for (String singleControllersKey : wrapper.getSingleControllersKeys()) {
					children.add(new Node(CONTROLLER_KEY_SINGLE, type, singleControllersKey, null));
				}
				
				// get additive controllers keys
				for (String additiveControllersKey : wrapper.getAdditiveControllersKeys()) {
					children.add(new Node(CONTROLLER_KEY_ADDITIVE, type, additiveControllersKey, null));
				}
				
				return children;
			}
		});
		
		///////////////////////////////////////////////////
		// set category name and icons
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CATEGORY)
		.addAdditiveController(PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
				node.getProperties().put(NAME, node.getIdWithinResource());
				node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/folder.png"));
			}
		});
		
		///////////////////////////////////////////////////
		// add controllers to controller key
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CONTROLLER_KEY_SINGLE)
		.addAdditiveController(PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
				// node: CONTROLLER_KEY_SINGLE|descriptorType|controllerKey
				node.getProperties().put(NAME, node.getIdWithinResource());
				String icons = ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/full-1.png");
				TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getResource()));
				if (wrapper.isCachedSingleController(node.getIdWithinResource())) {
					icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/flag.png");
				}
				node.getProperties().put(ICONS, icons);
			}
		})
		.addAdditiveController(CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
				// parent: CONTROLLER_KEY_SINGLE|type|controllerKey
				List<Node> children = new ArrayList<Node>();
				TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getResource());
				TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
				
				AbstractController cachedController = wrapper.getCachedSingleController(node.getIdWithinResource());
				AbstractController selfController = wrapper.getSelfSingleController(node.getIdWithinResource());
				
				// add cached controller
				if (cachedController != null) {
					Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(cachedController, CONTROLLER_SINGLE, node.getFullNodeId()); 
					String icons = (String) child.getOrPopulateProperties().get(ICONS);
					if (cachedController != selfController) {
						// override
						icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/attach.png");
						child.getOrPopulateProperties().put(ICONS, icons);
					}
					children.add(child);
				}
				
				// add self controller - only if different from cached
				if (selfController != null && selfController != cachedController) {
					Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(selfController, CONTROLLER_SINGLE, node.getFullNodeId()); 
					// deleted
					child.getOrPopulateProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/button-cancel.png"));
					children.add(child);
				}
				
				return children;
			}
		});
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CONTROLLER_KEY_ADDITIVE)
		.addAdditiveController(PROPERTIES_PROVIDER, new PropertiesProvider() {
			
			@Override
			public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
				// node: CONTROLLER_KEY_ADDITIVE|descriptorType|controllerKey
				node.getProperties().put(NAME, node.getIdWithinResource());
				String icons = ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/positive.png");
				TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getResource()));
				if (wrapper.isCachedAdditiveController(node.getIdWithinResource())) {
					icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/flag.png");
				}
				node.getProperties().put(ICONS, icons);
			}
		})
		.addAdditiveController(CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
				// parent: CONTROLLER_KEY_ADDITIVE|type|controllerKey
				List<Node> children = new ArrayList<Node>();
				TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getResource());
				TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
				
				List<? extends AbstractController> cachedControllers = wrapper.getCachedAdditiveControllers(node.getIdWithinResource());
				List<? extends AbstractController> selfControllers = wrapper.getSelfAdditiveControllers(node.getIdWithinResource());
				
				// add controllers
				for (AbstractController cachedController : cachedControllers) {
					Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(cachedController, CONTROLLER_ADDITIVE, node.getFullNodeId()); 
					String icons = (String) child.getOrPopulateProperties().get(ICONS);
					if (!selfControllers.contains(cachedController)) {
						// contributed
						icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/attach.png");
						child.getOrPopulateProperties().put(ICONS, icons);
					}
					children.add(child);
				}
				
				return children;
			}
		});
		
		///////////////////////////////////////////////////
		// controller descriptors
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CONTROLLER_SINGLE);
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CONTROLLER_ADDITIVE);
		
	}
	
	private Node createControllerNode(AbstractController controller, String type, String resource) {
		String id = controller.toString();
		Node node = new Node(type, resource, id, null);
		node.getOrPopulateProperties().put(NAME, id);
		node.getOrPopulateProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/executable.png"));
		return node;
	}
	
}
