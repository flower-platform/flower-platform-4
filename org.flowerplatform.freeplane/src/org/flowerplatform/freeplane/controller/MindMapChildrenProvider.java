package org.flowerplatform.freeplane.controller;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceHandler;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapChildrenProvider extends AbstractController implements IChildrenProvider {
			
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		NodeModel nodeModel = (NodeModel) node.getRawNodeData();
		List<Node> children = new ArrayList<Node>();		
		for (NodeModel childNodeModel : nodeModel.getChildren()) {
			String scheme = Utils.getScheme(node.getNodeUri());
			String ssp = Utils.getSchemeSpecificPart(node.getNodeUri());
			String childUri = Utils.getUri(scheme, ssp, childNodeModel.createID());
			Node child = new Node(childUri);
			ResourceHandler resourceHandler = CorePlugin.getInstance().getResourceService()
					.getResourceHandler(scheme);
			child.setType(resourceHandler.getType(childNodeModel, childUri));
			child.setRawNodeData(childNodeModel);
			children.add(child);
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		NodeModel nodeModel = (NodeModel) node.getRawNodeData();
		return nodeModel.hasChildren();
	}

}
