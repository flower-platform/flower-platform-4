package org.flowerplatform.team.git.controller;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

import static org.flowerplatform.team.git.GitConstants.GIT_REPO_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_SCHEME;

/**
 * @author Cojocea Marius Eduard
 */
public class RepoChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		String repo = FileControllerUtils.getRepo(node) + "|" + GIT_REPO_TYPE;
		children.add(new Node(Utils.getUri(GIT_SCHEME, repo), GIT_REPO_TYPE));
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
