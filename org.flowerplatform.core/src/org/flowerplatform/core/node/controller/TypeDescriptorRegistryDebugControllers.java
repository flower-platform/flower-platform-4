/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IController;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorDebugWrapper;

/**
 * @author Mariana Gheorghe
 */
public class TypeDescriptorRegistryDebugControllers extends DebugControllers {

	private static final String TYPES = DEBUG + "NodeTypes";
	
	private static final String TYPES_FLEX = DEBUG + "FlexTypes";
	
	private static final String TYPES_JAVA = DEBUG + "JavaTypes";
	private static final String TYPE = DEBUG + "JavaType";
	
	private static final String CATEGORY = DEBUG + "JavaCategory";
	private static final String CONTROLLER_KEY_SINGLE = DEBUG + "JavaControllerKeySingle";
	private static final String CONTROLLER_KEY_ADDITIVE = DEBUG + "JavaControllerKeyAdditive";
	
	private static final String CONTROLLER_SINGLE = DEBUG + "JavaControllerSingle";
	private static final String CONTROLLER_ADDITIVE = DEBUG + "JavaControllerAdditive";
	
	/**
	 * @author Mariana Gheroghe
	 */
	class DebugController extends AbstractController implements IChildrenProvider {
		
		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Collections.singletonList(createVirtualNode(TYPES, null));
		}
	}
	
	/**
	 * @author Mariana Gheroghe
	 */
	class TypesDebugController extends AbstractController implements IChildrenProvider {
		
		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			return Arrays.asList(
					createVirtualNode(TYPES_FLEX, null),
					createVirtualNode(TYPES_JAVA, null));
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
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
	
	/**
	 * @author Mariana Gheorghe
	 */
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
				children.add(createVirtualNode(TYPE, type));
			}
			return children;
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	class TypeDebugController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			String type = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type);
			List<Node> children = new ArrayList<Node>();
			
			// get categories
			for (String category : descriptor.getCategories()) {
				children.add(createVirtualNode(CATEGORY, type + "#" + category));
			}
			
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
			
			// get single controllers keys
			for (String singleControllersKey : wrapper.getSingleControllersKeys()) {
				children.add(createVirtualNode(CONTROLLER_KEY_SINGLE, type + "#" + singleControllersKey));
			}
			
			// get additive controllers keys
			for (String additiveControllersKey : wrapper.getAdditiveControllersKeys()) {
				children.add(createVirtualNode(CONTROLLER_KEY_ADDITIVE, type + "#" + additiveControllersKey));
			}
			
			return children;
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(NAME, getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri()));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/idea.png"));
		}
	}
	
	/**
	 * @author Mariana Gheroghe
	 */
	class SingleControllerKeyDebugController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			List<Node> children = new ArrayList<Node>();
			String type = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type);
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
			
			String controllerType = Utils.getFragment(node.getNodeUri());
			Object cachedController = wrapper.getCachedSingleController(controllerType);
			Object selfController = wrapper.getSelfSingleController(controllerType);
			
			// add cached controller
			if (cachedController != null) {
				Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(cachedController, CONTROLLER_SINGLE, type); 
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
				Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(selfController, CONTROLLER_SINGLE, type); 
				// deleted
				child.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/button-cancel.png"));
				children.add(child);
			}
			
			return children;
		}
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			String type = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			node.getProperties().put(NAME, type);
			String icons = ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/full-1.png");
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type));
			if (wrapper.isCachedSingleController(Utils.getFragment(node.getNodeUri()))) {
				icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/flag.png");
			}
			node.getProperties().put(ICONS, icons);
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	class AdditiveControllerKeyDebugController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

		@Override
		public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
			return true;
		}
		
		@Override
		public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
			List<Node> children = new ArrayList<Node>();
			String type = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type);
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(descriptor);
			
			String controllerType = Utils.getFragment(node.getNodeUri());
			List<? extends IController> cachedControllers = wrapper.getCachedAdditiveControllers(controllerType);
			List<? extends IController> selfControllers = wrapper.getSelfAdditiveControllers(controllerType);
			
			// add controllers
			for (IController cachedController : cachedControllers) {
				Node child = TypeDescriptorRegistryDebugControllers.this.createControllerNode(cachedController, CONTROLLER_ADDITIVE, type); 
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
			String controllerType = Utils.getFragment(node.getNodeUri());
			node.getProperties().put(NAME, controllerType);
			String icons = ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/positive.png");
			String type = getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
			TypeDescriptorDebugWrapper wrapper = new TypeDescriptorDebugWrapper(CorePlugin.getInstance()
					.getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(type));
			if (wrapper.isCachedAdditiveController(controllerType)) {
				icons += ICONS_SEPARATOR + ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/flag.png");
			}
			node.getProperties().put(ICONS, icons);
		}
	}
	
	/**
	 * @author Mariana Gheroghe
	 */
	class CategoryDebugController extends AbstractController implements IPropertiesProvider {
		
		@Override
		public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
			node.getProperties().put(NAME, Utils.getFragment(node.getNodeUri()));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/folder.png"));
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	public void registerControllers() {
		
		addVirtualDebugType(TYPES);
		addVirtualDebugType(TYPES_FLEX);
		addVirtualDebugType(TYPES_JAVA);
		addVirtualDebugType(TYPE);
		addVirtualDebugType(CATEGORY);
		addVirtualDebugType(CONTROLLER_KEY_SINGLE);
		addVirtualDebugType(CONTROLLER_KEY_ADDITIVE);
		addVirtualDebugType(CONTROLLER_SINGLE);
		addVirtualDebugType(CONTROLLER_ADDITIVE);
		
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
		Node node = createVirtualNode(type, resource);
		node.getProperties().put(NAME, id);
		node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/executable.png"));
		return node;
	}
	
}
