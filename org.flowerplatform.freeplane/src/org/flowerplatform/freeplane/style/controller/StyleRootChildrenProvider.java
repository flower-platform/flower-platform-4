package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.freeplane.FreeplanePlugin.MIND_MAP_STYLE;
import static org.flowerplatform.freeplane.FreeplanePlugin.STYLE_ROOT_NODE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.styles.MapStyleModel;

/**
 * @author Sebastian Solomon
 */
public class StyleRootChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> list = new ArrayList<>(); 
		NodeModel model = (NodeModel) node.getRawNodeData();
		if ((model.getMap().getRootNode().equals(node.getRawNodeData()))) {
			String styleNodeUri = Utils.getUri(MIND_MAP_STYLE, Utils.getSchemeSpecificPart(node.getNodeUri()));
			Node styleNode = new Node(styleNodeUri, STYLE_ROOT_NODE);
			styleNode.setRawNodeData(MapStyleModel.getExtension(model.getMap()).getStyleMap().getRootNode());
			list.add(styleNode);
		}
		return list;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return ((NodeModel)node.getRawNodeData()).getMap().getRootNode().equals(node.getRawNodeData());
	}

}
