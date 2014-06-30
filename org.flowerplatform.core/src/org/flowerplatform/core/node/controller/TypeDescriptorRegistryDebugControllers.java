/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.BaseResourceHandler;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IController;
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
	
	class DebugController extends AbstractController implements IChildrenProvider {
		
		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Collections.singletonList(new Node(Utils.getUri(TYPES, DEBUG), TYPES));
		}
	}
	
	class TypesDebugController extends AbstractController implements IChildrenProvider {
		
		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Arrays.asList(new Node(Utils.getUri(TYPES_FLEX, TYPES), TYPES_FLEX), new Node(Utils.getUri(TYPES_JAVA, TYPES), TYPES_JAVA));
		}
	}
	
	class FlexTypesDebugController extends AbstractController implements IChildrenProvider {
	
		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			// should not be called from client
			return Collections.emptyList();
		}
	}
	
	class JavaTypesDebugController extends AbstractController implements IChildrenProvider {
		
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
				children.add(new Node(Utils.getUri(TYPE, TYPES_JAVA, type), TYPE));
			}
			return children;
		}
	}
	
	class TypeDebugController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			String type = Utils.getFragment(node.getNodeUri());
			TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type);
			List<Node> children = new ArrayList<Node>();
			
			// get categories
			for (String category : descriptor.getCategories()) {
				children.add(new Node(Utils.getUri(CATEGORY, type, category), CATEGORY));
			}
			
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
			
			// get single controllers keys
			for (String singleControllersKey : wrapper.getSingleControllersKeys()) {
				children.add(new Node(Utils.getUri(CONTROLLER_KEY_SINGLE, type, singleControllersKey), CONTROLLER_KEY_SINGLE));
			}
			
			// get additive controllers keys
			for (String additiveControllersKey : wrapper.getAdditiveControllersKeys()) {
				children.add(new Node(Utils.getUri(CONTROLLER_KEY_ADDITIVE, type, additiveControllersKey), CONTROLLER_KEY_ADDITIVE));
			}
			
			return children;
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(NAME, Utils.getFragment(node.getNodeUri()));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/idea.png"));
		}
	}
	
	class SingleControllerKeyDebugController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			// parent: CONTROLLER_KEY_SINGLE|type|controllerKey
			List<Node> children = new ArrayList<Node>();
			TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(Utils.getSchemeSpecificPart(node.getNodeUri()));
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
			
			Object cachedController = wrapper.getCachedSingleController(Utils.getFragment(node.getNodeUri()));
			Object selfController = wrapper.getSelfSingleController(Utils.getFragment(node.getNodeUri()));
			
			// add cached controller
			if (cachedController != null) {
				Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(cachedController, CONTROLLER_SINGLE, node.getNodeUri()); 
				String icons = (String) child.getProperties().get(ICONS);
				if (cachedController != selfController) {
					// override
					icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/attach.png");
					child.getProperties().put(ICONS, icons);
				}
				children.add(child);
			}
			
			// add self controller - only if different from cached
			if (selfController != null && selfController != cachedController) {
				Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(selfController, CONTROLLER_SINGLE, node.getNodeUri()); 
				// deleted
				child.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/button-cancel.png"));
				children.add(child);
			}
			
			return children;
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			// node: CONTROLLER_KEY_SINGLE|descriptorType|controllerKey
			node.getProperties().put(NAME, Utils.getFragment(node.getNodeUri()));
			String icons = ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/full-1.png");
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(Utils.getSchemeSpecificPart(node.getNodeUri())));
			if (wrapper.isCachedSingleController(Utils.getFragment(node.getNodeUri()))) {
				icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/flag.png");
			}
			node.getProperties().put(ICONS, icons);
		}
	}
	
	class AdditiveControllerKeyDebugController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			// parent: CONTROLLER_KEY_ADDITIVE|type|controllerKey
			List<Node> children = new ArrayList<Node>();
			TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(Utils.getSchemeSpecificPart(node.getNodeUri()));
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
			
			List<? extends IController> cachedControllers = wrapper.getCachedAdditiveControllers(Utils.getFragment(node.getNodeUri()));
			List<? extends IController> selfControllers = wrapper.getSelfAdditiveControllers(Utils.getFragment(node.getNodeUri()));
			
			// add controllers
			for (IController cachedController : cachedControllers) {
				Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(cachedController, CONTROLLER_ADDITIVE, node.getNodeUri()); 
				String icons = (String) child.getPropertyValue(ICONS);
				if (!selfControllers.contains(cachedController)) {
					// contributed
					icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/attach.png");
					child.getProperties().put(ICONS, icons);
				}
				children.add(child);
			}
			
			return children;
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			// node: CONTROLLER_KEY_ADDITIVE|descriptorType|controllerKey
			node.getProperties().put(NAME, Utils.getFragment(node.getNodeUri()));
			String icons = ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/positive.png");
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(Utils.getSchemeSpecificPart(node.getNodeUri())));
			if (wrapper.isCachedAdditiveController(Utils.getFragment(node.getNodeUri()))) {
				icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/flag.png");
			}
			node.getProperties().put(ICONS, icons);
		}
	}
	
	class CategoryDebugController extends AbstractController implements IPropertiesProvider {
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(NAME, Utils.getFragment(node.getNodeUri()));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/folder.png"));
		}
	}
	
	private void addResourceHandler(String type) {
		CorePlugin.getInstance().getResourceService().addResourceHandler(type, new BaseResourceHandler(type));
	}
	
	public void registerControllers() {
		
		addResourceHandler(TYPES);
		addResourceHandler(TYPES_FLEX);
		addResourceHandler(TYPES_JAVA);
		addResourceHandler(TYPE);
		addResourceHandler(CATEGORY);
		addResourceHandler(CONTROLLER_KEY_SINGLE);
		addResourceHandler(CONTROLLER_KEY_ADDITIVE);
		addResourceHandler(CONTROLLER_SINGLE);
		addResourceHandler(CONTROLLER_ADDITIVE);
		
		///////////////////////////////////////////////////
		// add types
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(DEBUG)
		.addAdditiveController(CHILDREN_PROVIDER, new DebugController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(TYPES)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Node Types"))
		.addAdditiveController(CHILDREN_PROVIDER, new TypesDebugController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(TYPES_FLEX)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Flex"))
		.addAdditiveController(CHILDREN_PROVIDER, new FlexTypesDebugController());
		
		///////////////////////////////////////////////////
		// add types to "Java Types"
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(TYPES_JAVA)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Java"))
		.addAdditiveController(CHILDREN_PROVIDER, new JavaTypesDebugController());
		
		///////////////////////////////////////////////////
		// add categories and controller keys to type
		///////////////////////////////////////////////////
		
		TypeDebugController typeDebugController = new TypeDebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(TYPE)
		.addAdditiveController(CHILDREN_PROVIDER, typeDebugController)
		.addAdditiveController(PROPERTIES_PROVIDER, typeDebugController);
		
		///////////////////////////////////////////////////
		// set category name and icons
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CATEGORY)
		.addAdditiveController(PROPERTIES_PROVIDER, new CategoryDebugController());
		
		///////////////////////////////////////////////////
		// add controllers to controller key
		///////////////////////////////////////////////////
		
		SingleControllerKeyDebugController singleControllerKeyDebugController = new SingleControllerKeyDebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CONTROLLER_KEY_SINGLE)
		.addAdditiveController(CHILDREN_PROVIDER, singleControllerKeyDebugController)
		.addAdditiveController(PROPERTIES_PROVIDER, singleControllerKeyDebugController);
		
		AdditiveControllerKeyDebugController additiveControllerKeyDebugController = new AdditiveControllerKeyDebugController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CONTROLLER_KEY_ADDITIVE)
		.addAdditiveController(CHILDREN_PROVIDER, additiveControllerKeyDebugController)
		.addAdditiveController(PROPERTIES_PROVIDER, additiveControllerKeyDebugController);
		
		///////////////////////////////////////////////////
		// controller descriptors
		///////////////////////////////////////////////////
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CONTROLLER_SINGLE);
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CONTROLLER_ADDITIVE);
		
	}
	
	private Node createControllerNode(Object controller, String type, String resource) {
		String id = controller.toString();
		Node node = new Node(Utils.getUri(type, resource, id), type);
		node.getProperties().put(NAME, id);
		node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/executable.png"));
		return node;
	}
	
}
