package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MODEL_TREE_NODE_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Elena Posea
 *
 */
public class RegexTestFileChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		String repo = CoreUtils.getRepoFromNode(node);
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, REGEX_MODEL_TREE_NODE_TYPE, virtualNodeHandler.getTypeSpecificPartFromNodeUri(node.getNodeUri())),
				REGEX_MODEL_TREE_NODE_TYPE));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, REGEX_EXPECTED_MODEL_TREE_NODE_TYPE, virtualNodeHandler.getTypeSpecificPartFromNodeUri(node.getNodeUri())),
				REGEX_EXPECTED_MODEL_TREE_NODE_TYPE));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, REGEX_MATCHES_NODE_TYPE, virtualNodeHandler.getTypeSpecificPartFromNodeUri(node.getNodeUri())),
				REGEX_MATCHES_NODE_TYPE));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, REGEX_EXPECTED_MATCHES_NODE_TYPE, virtualNodeHandler.getTypeSpecificPartFromNodeUri(node.getNodeUri())),
				REGEX_EXPECTED_MATCHES_NODE_TYPE));
		
		return children;
		
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
