package org.flowerplatform.freeplane.controller;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapChildrenProvider extends ChildrenProvider {
			
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		NodeModel nodeModel = (NodeModel) node.getOrRetrieveRawNodeData();
		List<Node> children = new ArrayList<Node>();		
		for (NodeModel childNodeModel : nodeModel.getChildren()) {
			children.add(FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(childNodeModel, node.getResource()));
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		NodeModel nodeModel = (NodeModel) node.getOrRetrieveRawNodeData();
		return nodeModel.hasChildren();
	}

}
