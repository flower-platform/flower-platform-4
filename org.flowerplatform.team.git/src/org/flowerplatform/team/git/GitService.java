package org.flowerplatform.team.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.BranchConfig;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
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
	
	public void configureBranch(Repository repo, String branchName, String remote, String upstream){
		
		try {
			//TODO: repo must be a String; get the Repository using getRepo(node.Uri) from Utils.java
			
			Ref branchReference = repo.getRef(branchName); 
			
			StoredConfig config = repo.getConfig();
			
			config.save();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}