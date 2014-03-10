package org.flowerplatform.freeplane.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapChildrenProvider extends ChildrenProvider {
			
	@Override
	public List<Node> getChildren(Node node, Map<String, Object> options) {
		NodeModel nodeModel = (NodeModel) node.getOrRetrieveRawNodeData();
		if (CorePlugin.RESOURCE_TYPE.equals(node.getType())) {
			return Collections.singletonList(FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(nodeModel));
		}
		List<Node> children = new ArrayList<Node>();		
		for (NodeModel childNodeModel : nodeModel.getChildren()) {
			children.add(FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(childNodeModel));
		}
		return children;		
	}

	@Override
	public boolean hasChildren(Node node, Map<String, Object> options) {
		NodeModel nodeModel = (NodeModel) node.getOrRetrieveRawNodeData();
		return nodeModel.hasChildren();
	}

}
