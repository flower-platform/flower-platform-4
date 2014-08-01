package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.CURRENT_BRANCH;
import static org.flowerplatform.team.git.GitConstants.CURRENT_COMMIT;
import static org.flowerplatform.team.git.GitConstants.IS_REPO;

import java.io.File;
import java.util.Map;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		
		try {
			Repository repo = null;
			String repoPath = Utils.getRepo(node.getNodeUri());
			repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
			
			if (repo != null) {
				node.getProperties().put(NAME,ResourcesPlugin.getInstance().getMessage("git.git"));
				node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/" + "git.gif"));
				node.getProperties().put(CURRENT_BRANCH, repo.getBranch());
				node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
				
				Map<String, org.eclipse.jgit.lib.Ref> refs = repo.getRefDatabase().getRefs(RefDatabase.ALL);
					
				for (Ref entry : refs.values()) {
					if (entry.getTarget().getName().equals(repo.getFullBranch())) {
						node.getProperties().put(CURRENT_COMMIT, entry.getObjectId().name());
						break;
					}
				}		
	
				String repositoryPath = GitUtils.getNodePath(node.getNodeUri());
				File repoFile;
				repoFile = (File) FileControllerUtils.getFileAccessController().getFile(repositoryPath);
				node.getProperties().put(IS_REPO, GitUtils.isRepository(repoFile));
			}
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		
	}
}
