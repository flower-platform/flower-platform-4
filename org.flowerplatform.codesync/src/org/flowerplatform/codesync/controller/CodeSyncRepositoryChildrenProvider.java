package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.CODESYNC;
import static org.flowerplatform.codesync.CodeSyncConstants.MDA;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Adds CodeSync nodes to the repository.
 * 
 * @author Mariana Gheorghe
 */
public class CodeSyncRepositoryChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		String repo = FileControllerUtils.getRepo(node);
		children.add(new Node(Utils.getUri(CODESYNC, repo), CODESYNC));
		children.add(new Node(Utils.getUri(MDA, repo), MDA));
		
		children.add(new Node(Utils.getUri("structureDiff", repo), "structureDiff"));
		
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
