package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.RootNodeProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapRootNodeProvider extends RootNodeProvider {

	@Override
	public Node getRootNode(Node node) {
		NodeModel nodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getIdWithinResource());
		if (nodeModel != null) {
			return FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(nodeModel.getMap().getRootNode());
		}
		return null;
	}

}
