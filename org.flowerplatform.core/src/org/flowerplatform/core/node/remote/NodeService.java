package org.flowerplatform.core.node.remote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.node.NodeTypeDescriptor;
import org.flowerplatform.core.node.NodeTypeDescriptorRegistry;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.RunnableWithParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Spiescu
 */
public class NodeService {
	
	private final static Logger logger = LoggerFactory.getLogger(NodeService.class);
	
	protected NodeTypeDescriptorRegistry registry;
	
	public NodeService(NodeTypeDescriptorRegistry registry) {
		super();
		this.registry = registry;
	}

	public List<Node> getChildren(Node node, boolean populateProperties) {
		NodeTypeDescriptor descriptor = registry.getExpectedNodeTypeDescriptor(node.getType());
		if (descriptor == null) {
			return null;
		}
		
		List<ChildrenProvider> providers = registry.getControllersForTypeAndCategories(descriptor, new RunnableWithParam<List<ChildrenProvider>, NodeTypeDescriptor>() {
			@Override
			public List<ChildrenProvider> run(NodeTypeDescriptor param) {
				return param.getChildrenProviders();
			}
		});
		
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
		NodeTypeDescriptor descriptor = registry.getExpectedNodeTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		
		List<PropertiesProvider<?>> providers = registry.getControllersForTypeAndCategories(descriptor, new RunnableWithParam<List<PropertiesProvider<?>>, NodeTypeDescriptor>() {
			@Override
			public List<PropertiesProvider<?>> run(NodeTypeDescriptor param) {
				return param.getPropertiesProviders();
			}
		});

		for (PropertiesProvider<?> provider : providers) {
			((PropertiesProvider<Object>) provider).populateWithProperties(node, rawNodeData);
		}
	}
	
	public void setProperty(Node node, String property, Object value) {
		
	}
	
	public void addChild(Node node, Node child) {
		
	}
	
	public void removeChild(Node node, Node child) {
		
	}
}
