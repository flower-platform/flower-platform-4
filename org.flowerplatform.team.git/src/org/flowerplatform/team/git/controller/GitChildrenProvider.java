package org.flowerplatform.team.git.controller;

import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_SCHEME;
import static org.flowerplatform.team.git.GitConstants.GIT_TAGS_TYPE;

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
 * @author Cojocea Marius Eduard
 */
public class GitChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();

		String repo = Utils.getRepo(node.getNodeUri());
		children.add(new Node(Utils.getUri(GIT_SCHEME, repo + "|" + GIT_LOCAL_BRANCHES_TYPE), GIT_LOCAL_BRANCHES_TYPE));
		children.add(new Node(Utils.getUri(GIT_SCHEME, repo + "|" + GIT_REMOTE_BRANCHES_TYPE), GIT_REMOTE_BRANCHES_TYPE));
		children.add(new Node(Utils.getUri(GIT_SCHEME, repo + "|" + GIT_TAGS_TYPE), GIT_TAGS_TYPE));
		children.add(new Node(Utils.getUri(GIT_SCHEME, repo + "|" + GIT_REMOTES_TYPE), GIT_REMOTES_TYPE));

		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
