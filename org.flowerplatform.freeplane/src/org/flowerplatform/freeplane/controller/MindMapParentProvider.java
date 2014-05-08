package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ParentProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Mariana Gheorghe
 */
public class MindMapParentProvider extends ParentProvider {

	@Override
	public Node getParent(Node node, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());		
		NodeModel parentNodeModel = rawNodeData.getParentNode();
		if (parentNodeModel == null) {
			return null;
		}
		return FreeplanePlugin.getInstance().getStandardNode(parentNodeModel, node.getResource());
	}

}
