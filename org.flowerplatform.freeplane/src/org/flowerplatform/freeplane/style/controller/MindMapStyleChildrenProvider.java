package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.freeplane.FreeplanePlugin.MIND_MAP_STYLE;

import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.styles.MapStyleModel;


/**
 * @author Sebastian Solomon
 */
public class MindMapStyleChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> serviceContext) {
		// idWithinResource == null -> path to workspace location
		NodeModel nodeModel = (NodeModel) node.getRawNodeData();
		
		Enumeration<NodeModel> styles = MapStyleModel.getExtension(nodeModel.getMap()).getStyleMap().getRootNode().children();
		if (styles == null) {	
			return null;
		}
		List<Node> children = new ArrayList<Node>();
		while (styles.hasMoreElements()) {
			for (NodeModel styleNodeModel : styles.nextElement().getChildren()) {
				URI styleNodeUri = Utils.getUriWithFragment(Utils.getUri(node.getNodeUri()), styleNodeModel.createID());
				Node child = new Node(Utils.getString(styleNodeUri));
				child.setType(MindMapConstants.MINDMAP_NODE_TYPE);
				child.setRawNodeData(styleNodeModel);
				children.add(child);
			}
		}		
		return children;
	}
	
	
	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> serviceContext) {
		if (node.getType().equals(MIND_MAP_STYLE)) {
			return false;
		}
		return true;
	}

}
