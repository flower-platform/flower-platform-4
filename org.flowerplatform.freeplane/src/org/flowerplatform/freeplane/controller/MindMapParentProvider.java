package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.ParentProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Mariana Gheorghe
 */
public class MindMapParentProvider extends ParentProvider {

	@Override
	public Node getParent(Node node) {
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());		
		NodeModel parentNodeModel = rawNodeData.getParentNode();
		if (parentNodeModel == null) {
			return null;
		}
		return FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(parentNodeModel, node.getResource());
	}

}
