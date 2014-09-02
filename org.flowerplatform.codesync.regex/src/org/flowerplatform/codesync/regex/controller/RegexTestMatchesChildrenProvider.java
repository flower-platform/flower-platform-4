package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IController;

public class RegexTestMatchesChildrenProvider extends AbstractController implements IChildrenProvider, IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(CoreConstants.NAME, "Matches");
		String resourceUri = getResourceUri(node);
		String contentType = "mindmap";

		@SuppressWarnings("unchecked")
		List<Pair<String, String>> subscribableResources = (List<Pair<String, String>>) node.getProperties().get(SUBSCRIBABLE_RESOURCES);
		if (subscribableResources == null) {
			subscribableResources = new ArrayList<Pair<String, String>>();
			node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
		}
		Pair<String, String> subscribableResource = new Pair<String, String>(resourceUri, contentType);
		subscribableResources.add(0, subscribableResource);

		node.getProperties().put(CoreConstants.USE_NODE_URI_ON_NEW_EDITOR, true);
		node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getNode(getResourceUri(node));
		return context.getService().getChildren(resourceNode, context);
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

	protected String getResourceUri(Node node) {
		// Node parent = CorePlugin.getInstance().getNodeService().getParent(node, new ServiceContext<NodeService>());
		String repo = CoreUtils.getRepoFromNode(node);
		String testFilePath = CorePlugin.getInstance().getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
		return CoreUtils.createNodeUriWithRepo("fpp", repo, CodeSyncRegexConstants.REGEX_CONFIGS_FOLDER + "/" + testFilePath);
		// fpp:elena/repo1|.regex-configs/ActionScript/match-files/....as.match
	}

}
