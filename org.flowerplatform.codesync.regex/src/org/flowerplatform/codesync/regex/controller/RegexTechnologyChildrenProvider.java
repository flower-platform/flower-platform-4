package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEXES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILES_NODE_TYPE;

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
public class RegexTechnologyChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		String repo = CoreUtils.getRepoFromNode(node);
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, REGEXES_NODE_TYPE, virtualNodeHandler.getTypeSpecificPartFromNodeUri(node.getNodeUri())), REGEXES_NODE_TYPE));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, REGEX_TEST_FILES_NODE_TYPE, virtualNodeHandler.getTypeSpecificPartFromNodeUri(node.getNodeUri())),
				REGEX_TEST_FILES_NODE_TYPE));
		
		return children;
		
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
