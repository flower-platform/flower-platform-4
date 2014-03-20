package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.node.NodeService.STOP_CONTROLLER_INVOCATION;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Mariana Gheorghe
 */
public class FreeplaneResourceChildrenProvider extends ChildrenProvider {

	public FreeplaneResourceChildrenProvider() {
		super();
		// children controller must be invoked before the file children provider
		setOrderIndex(-10000);
	}
	
	@Override
	public List<Node> getChildren(Node node, Map<String, Object> options) {
		if (!CorePlugin.getInstance().getNodeService().isSubscribable(node.getOrPopulateProperties())) {
			return Collections.emptyList();
		}
		
		NodeModel nodeModel = (NodeModel) node.getOrRetrieveRawNodeData();
		List<Node> children = Collections.singletonList(FreeplanePlugin.getInstance().getFreeplaneUtils()
				.getStandardNode(nodeModel, node.getFullNodeId()));
		options.put(STOP_CONTROLLER_INVOCATION, true);
		return children;
	}

	@Override
	public boolean hasChildren(Node node, Map<String, Object> options) {
		return CorePlugin.getInstance().getNodeService().isSubscribable(node.getProperties());
	}
	
}
