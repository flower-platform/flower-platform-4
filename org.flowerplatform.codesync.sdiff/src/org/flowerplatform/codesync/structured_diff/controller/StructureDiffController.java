package org.flowerplatform.codesync.structured_diff.controller;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.Match;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class StructureDiffController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(NAME, "Structured Diff");
		@SuppressWarnings("unchecked")
		List<Pair<String, String>> subscribableResources = (List<Pair<String, String>>) 
				node.getProperties().get(SUBSCRIBABLE_RESOURCES);
		if (subscribableResources == null) {
			subscribableResources = new ArrayList<Pair<String, String>>();
			node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
		}
		Pair<String, String> subscribableResource = new Pair<String, String>(node.getNodeUri(), "mindmap");
		subscribableResources.add(0, subscribableResource);
		
		node.getProperties().put(CoreConstants.USE_NODE_URI_ON_NEW_EDITOR, true);
		node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		@SuppressWarnings("unchecked")
		Map<String, Match> matches = (Map<String, Match>) node.getRawNodeData();
		List<Node> children = new ArrayList<Node>();
		IResourceHandler handler = CorePlugin.getInstance().getResourceService().getResourceHandler(Utils.getScheme(node.getNodeUri()));
		String baseUri = node.getNodeUri() + "#";
		for (Map.Entry<String, Match> entry : matches.entrySet()) {
			String name = entry.getKey();
			Match match = entry.getValue();
			String childUri = baseUri + name;
			Node child = handler.createNodeFromRawNodeData(childUri, match);
			children.add(child);
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
