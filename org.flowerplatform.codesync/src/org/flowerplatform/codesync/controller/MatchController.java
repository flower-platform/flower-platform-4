package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_MODIFIED_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_MODIFIED_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_FEATURE;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_ID_SEPARATOR;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_TYPE;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.Match;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class MatchController extends AbstractController implements IPropertiesProvider, IChildrenProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String fragment = Utils.getFragment(node.getNodeUri());
		int index = fragment.lastIndexOf(MATCH_ID_SEPARATOR);
		if (index >= 0) {
			fragment = fragment.substring(index + 1);
		}
		node.getProperties().put(NAME, fragment);
		
		Match match = (Match) node.getRawNodeData();
		node.getProperties().put(MATCH_TYPE, match.getMatchType());
		node.getProperties().put(MATCH_FEATURE, match.getFeature());
		node.getProperties().put(MATCH_CHILDREN_MODIFIED_LEFT, match.isChildrenModifiedLeft());
		node.getProperties().put(MATCH_CHILDREN_MODIFIED_RIGHT, match.isChildrenModifiedRight());
		node.getProperties().put(MATCH_CHILDREN_CONFLICT, match.isChildrenConflict());
		node.getProperties().put(MATCH_DIFFS_MODIFIED_LEFT, match.isDiffsModifiedLeft());
		node.getProperties().put(MATCH_DIFFS_MODIFIED_RIGHT, match.isDiffsModifiedRight());
		node.getProperties().put(MATCH_DIFFS_CONFLICT, match.isDiffsConflict());
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		Match match = (Match) node.getRawNodeData();
		List<Node> children = new ArrayList<Node>();
		IResourceHandler handler = CorePlugin.getInstance().getResourceService().getResourceHandler(Utils.getScheme(node.getNodeUri()));
		String baseUri = node.getNodeUri() + MATCH_ID_SEPARATOR;
		for (Match subMatch : match.getSubMatches()) {
			String childUri = baseUri + subMatch.getMatchKey();
			Node child = handler.createNodeFromRawNodeData(childUri, subMatch);
			children.add(child);
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return ((Match) node.getRawNodeData()).getSubMatches().size() > 0;
	}

}
