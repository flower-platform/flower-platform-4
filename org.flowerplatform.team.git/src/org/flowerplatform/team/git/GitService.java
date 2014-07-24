package org.flowerplatform.team.git;

import java.io.File;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.revwalk.RevWalk;
import org.flowerplatform.core.file.FileControllerUtils;
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
	
	public void deleteGitRepository(String nodeUri, Boolean keepWorkingDirectoryContent) throws Exception {
		int index = nodeUri.indexOf("|");
		if (index < 0) {
			index = nodeUri.length();
		}
		String repoPath = nodeUri.substring(nodeUri.indexOf(":") + 1, index);
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
		repo.close();
		RepositoryCache.close(repo);
	}

}