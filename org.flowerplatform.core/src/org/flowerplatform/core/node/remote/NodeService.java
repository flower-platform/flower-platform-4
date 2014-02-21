package org.flowerplatform.core.node.remote;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.ParentProvider.PARENT_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.RootNodeProvider.ROOT_NODE_PROVIDER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.ParentProvider;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.controller.RootNodeProvider;
import org.flowerplatform.core.node.update.RootNodeInfoDAO;
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
	
	protected TypeDescriptorRegistry registry;
	
	protected RootNodeInfoDAO rootNodeInfoDAO;
	
	public NodeService() {
		super();		
	}
	
	public NodeService(TypeDescriptorRegistry registry, RootNodeInfoDAO rootNodeInfoDAO) {
		super();
		this.registry = registry;
		this.rootNodeInfoDAO = rootNodeInfoDAO;
	}

	public RootNodeInfoDAO getRootNodeInfoDAO() {
		return rootNodeInfoDAO;
	}
	
	public List<Node> getChildren(Node node, boolean populateProperties) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		List<ChildrenProvider> providers = descriptor.getAdditiveControllers(CHILDREN_PROVIDER, node);
		// many times there will be no children; that's why we lazy init the list (if needed)
		List<Node> children = null;
		// we ask each registered provider for children
		for (ChildrenProvider provider : providers) {
			// we take the children ...
			List<Node> childrenFromCurrentProvider = provider.getChildren(node);
			for (Node currentChild : childrenFromCurrentProvider) {
				if (populateProperties) {
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
		if (children == null) {
			return Collections.emptyList();
		} else {
			return children;
		}
	}

	
	public Node getParent(Node node) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		ParentProvider provider = descriptor.getSingleController(PARENT_PROVIDER, node);
		Pair<Node, Object> parent = provider.getParent(node);
		if (parent == null) {
			return null;
		}
		return parent.a;
	}
		
	/**
	 * @author Mariana Gheorghe
	 */
	public List<PropertyDescriptor> getPropertyDescriptors(Node node) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return Collections.emptyList();
		}
		
		return descriptor.getAdditiveControllers(PROPERTY_DESCRIPTOR, node);
	}
	
	public void setProperty(Node node, String property, Object value) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<PropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER, node);
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
		
		List<PropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER, node);
		for (PropertySetter controller : controllers) {
			controller.unsetProperty(node, property);
		}
	}
	
	public void addChild(Node node, Node child, Node currentChildAtInsertionPoint) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<AddNodeController> controllers = descriptor.getAdditiveControllers(ADD_NODE_CONTROLLER, node);
		for (AddNodeController controller : controllers) {
			controller.addNode(node, child, currentChildAtInsertionPoint);
		}
	}
	
	public void removeChild(Node node, Node child) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<RemoveNodeController> controllers = descriptor.getAdditiveControllers(REMOVE_NODE_CONTROLLER, node);
		for (RemoveNodeController controller : controllers) {
			controller.removeNode(node, child);
		}
	}
	
	public Node getRootNode(Node node) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		List<RootNodeProvider> providers = descriptor.getAdditiveControllers(ROOT_NODE_PROVIDER, node);
		Node rootNode = null;
		for (RootNodeProvider provider : providers) {
			rootNode = provider.getRootNode(node);
			if (rootNode != null) {
				break;
			}
		}
		return rootNode;
	}
	
	public NodeWithVisibleChildren refresh(NodeWithVisibleChildren currentNodeWithVisibleChildren) {	
		Node currentNode = currentNodeWithVisibleChildren.getNode();
		boolean currentNodeHasVisibleChildren = currentNodeWithVisibleChildren.getVisibleChildren() != null;
		
		// get new data
		NodeWithVisibleChildren newNodeWithVisibleChildren = computeNodeWithVisibleChildren(currentNode, currentNodeHasVisibleChildren);
		
		if (currentNodeHasVisibleChildren) {
			// verify if node exists in list of new visibleChildren
			for (NodeWithVisibleChildren currentChildWithVisibleChildren : currentNodeWithVisibleChildren.getVisibleChildren()) {				
				for (int i = 0; i < newNodeWithVisibleChildren.getVisibleChildren().size(); i++) {
					Node newChildNode = newNodeWithVisibleChildren.getVisibleChildren().get(i).getNode();
					if (newChildNode.getId().equals(currentChildWithVisibleChildren.getNode().getId())) {	// node exists in new list -> refresh its structure also			
						newNodeWithVisibleChildren.getVisibleChildren().set(i, refresh(currentChildWithVisibleChildren));
						break;
					}
				}
			}
		}
		return newNodeWithVisibleChildren;
	}
		
	private NodeWithVisibleChildren computeNodeWithVisibleChildren(Node node, boolean  populateWithChildren) {	
		NodeWithVisibleChildren newNodeWithVisibleChildren = new NodeWithVisibleChildren();
		newNodeWithVisibleChildren.setNode(node);
		
		node.getOrPopulateProperties();
			
		// get new visible children
		List<NodeWithVisibleChildren> visibleChildren = new ArrayList<NodeWithVisibleChildren>();
		if (populateWithChildren) {			
			for (Node child : getChildren(node, false)) { // get children without properties
				visibleChildren.add(computeNodeWithVisibleChildren(child, false)); // here we populate it
			}
		}
		newNodeWithVisibleChildren.setVisibleChildren(visibleChildren);
		return newNodeWithVisibleChildren;
	}	
	
	/**
	 * @author Mariana Gheorghe
	 */
	public Map<String, List<AddChildDescriptor>> getAddChildDescriptors() {
		Map<String, List<AddChildDescriptor>> descriptorsMap = new HashMap<String, List<AddChildDescriptor>>();
		
		Collection<TypeDescriptor> typeDescriptors = registry.getTypeDescriptors();
		for (TypeDescriptor typeDescriptor : typeDescriptors) {
			List<AddChildDescriptor> addChildDescriptors = typeDescriptor.getAdditiveControllers(AddChildDescriptor.ADD_CHILD_DESCRIPTOR, null);
			descriptorsMap.put(typeDescriptor.getType(), addChildDescriptors);
		}
		
		return descriptorsMap;
	}
	
	public void subscribe(Node rootNode) {
		rootNodeInfoDAO.subscribe(CorePlugin.getInstance().getSessionId(), rootNode);
	}

	public void unsubscribe(Node rootNode) {	
		rootNodeInfoDAO.unsubscribe(CorePlugin.getInstance().getSessionId(), rootNode);
	}
	
	public void stillSubscribedPing(Node rootNode) {
		rootNodeInfoDAO.stillSubscribedPing(CorePlugin.getInstance().getSessionId(), rootNode);
	}
}
