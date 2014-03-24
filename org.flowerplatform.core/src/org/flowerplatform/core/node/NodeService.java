package org.flowerplatform.core.node;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.IS_DIRTY;
import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.ParentProvider.PARENT_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RawNodeDataProvider.RAW_NODE_DATA_PROVIDER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.NodePropertiesConstants;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.ParentProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.controller.RawNodeDataProvider;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.TypeDescriptorRemote;
import org.flowerplatform.core.node.update.controller.UpdatePropertySetterController;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IDescriptor;
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
	
	/**
	 * An additive controller may set this option, to stop the service from invoking any controllers
	 * with a higher order index.
	 */
	public static final String STOP_CONTROLLER_INVOCATION = "stopControllerInvocation";
	
	/**
	 * An additive controller may set this option, to ask the {@link UpdatePropertySetterController} to use
	 * the node given as parameter as the resourceNode where the update will be saved.
	 * 
	 * <p>
	 * An example is {@link NodePropertiesConstants#IS_DIRTY}: if option not set, the update will be registered on parent resource -> bad.
	 */
	public static final String NODE_IS_RESOURCE_NODE = "nodeIsResourceNode";
	
	private final static Logger logger = LoggerFactory.getLogger(NodeService.class);
	
	protected TypeDescriptorRegistry registry;
	
	public NodeService() {
		super();		
	}
	
	public NodeService(TypeDescriptorRegistry registry) {
		super();
		this.registry = registry;
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
		Map<String, Object> options = getControllerInvocationOptions();
		for (ChildrenProvider provider : providers) {
			// we take the children ...
			List<Node> childrenFromCurrentProvider = provider.getChildren(node, options);
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
			if ((boolean) options.get(STOP_CONTROLLER_INVOCATION)) {
				break;
			}
		}
		if (children == null) {
			return Collections.emptyList();
		} else {
			return children;
		}
	}
		
	public boolean hasChildren(Node node) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return false;
		}
		List<ChildrenProvider> childrenProviders = descriptor.getAdditiveControllers(CHILDREN_PROVIDER, node);
		Map<String, Object> options = getControllerInvocationOptions();
		for (ChildrenProvider provider : childrenProviders) {
			if (provider.hasChildren(node, options)) {
				return true;
			}
			if ((boolean) options.get(STOP_CONTROLLER_INVOCATION)) {
				break;
			}
		}
		return false;
	}
		
	/**
	 * @author Mariana Gheorghe
	 */
	public Node getParent(Node node) {
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		ParentProvider provider = descriptor.getSingleController(PARENT_PROVIDER, node);
		Node parent = provider.getParent(node);
		if (parent == null) {
			return null;
		}
		return parent;
	}
	

	public void setProperty(Node node, String property, Object value, Map<String, Object> options) {		
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		Node resourceNode = CoreUtils.getRootNode(node);
		boolean oldDirty = CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNode.getFullNodeId(), getControllerInvocationOptions());
				
		List<PropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER, node);
		PropertyValueWrapper wrapper = new PropertyValueWrapper(value);
		for (PropertySetter controller : controllers) {
			controller.setProperty(node, property, wrapper, options);
		}
		
		// get dirty state after executing controllers
		boolean newDirty = CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNode.getFullNodeId(), getControllerInvocationOptions());
		if (oldDirty != newDirty) {			
			// dirty state changed -> change resourceNode isDirty property
			options.put(NODE_IS_RESOURCE_NODE, true);
			CorePlugin.getInstance().getNodeService().setProperty(resourceNode, IS_DIRTY, newDirty, options);
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	public void unsetProperty(Node node, String property, Map<String, Object> options) {	
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		Node resourceNode = CoreUtils.getRootNode(node);
		boolean oldDirty = CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNode.getFullNodeId(), getControllerInvocationOptions());
					
		List<PropertySetter> controllers = descriptor.getAdditiveControllers(PROPERTY_SETTER, node);
		for (PropertySetter controller : controllers) {
			controller.unsetProperty(node, property, options);
		}
		
		// get dirty state after executing controllers
		boolean newDirty = CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNode.getFullNodeId(), getControllerInvocationOptions());
		if (oldDirty != newDirty) {			
			// dirty state changed -> change resourceNode isDirty property
			options.put(NODE_IS_RESOURCE_NODE, true);
			CorePlugin.getInstance().getNodeService().setProperty(resourceNode, IS_DIRTY, newDirty, options);
		}
	}
	
	public void addChild(Node node, Node child, Node insertBeforeNode) {		
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		Node resourceNode = CoreUtils.getRootNode(node);
		boolean oldDirty = CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNode.getFullNodeId(), getControllerInvocationOptions());
				
		List<AddNodeController> controllers = descriptor.getAdditiveControllers(ADD_NODE_CONTROLLER, node);
		for (AddNodeController controller : controllers) {
			controller.addNode(node, child, insertBeforeNode);
		}
		
		// get dirty state after executing controllers
		boolean newDirty = CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNode.getFullNodeId(), getControllerInvocationOptions());
		if (oldDirty != newDirty) {
			// dirty state changed -> change resourceNode isDirty property
			Map<String, Object> options = getControllerInvocationOptions();
			options.put(NODE_IS_RESOURCE_NODE, true);
			CorePlugin.getInstance().getNodeService().setProperty(resourceNode, IS_DIRTY, newDirty, options);
		}
		setProperty(node, HAS_CHILDREN, true, getControllerInvocationOptions());
	}
	
	public void removeChild(Node node, Node child) {	
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		// resourceNode can be modified after this operation, so store current dirty state before executing controllers
		Node resourceNode = CoreUtils.getRootNode(node);
		boolean oldDirty = CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNode.getFullNodeId(), getControllerInvocationOptions());
						
		List<RemoveNodeController> controllers = descriptor.getAdditiveControllers(REMOVE_NODE_CONTROLLER, node);
		for (RemoveNodeController controller : controllers) {
			controller.removeNode(node, child);
		}
		
		// get dirty state after executing controllers
		boolean newDirty = CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNode.getFullNodeId(), getControllerInvocationOptions());
		if (oldDirty != newDirty) {
			// dirty state changed -> change resourceNode isDirty property
			Map<String, Object> options = getControllerInvocationOptions();
			options.put(NODE_IS_RESOURCE_NODE, true);
			CorePlugin.getInstance().getNodeService().setProperty(resourceNode, IS_DIRTY, newDirty, options);
		}
		setProperty(node, HAS_CHILDREN, hasChildren(node), getControllerInvocationOptions());
	}
	
	/**
	 * Converts the registered {@link TypeDescriptor}s to {@link TypeDescriptorRemote}s that will be sent to the client.
	 * 
	 * @author Mariana Gheorghe
	 */
	public List<TypeDescriptorRemote> getRegisteredTypeDescriptors() {
		List<TypeDescriptorRemote> remotes = new ArrayList<TypeDescriptorRemote>();
		for (TypeDescriptor descriptor : registry.getRegisteredTypeDescriptors()) {
			// create the new remote type descriptor with the type and static categories
			TypeDescriptorRemote remote = new TypeDescriptorRemote(descriptor.getType(), descriptor.getCategories());
			
			// filter the single controllers map
			for (Entry<String, Pair<AbstractController, Boolean>> entry : descriptor.getSingleControllers().entrySet()) {
				if (entry.getValue().a instanceof IDescriptor) {
					remote.getSingleControllers().put(entry.getKey(), (IDescriptor) entry.getValue().a);
				}
			}
			
			// filter the additive controlers map
			for (Entry<String, Pair<List<? extends AbstractController>, Boolean>> entry : descriptor.getAdditiveControllers().entrySet()) {
				List<IDescriptor> additiveControllers = new ArrayList<IDescriptor>();
				for (AbstractController abstractController : entry.getValue().a) {
					if (abstractController instanceof IDescriptor) {
						additiveControllers.add((IDescriptor) abstractController);
					}
				}
				if (additiveControllers.size() > 0) {
					remote.getAdditiveControllers().put(entry.getKey(), additiveControllers);
				}
			}
			remotes.add(remote);
		}
		return remotes;
	}
	
	/**
	 * Internal method; shouldn't be called explicitly. It's invoked automatically by the {@link Node}.
	 */
	public void populateNodeProperties(Node node) {	
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
					
		List<PropertiesProvider> providers = descriptor.getAdditiveControllers(PROPERTIES_PROVIDER, node);
		Map<String, Object> options = getControllerInvocationOptions();
		for (PropertiesProvider provider : providers) {
			provider.populateWithProperties(node, options);
			if ((boolean) options.get(STOP_CONTROLLER_INVOCATION)) {
				break;
			}
		}
		
		node.getProperties().put(HAS_CHILDREN, hasChildren(node));
	}
	
	/**
	 * Internal method; shouldn't be called explicitly. It's invoked automatically by the {@link Node}.
	 */
	public Object getRawNodeData(Node node) {	
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
					
		RawNodeDataProvider<Object> rawNodeDataProvider = descriptor.getSingleController(RAW_NODE_DATA_PROVIDER, node);	
		if (rawNodeDataProvider == null) {
			return null;
		}
		return rawNodeDataProvider.getRawNodeData(node);	
	}
	
	public Map<String, Object> getControllerInvocationOptions() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(STOP_CONTROLLER_INVOCATION, false);
		options.put(NODE_IS_RESOURCE_NODE, false);
		return options;
	}
	
}
