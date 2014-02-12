package org.flowerplatform.freeplane.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.util.Pair;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicChildrenProvider extends ChildrenProvider {
			
	@Override
	public List<Pair<Node, Object>> getChildren(Node node) {
		NodeModel nodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
		if (node.getId() == null) {
			return Collections.singletonList(new Pair<Node, Object>(FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(nodeModel), nodeModel));
		}
		List<Pair<Node, Object>> children = new ArrayList<Pair<Node, Object>>();		
		for (NodeModel child : nodeModel.getChildren()) {
			children.add(new Pair<Node, Object>(FreeplanePlugin.getInstance().getFreeplaneUtils().getStandardNode(child), child));
		}
			
		return children;		
	}

}
