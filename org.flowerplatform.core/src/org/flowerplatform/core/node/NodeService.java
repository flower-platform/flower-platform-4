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
package org.flowerplatform.core.node;

import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.DEFAULT_PROPERTY_PROVIDER;
import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.HAS_CHILDREN;
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
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IDefaultPropertyValueProvider;
import org.flowerplatform.core.node.controller.IParentProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
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
		
	private final static Logger logger = LoggerFactory.getLogger(NodeService.class);
	
	protected TypeDescriptorRegistry registry;
	
	public NodeService() {
		super();		
	}
	
	public NodeService(TypeDescriptorRegistry registry) {
		super();
		this.registry = registry;
	}
	
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
			// we take the children ...
			List<Node> childrenFromCurrentProvider = provider.getChildren(node, context);
			if (childrenFromCurrentProvider != null) {
				for (Node currentChild : childrenFromCurrentProvider) {
					if (context.getBooleanValue(POPULATE_WITH_PROPERTIES)) {
						// ... and then populate them
						currentChild.getOrPopulateProperties();
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
		
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return false;
		}
		List<IChildrenProvider> childrenProviders = descriptor.getAdditiveControllers(CHILDREN_PROVIDER, node);
		for (IChildrenProvider provider : childrenProviders) {
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
		Node parent = provider.getParent(node, context);
		if (parent == null) {
			return null;
		}
		return parent;
	}
	
	/**
	 *
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 * @author Claudiu Matei
	 */
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {		
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		ResourceService resourceService = CorePlugin.getInstance().getResourceService();
		boolean oldDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
		
		// Save value before the change
		if (node.getOrPopulateProperties().containsKey(property)) {
			Object oldValue = node.getOrPopulateProperties().get(property);
			context.add(CoreConstants.OLD_VALUE, oldValue);
		}
		
		List<IPropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER, node);
		PropertyValueWrapper wrapper = new PropertyValueWrapper(value);
		for (IPropertySetter controller : controllers) {
			controller.setProperty(node, property, wrapper, context);
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		
		// get dirty state after executing controllers
		boolean newDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
		if (oldDirty != newDirty) {			
			// dirty state changed -> change resourceNode isDirty property
			Node resourceNode = resourceService.getResourceNode(node.getNodeUri());
			setProperty(resourceNode, IS_DIRTY, newDirty, new ServiceContext<NodeService>(context.getService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
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
					
		List<IPropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER, node);
		for (IPropertySetter controller : controllers) {
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
			setProperty(resourceNode, IS_DIRTY, newDirty, new ServiceContext<NodeService>(context.getService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
		}
	}
	
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
			setProperty(resourceNode, IS_DIRTY, newDirty, new ServiceContext<NodeService>(context.getService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
		}
		setProperty(node, HAS_CHILDREN, hasChildren(node, new ServiceContext<NodeService>(context.getService())), new ServiceContext<NodeService>(context.getService()).add(EXECUTE_ONLY_FOR_UPDATER, true));
	}
	
	public void removeChild(Node node, Node child, ServiceContext<NodeService> context) {	
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		ResourceService resourceService = CorePlugin.getInstance().getResourceService();
		boolean oldDirty = resourceService.isDirty(node.getNodeUri(), new ServiceContext<ResourceService>(resourceService));
						
		List<IRemoveNodeController> controllers = descriptor.getAdditiveControllers(REMOVE_NODE_CONTROLLER, node);
		for (IRemoveNodeController controller : controllers) {
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
			setProperty(resourceNode, IS_DIRTY, newDirty, new ServiceContext<NodeService>(context.getService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
		}
		setProperty(node, HAS_CHILDREN, hasChildren(node, new ServiceContext<NodeService>(context.getService())), new ServiceContext<NodeService>(context.getService()).add(EXECUTE_ONLY_FOR_UPDATER, true));
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
