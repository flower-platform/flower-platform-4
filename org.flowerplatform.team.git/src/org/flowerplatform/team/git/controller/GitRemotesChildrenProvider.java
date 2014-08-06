package org.flowerplatform.team.git.controller;

import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_SCHEME;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitRemotesChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		Repository repo = null;
		String repoPath = Utils.getRepo(node.getNodeUri());
		try {
			repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<String> remotes = repo.getRemoteNames();
		
		for (String entry : remotes) {
			String path = repoPath + "|" + GIT_REMOTE_TYPE + "$" + entry;
			children.add(CorePlugin.getInstance().getResourceService().getResourceHandler(GIT_SCHEME)
					.createNodeFromRawNodeData(Utils.getUri(GIT_SCHEME, path), entry));
		}
		return children; 
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		if (getChildren(node, context).size() > 0) {
			return true;
		}
		return false;
	}

}
