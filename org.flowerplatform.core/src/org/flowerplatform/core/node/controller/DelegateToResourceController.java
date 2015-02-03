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

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * @author Mariana Gheorghe
 */
public class DelegateToResourceController extends AbstractController implements 
		IChildrenProvider, IParentProvider, IAddNodeController, IRemoveNodeController, 
		IPropertiesProvider, IPropertySetter, IDefaultPropertyValueProvider,
		IPersistenceController {

	/**
	 * @author Mariana Gheorghe
	 **/
	protected String getResource(String scheme) {
		return CoreConstants.CATEGORY_RESOURCE_PREFIX + scheme;
	}

	/**
	 * @author Mariana Gheorghe
	 **/
	protected TypeDescriptor getDescriptor(Node node) {
		return getTypeDescriptor().getRegistry().getExpectedTypeDescriptor(getResource(node.getScheme()));
	}

	/**
	 * @author Mariana Gheorghe
	 **/
	protected List<AbstractController> getControllers(Node node, String controllerType) {
		TypeDescriptor descriptor = getDescriptor(node);
		if (descriptor == null) {
			return Collections.emptyList();
		}

		List<AbstractController> controllers = descriptor.getAdditiveControllers(controllerType, node);
		controllers.remove(this);

		for (AbstractController controller : controllers) {
			if (controller instanceof UpdateController) {
				controllers.remove(controller);
				break;
			}
		}
		return controllers;
	}

	/**
	 * @author Mariana Gheorghe
	 **/
	protected AbstractController getController(Node node, String controllerType) {
		TypeDescriptor descriptor = getDescriptor(node);
		if (descriptor == null) {
			return null;
		}

		AbstractController controller = descriptor.getSingleController(controllerType, node);
		if (controller == this) {
			return null;
		}
		return controller;
	}

	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
		for (AbstractController controller : getControllers(node, CoreConstants.PROPERTY_SETTER)) {
			((IPropertySetter) controller).setProperties(node, properties, context);
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		for (AbstractController controller : getControllers(node, CoreConstants.PROPERTY_SETTER)) {
			((IPropertySetter) controller).unsetProperty(node, property, context);
		}
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		for (AbstractController controller : getControllers(node, CoreConstants.PROPERTIES_PROVIDER)) {
			((IPropertiesProvider) controller).populateWithProperties(node, context);
		}
	}

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		for (AbstractController controller : getControllers(node, CoreConstants.REMOVE_NODE_CONTROLLER)) {
			((IRemoveNodeController) controller).removeNode(node, child, context);
		}
	}

	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {
		for (AbstractController controller : getControllers(node, CoreConstants.ADD_NODE_CONTROLLER)) {
			((IAddNodeController) controller).addNode(node, child, context);
		}
	}

	@Override
	public Node getParent(Node node, ServiceContext<NodeService> context) {
		AbstractController controller = getController(node, CoreConstants.PARENT_PROVIDER);
		if (controller == null) {
			return null;
		}
		return ((IParentProvider) controller).getParent(node, context);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		for (AbstractController controller : getControllers(node, CoreConstants.CHILDREN_PROVIDER)) {
			children.addAll(((IChildrenProvider) controller).getChildren(node, context));
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		for (AbstractController controller : getControllers(node, CoreConstants.CHILDREN_PROVIDER)) {
			if (((IChildrenProvider) controller).hasChildren(node, context)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getDefaultValue(Node node, String property, ServiceContext<NodeService> context) {
		Object value = null;
		for (AbstractController controller : getControllers(node, CoreConstants.DEFAULT_PROPERTY_PROVIDER)) {
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
			value = ((IDefaultPropertyValueProvider) controller).getDefaultValue(node, property, context);
		}
		return value;
	}

}
