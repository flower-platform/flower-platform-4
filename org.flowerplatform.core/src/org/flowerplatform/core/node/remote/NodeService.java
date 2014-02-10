package org.flowerplatform.core.node.remote;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.ParentProvider.PARENT_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.ParentProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class NodeService {
	
	private final static Logger logger = LoggerFactory.getLogger(NodeService.class);
	
	protected static TypeDescriptorRegistry registry;
	
	public NodeService() {
		super();		
	}
	
	public NodeService(TypeDescriptorRegistry registry) {
		super();
		NodeService.registry = registry;
	}

	public List<Node> getChildren(Node node, boolean populateProperties) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		List<ChildrenProvider> providers = descriptor.getAdditiveControllers(CHILDREN_PROVIDER);
		// many times there will be no children; that's why we lazy init the list (if needed)
		List<Node> children = null;
		// we ask each registered provider for children
		for (ChildrenProvider provider : providers) {
			// we take the children ...
			List<Pair<Node, Object>> childrenFromCurrentProvider = provider.getChildren(node);
			for (Pair<Node, Object> currentChild : childrenFromCurrentProvider) {
				if (populateProperties) {
					// ... and then populate them
					populateNode(currentChild.a, currentChild.b);
				}
				
				// and add them to the result list
				if (children == null) {
					// lazy init of the list
					children = new ArrayList<Node>();
				}
				children.add(currentChild.a);
			}
		}
		if (children == null) {
			return Collections.emptyList();
		} else {
			return children;
		}
	}
	
	public Node getParent(Node node, boolean populateProperties) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		ParentProvider provider = descriptor.getSingleController(PARENT_PROVIDER);
		Pair<Node, Object> parent = provider.getParent(node);
		if (parent == null) {
			return null;
		}
		if (populateProperties) {
			populateNode(parent.a, parent.b);
		}
		return parent.a;
	}
	
	@SuppressWarnings("unchecked")
	protected void populateNode(Node node, Object rawNodeData) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<PropertiesProvider<?>> providers = descriptor.getAdditiveControllers(PROPERTIES_PROVIDER);
		for (PropertiesProvider<?> provider : providers) {
			((PropertiesProvider<Object>) provider).populateWithProperties(node, rawNodeData);
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	public List<PropertyDescriptor> getPropertyDescriptors(String type) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(type);
		if (descriptor == null) {
			return Collections.emptyList();
		}
		
		return descriptor.getAdditiveControllers(PROPERTY_DESCRIPTOR);
	}
	
	public void setProperty(Node node, String property, Object value) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<PropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER);
		for (PropertySetter controller : controllers) {
			controller.setProperty(node, property, value);
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	public void unsetProperty(Node node, String property) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<PropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER);
		for (PropertySetter controller : controllers) {
			controller.unsetProperty(node, property);
		}
	}
	
	public void addChild(Node node, Node child) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<AddNodeController> controllers = descriptor.getAdditiveControllers(ADD_NODE_CONTROLLER);
		for (AddNodeController controller : controllers) {
			controller.addNode(node, child);
		}
	}
	
	public void removeChild(Node node, Node child) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<RemoveNodeController> controllers = descriptor.getAdditiveControllers(REMOVE_NODE_CONTROLLER);
		for (RemoveNodeController controller : controllers) {
			controller.removeNode(node, child);
		}
	}
	
}
