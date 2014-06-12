package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.freeplane.FreeplanePlugin.STYLE_ROOT_NODE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Sebastian Solomon
 */
public class StyleRootChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> list = new ArrayList<>(); 
		if (((NodeModel)node.getOrRetrieveRawNodeData()).getMap().getRootNode().equals(node.getOrRetrieveRawNodeData())) {
			Node styleNode = new Node(STYLE_ROOT_NODE , null, new Node(node.getResource()).getIdWithinResource(), null);
			list.add(styleNode);
		}
		return list;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return ((NodeModel)node.getOrRetrieveRawNodeData()).getMap().getRootNode().equals(node.getOrRetrieveRawNodeData());
	}

}
