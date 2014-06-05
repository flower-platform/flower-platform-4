package org.flowerplatform.freeplane.controller;

import java.net.URI;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IParentProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceHandler;
import org.flowerplatform.util.Utils;
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
		URI resourceUri = Utils.getUriWithoutFragment(node.getNodeUri());
		String parentUri = Utils.getUriWithFragment(node.getNodeUri(), parentNodeModel.createID());
		ResourceHandler resourceHandler = CorePlugin.getInstance().getResourceService()
				.getResourceHandler(resourceUri.getScheme());
		return resourceHandler.getNode(Utils.getUri(parentUri));
	}

}
