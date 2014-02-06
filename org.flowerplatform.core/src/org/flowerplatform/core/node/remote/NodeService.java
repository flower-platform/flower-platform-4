package org.flowerplatform.core.node.remote;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.node.controller.update.UpdaterPersistenceProvider.UPDATER_CONTROLLER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.controller.update.UpdaterPersistenceProvider;
import org.flowerplatform.core.node.update.remote.ChildrenListUpdate;
import org.flowerplatform.core.node.update.remote.ClientNodeStatus;
import org.flowerplatform.core.node.update.remote.NodeUpdate;
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
	
	private static final String FULL_CHILDREN_LIST_KEY = "fullChildrenList";
	
	protected static TypeDescriptorRegistry registry;
	
	public NodeService() {
		super();		
	}
	
	public NodeService(TypeDescriptorRegistry registry) {
		super();
		NodeService.registry = registry;
	}

	private NodeUpdate checkForNodeUpdates(ClientNodeStatus clientNodeStatus, Map<String, Object> context, List<ChildrenListUpdate> listUpdates) {
		NodeUpdate nodeUpdate = new NodeUpdate();
		nodeUpdate.setNode(clientNodeStatus.getNode());
		
		if (context != null && context.containsKey(FULL_CHILDREN_LIST_KEY)) {
			nodeUpdate.setFullChildrenList(getChildren(nodeUpdate.getNode(), true));
			return nodeUpdate;
		}
				
		nodeUpdate.setUpdatedProperties(
				getPropertyUpdates(
						clientNodeStatus.getNode(), 
						clientNodeStatus.getTimestamp()));
					
		if (clientNodeStatus.getVisibleChildren() != null) {
			listUpdates.addAll(
					getChildrenListUpdates(
							clientNodeStatus.getNode(), 
							clientNodeStatus.getTimestamp()));
			
			for (ClientNodeStatus childNodeStatus : clientNodeStatus.getVisibleChildren()) {
				nodeUpdate.addNodeUpdatesForChild(checkForNodeUpdates(childNodeStatus, context, listUpdates));
			}
		}
		return nodeUpdate;
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
	
	public NodeUpdate checkForNodeUpdates(ClientNodeStatus clientNodeStatus, Map<String, Object> context) {				
		List<ChildrenListUpdate> listUpdates = new ArrayList<ChildrenListUpdate>();
		
		NodeUpdate nodeUpdate = checkForNodeUpdates(clientNodeStatus, context, listUpdates);
		
		Collections.sort(listUpdates, new Comparator<ChildrenListUpdate>() {
			public int compare(ChildrenListUpdate u1, ChildrenListUpdate u2) {
				return Long.compare(u1.getTimestamp1(), u2.getTimestamp1());
            }
		});		
		
		nodeUpdate.setChildrenListUpdates(listUpdates);
		
		return nodeUpdate;
	}
	
	public Map<String, Object> getPropertyUpdates(Node node, long startingWithTimestamp) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		UpdaterPersistenceProvider provider = descriptor.getSingleController(UPDATER_CONTROLLER);		
		return provider.getPropertyUpdates(node, startingWithTimestamp);		
	}
	
	public List<ChildrenListUpdate> getChildrenListUpdates(Node node, long startingWithTimestamp) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		UpdaterPersistenceProvider provider = descriptor.getSingleController(UPDATER_CONTROLLER);		
		return provider.getChildrenListUpdates(node, startingWithTimestamp);	
	}
	
	public void addPropertyUpdate(Node node, long timestamp, String property, Object value) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		UpdaterPersistenceProvider provider = descriptor.getSingleController(UPDATER_CONTROLLER);	
		provider.addPropertyUpdate(node, timestamp, property, value);		
	}
	
	public void addChildrenListUpdate(Node node, long timestamp, String type, int index, Node childNode) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		UpdaterPersistenceProvider provider = descriptor.getSingleController(UPDATER_CONTROLLER);
		provider.addChildrenListUpdate(node, timestamp, type, index, childNode);	
	}
	
}
