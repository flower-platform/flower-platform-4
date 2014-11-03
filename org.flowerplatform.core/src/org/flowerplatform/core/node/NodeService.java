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
package org.flowerplatform.core.node;

import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.DEFAULT_PROPERTY_PROVIDER;
import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.HAS_CHILDREN;
import static org.flowerplatform.core.CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES;
import static org.flowerplatform.core.CoreConstants.IS_DIRTY;
import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;
import static org.flowerplatform.core.CoreConstants.PARENT_PROVIDER;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IDefaultPropertyValueProvider;
import org.flowerplatform.core.node.controller.IParentProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The methods of this service are exposed on the web by {@link NodeServiceRemote}; {@link NodeServiceRemote}
 * has almost the same methods, but it uses <code>fullNodeId:String</code> instead of a {@link Node} instance. It 
 * does this to forbid receiving from the client populated nodes (which don't affect the business logic, but generate
 * unnecessary traffic).
 * 
 * @see NodeServiceRemote
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class NodeService {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(NodeService.class);
	
	protected TypeDescriptorRegistry registry;
	
	/**
	 *@author see class
	 **/
	public NodeService() {
		super();		
	}
	
	/**
	 *@author see class
	 **/
	public NodeService(TypeDescriptorRegistry registry) {
		super();
		this.registry = registry;
	}
	
	/**
	 *@author see class
	 **/
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {		
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		List<IChildrenProvider> providers = descriptor.getAdditiveControllers(CHILDREN_PROVIDER, node);
		// many times there will be no children; that's why we lazy init the list (if needed)
		List<Node> children = null;
		// we ask each registered provider for children
		for (IChildrenProvider provider : providers) {
			if (!CoreUtils.isControllerInvokable(provider, context)) {
				continue;
			}
			// we take the children ...
			List<Node> childrenFromCurrentProvider = provider.getChildren(node, context);
			if (childrenFromCurrentProvider != null) {
				for (Node currentChild : childrenFromCurrentProvider) {
					if (context.getBooleanValue(POPULATE_WITH_PROPERTIES)) {
						// ... and then populate them
						currentChild.getOrPopulateProperties(new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
					}
					
					// and add them to the result list
					if (children == null) {
						// lazy init of the list
						children = new ArrayList<Node>();
					}
					children.add(currentChild);
				}
			}
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		if (children == null) {
			return Collections.emptyList();
		} else {
			return children;
		}
	}
		
	/**
	 *@author see class
	 **/
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return false;
		}
		List<IChildrenProvider> childrenProviders = descriptor.getAdditiveControllers(CHILDREN_PROVIDER, node);
		for (IChildrenProvider provider : childrenProviders) {
			if (!CoreUtils.isControllerInvokable(provider, context)) {
				continue;
			}
 			if (provider.hasChildren(node, context)) {
				return true;
			}
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		return false;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public Object getDefaultPropertyValue(Node node, String property, ServiceContext<NodeService> context) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		List<IDefaultPropertyValueProvider> defaultPropertyProviders = descriptor.getAdditiveControllers(DEFAULT_PROPERTY_PROVIDER, node);
		Object propertyValue = null;
		for (IDefaultPropertyValueProvider provider : defaultPropertyProviders) {
			if (!CoreUtils.isControllerInvokable(provider, context)) {
				continue;
			}
			propertyValue = provider.getDefaultValue(node, property, context);
 			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
 				break;
			}
		}
		return propertyValue;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public List<AbstractController> getPropertyDescriptors(Node node) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		return descriptor.getAdditiveControllers(PROPERTY_DESCRIPTOR, node);
	}
		
	/**
	 * @author Mariana Gheorghe
	 */
	public Node getParent(Node node, ServiceContext<NodeService> context) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		IParentProvider provider = descriptor.getSingleController(PARENT_PROVIDER, node);
		if (!CoreUtils.isControllerInvokable(provider, context)) {
			return null;
		}
		Node parent = provider.getParent(node, context);
		if (parent == null) {
			return null;
		}
		return parent;
	}

	/**
	 *
	 * @author Claudiu Matei
	 */
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
		setProperties(node, Collections.singletonMap(property, value), context);
	}
		
	/**
	 *
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 * @author Claudiu Matei
	 */
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {		
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		ResourceService resourceService = CorePlugin.getInstance().getResourceService();
		boolean oldDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
		
		// Save values before the change
		Map<String, Object> oldValues = new HashMap<>();
		Map<String, Object> props = node.getOrPopulateProperties(context);
		for (String property : properties.keySet()) {
			if (props.containsKey(property)) {
				Object oldValue = node.getOrPopulateProperties(context).get(property);
				oldValues.put(property, oldValue);
			}
		}
		context.add(CoreConstants.OLD_VALUES, oldValues);
		
		List<IPropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER, node);		
		for (IPropertySetter controller : controllers) {
			if (!CoreUtils.isControllerInvokable(controller, context)) {
				continue;
			}
			controller.setProperties(node, properties, context);
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		
		// get dirty state after executing controllers
		boolean newDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
		if (oldDirty != newDirty) {			
			// dirty state changed -> change resourceNode isDirty property
			Node resourceNode = resourceService.getResourceNode(node.getNodeUri());
			setProperty(resourceNode, IS_DIRTY, newDirty, new ServiceContext<NodeService>(context.getService())
					.add(NODE_IS_RESOURCE_NODE, true).add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
		}
	}

	/**
	 * @author Mariana Gheorghe
	 */
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {	
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		ResourceService resourceService = CorePlugin.getInstance().getResourceService();
		boolean oldDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));

		// Save value before the change
		Map<String, Object> oldValues = new HashMap<>();
		Object oldValue = node.getOrPopulateProperties(context).get(property);
		oldValues.put(property, oldValue);
		context.add(CoreConstants.OLD_VALUES, oldValues);
		
		List<IPropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER, node);
		for (IPropertySetter controller : controllers) {
			if (!CoreUtils.isControllerInvokable(controller, context)) {
				continue;
			}
			controller.unsetProperty(node, property, context);
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		
		// get dirty state after executing controllers
		boolean newDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
		if (oldDirty != newDirty) {			
			// dirty state changed -> change resourceNode isDirty property
			Node resourceNode = resourceService.getResourceNode(node.getNodeUri());
			setProperty(resourceNode, IS_DIRTY, newDirty, new ServiceContext<NodeService>(context.getService())
					.add(NODE_IS_RESOURCE_NODE, true).add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
		}
	}
	
	/**
	 *@author see class
	 **/
	public void addChild(Node node, Node child, ServiceContext<NodeService> context) {		
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		ResourceService resourceService = CorePlugin.getInstance().getResourceService();
		boolean oldDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
				
		List<IAddNodeController> controllers = descriptor.getAdditiveControllers(ADD_NODE_CONTROLLER, node);
		for (IAddNodeController controller : controllers) {
			if (!CoreUtils.isControllerInvokable(controller, context)) {
				continue;
			}
			controller.addNode(node, child, context);
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		
		// get dirty state after executing controllers
		boolean newDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
		if (oldDirty != newDirty) {
			// dirty state changed -> change resourceNode isDirty property
			Node resourceNode = resourceService.getResourceNode(node.getNodeUri());
			setProperty(resourceNode, IS_DIRTY, newDirty, new ServiceContext<NodeService>(context.getService())
					.add(NODE_IS_RESOURCE_NODE, true).add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
		}
		setProperty(node, HAS_CHILDREN, hasChildren(node, new ServiceContext<NodeService>(context.getService())), new ServiceContext<NodeService>(context.getService())
				.add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
	}
	
	private void removeChildDFS(Node node, Node child, ArrayList<ChildrenUpdate> removedNodes) {
		Node removedNode = CorePlugin.getInstance().getResourceService().getNode(child.getNodeUri());
		removedNode.getOrPopulateProperties(new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		ChildrenUpdate update = new ChildrenUpdate();
		update.setFullNodeId(node.getNodeUri());
		update.setTargetNode(removedNode);
		removedNodes.add(update);
		List<Node> grandChildren = CorePlugin.getInstance().getNodeService().getChildren(child, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		for (Node grandChild : grandChildren) {
			removeChildDFS(child, grandChild, removedNodes);
		}
	}
	
	/**
	 *@author see class
	 **/
	public void removeChild(Node node, Node child, ServiceContext<NodeService> context) {	
		List<Node> grandChildren = CorePlugin.getInstance().getNodeService().getChildren(child, context);
		if (context.get("parentNode") == null) {
			context.add("parentNode", node);
		}
		for (int i = grandChildren.size() - 1; i >= 0; i--) {
			Node grandChild = grandChildren.get(i);
			removeChild(child, grandChild, context);
		}
		
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}

		ArrayList<ChildrenUpdate> removedNodes = new ArrayList<>();
		removeChildDFS(node, child, removedNodes);
		context.add("removedNodes", removedNodes);
		
		// Find next sibbling and save it for undo of position
		List<Node> sibblings = getChildren(node, context);
		int childIndex = sibblings.indexOf(child);
		if (childIndex < sibblings.size() - 1) {
			context.add(CoreConstants.INSERT_BEFORE_FULL_NODE_ID, sibblings.get(childIndex + 1).getNodeUri());
		}

		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		ResourceService resourceService = CorePlugin.getInstance().getResourceService();
		boolean oldDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));

		child.getOrPopulateProperties(context);
		List<IRemoveNodeController> controllers = descriptor.getAdditiveControllers(REMOVE_NODE_CONTROLLER, node);
		for (IRemoveNodeController controller : controllers) {
			if (!CoreUtils.isControllerInvokable(controller, context)) {
				continue;
			}
			controller.removeNode(node, child, context);
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		
		// get dirty state after executing controllers
		boolean newDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
		if (oldDirty != newDirty) {
			// dirty state changed -> change resourceNode isDirty property
			Node resourceNode = resourceService.getResourceNode(node.getNodeUri());
			setProperty(resourceNode, IS_DIRTY, newDirty, new ServiceContext<NodeService>(context.getService())
					.add(NODE_IS_RESOURCE_NODE, true).add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
		}
		setProperty(node, HAS_CHILDREN, hasChildren(node, new ServiceContext<NodeService>(context.getService())), new ServiceContext<NodeService>(context.getService())
				.add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
	}
	
	/**
	 * Internal method; shouldn't be called explicitly. It's invoked automatically by the {@link Node}.
	 */
	public void populateNodeProperties(Node node, ServiceContext<NodeService> context) {	
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
					
		List<IPropertiesProvider> providers = descriptor.getAdditiveControllers(PROPERTIES_PROVIDER, node);		
		for (IPropertiesProvider provider : providers) {
			if (!CoreUtils.isControllerInvokable(provider, context)) {
				continue;
			}
			provider.populateWithProperties(node, context);
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		
		node.getProperties().put(HAS_CHILDREN, hasChildren(node, new ServiceContext<NodeService>(context.getService())));

		ResourceService resourceService = CorePlugin.getInstance().getResourceService();
		node.getProperties().put(IS_DIRTY, resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService)));
	}

}
