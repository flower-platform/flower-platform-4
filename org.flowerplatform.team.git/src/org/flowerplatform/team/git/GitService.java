package org.flowerplatform.team.git;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;


/**
 * @author Valentina-Camelia Bojan
 */

public class GitService {
	
	public boolean validateHash(String hash, String repositoryPath) {				
		try {
			//testing if hash is valid
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
			if(repo == null)
				return false;
			
			ObjectId resolved = repo.resolve(hash);
			if(resolved == null)
				return false;
			
			//testing if hash exists in the repository
			RevWalk rw = new RevWalk(repo);
			rw.parseCommit(resolved);
		
		} catch (Exception e) {
			return false;
		}
		return true;		
	}
	/**
	 * @author Marius Iacob
	 */
	public void deleteBranch(String parentUri, String childUri) throws Exception {
			Node childNode = CorePlugin.getInstance().getResourceService().getNode(childUri);
			Node parentNode = CorePlugin.getInstance().getResourceService().getNode(parentUri);
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(childUri)));
			Git git = new Git(repo);
			git.branchDelete().setForce(true).setBranchNames(childNode.getPropertyValue(GitConstants.NAME).toString()).call();
			CorePlugin.getInstance().getNodeService().removeChild(parentNode, childNode, new ServiceContext<NodeService>());
	}
}