package org.flowerplatform.team.git;

import java.util.List;
import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.flowerplatform.core.file.FileControllerUtils;



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
	 * @author Tita Andreea
	 */
	
	/* rename the branch with the new name */
	public void renameBranch(String pathNode,String oldName,String newName) throws Exception{
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(pathNode));
		Git gitInstance = new Git(repo);
		/* set the new name */
		gitInstance.branchRename().setOldName(oldName).setNewName("origin/" + newName).call();
		/* refresh File System node */ //TODO
		//CorePlugin.getInstance().getNodeService().setProperty(node,"name",newName,new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));	
	}
	
}