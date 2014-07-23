package org.flowerplatform.team.git;

import java.io.File;
import java.util.ArrayList;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.flowerplatform.core.file.FileControllerUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RenameBranchCommand;


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

	/* get all names of branches from repository */
	public ArrayList<String> getAllNamesOfBranches(String repoPath){
		return null;
	}

	
	/* rename the branch with the new name */
	public void renameBranch(String oldName,String newName,String repoPath){
		try {
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
			Git gitInstance = new Git(repo);
			RenameBranchCommand renameBranch = gitInstance.branchRename();
			renameBranch.setOldName(oldName);
			renameBranch.setNewName(newName);
			renameBranch.call();
		} catch (Exception e) {	
			
		}
	}
}