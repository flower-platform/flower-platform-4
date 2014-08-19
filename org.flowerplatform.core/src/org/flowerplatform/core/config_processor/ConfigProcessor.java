package org.flowerplatform.core.config_processor;

import static org.flowerplatform.core.CoreConstants.CONFIG_NODE_PROCESSOR;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.TypeDescriptor;

public class ConfigProcessor {

	public void processConfigHierarchy(Node node, Object parentProcessedDataStructure) {
		// take the current node and create the instance; add it to parentProcessingDataStructure
		// get all controllers registered on this node
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return;
		}
		IConfigNodeProcessor processor = descriptor.getSingleController(CONFIG_NODE_PROCESSOR, node);
		// I need to know the dataStructure of the current node, in order to know where to add its children
		Object nodeProcessedDataStructure = processor.processConfigNode(node, parentProcessedDataStructure, new ServiceContext<>(CorePlugin.getInstance().getNodeService()));

		// parse all the children and run this recursion for each of them
		// in order to run this recursion on each of node's children, I need to have a reference to node's data structure
		// so after processing  a child -> an instance; where should I add it?
		List<Node> children = CorePlugin.getInstance().getNodeService().getChildren(node, new ServiceContext<>(CorePlugin.getInstance().getNodeService()));
		for(Node child : children){
			processConfigHierarchy(child, nodeProcessedDataStructure);
		}
	}
	
}
