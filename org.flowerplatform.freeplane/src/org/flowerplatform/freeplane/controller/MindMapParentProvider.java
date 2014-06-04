package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IParentProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Mariana Gheorghe
 */
public class MindMapParentProvider extends AbstractController implements IParentProvider {

	@Override
	public Node getParent(Node node, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) node.getRawNodeData());		
		NodeModel parentNodeModel = rawNodeData.getParentNode();
		if (parentNodeModel == null) {
			return null;
		}
		return FreeplanePlugin.getInstance().getStandardNode(parentNodeModel, node.getResource());
	}

}
