package org.flowerplatform.team.git;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.remote.Node;

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
	 * @author Cristina Brinza
	 */
	
	/**
	 *  Get all branches from a certain repository
	 *  
	 */
	public ArrayList<Node> getBranches(Node repositoryNode) {
		return null;
	}
	
	/**
	 * Creates new branch
	 *  
	 */
	public void createBranch(Node repositoryNode, String name, boolean track, boolean setUpstream, boolean checkoutBranch) {
		
	}
	

}