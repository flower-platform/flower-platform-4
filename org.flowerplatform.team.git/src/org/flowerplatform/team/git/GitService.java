package org.flowerplatform.team.git;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.URIish;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.util.Utils;

/**
 * @author Valentina-Camelia Bojan
 */
public class GitService {
	
	private static final int NETWORK_TIMEOUT_MSEC = 15000;
	
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
	
	public int validateRepoURL(String url, String path) {
		try {
			URIish repoUri = new URIish(url.trim());
			if (repoUri.getScheme().toLowerCase().startsWith("http") ) {
				InputStream ins = null;
				URLConnection conn = new URL(repoUri.toString()).openConnection();
			    conn.setReadTimeout(NETWORK_TIMEOUT_MSEC);
			    ins = conn.getInputStream();
		    } 
			String repoName = new URIish(url.trim()).getHumanishName();
			File gitReposFile = new File(repoName);
			
			if (GitUtils.getGitDir(gitReposFile) != null) {
				return 1;
			}
		}
		catch(Exception ex) {
			return -1;
		}
		return 0;  
	}
	
	public void getBranches() {
		
	}

	public void cloneRepo(String uri, ArrayList<String> branches, boolean cloneAll, String path) {
		CloneCommand cc = new CloneCommand();
		try {
			cc.setCloneAllBranches(cloneAll);
			cc.setBranchesToClone(branches);
			cc.setURI(uri);
			File directory = new File(path);
			cc.setDirectory(directory);
			cc.call();
		} 
		catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}