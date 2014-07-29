package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.IS_REPO;

import java.io.File;

import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(NAME,ResourcesPlugin.getInstance().getMessage("git.git"));

		String repositoryPath = GitUtils.getNodePath(node.getNodeUri());
		File repoFile;
		try {
			repoFile = (File) FileControllerUtils.getFileAccessController().getFile(repositoryPath);
			node.getProperties().put(IS_REPO, GitUtils.isRepository(repoFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
