package org.flowerplatform.team.git;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.codesync.sdiff.IFileContentProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Utils;


import org.flowerplatform.core.CoreConstants.*;
/**
 * @author Valentina-Camelia Bojan
 */

public class GitService {
	
	private static final int NETWORK_TIMEOUT_MSEC = 15000;
	
	public Node createStructureDiffFromGitCommits(String oldHash, String newHash, String repoPath, String sdiffOutputPath) {
		IFileContentProvider fileContentProvider = new GitFileContentProvider(newHash, oldHash, repoPath);
		OutputStream patch = new ByteArrayOutputStream();

		// get the patch for the two commits
		try {
			Repository repository = GitUtils.getRepository((File) FileControllerUtils
											.getFileAccessController()
											.getFile(repoPath));
			Git git = new Git(repository);
			RevWalk revWalk = new RevWalk(repository);
			ObjectReader reader = repository.newObjectReader();
			CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
			oldTreeIter.reset(reader, revWalk.parseCommit(repository.resolve(oldHash)).getTree());
			CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
			newTreeIter.reset(reader, revWalk.parseCommit(repository.resolve(newHash)).getTree());

			git.diff().setOutputStream(patch).setNewTree(newTreeIter).setOldTree(oldTreeIter).call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return CodeSyncSdiffPlugin.getInstance().getSDiffService().createStructureDiff(patch.toString(), repoPath, sdiffOutputPath, fileContentProvider);
	}
	
	public boolean validateHash(String hash, String repositoryPath) {				
		try {
			// testing if hash is valid
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
			if (repo == null) {
				return false;
			}
			
			ObjectId resolved = repo.resolve(hash);
			if (resolved == null) {
				return false;
			}
			
			// testing if hash exists in the repository
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
		Repository repository; 

//        System.out.println("Listing local branches:");
		try {
			repository = new FileRepository(new File("/tmp"));
	        List<Ref> call;
//	        call = new Git(repository).branchList().call();
//	        for (Ref ref : call) {
//	            System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
//	        }

        	System.out.println("Now including remote branches:");
        
			call = new Git(repository).branchList().setListMode(ListMode.ALL).call();
			for (Ref ref : call) {
	            System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
	        }
			repository.close();
		} 
		catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cloneRepo(String nodeUri, String repoUri, ArrayList<String> branches, boolean cloneAll, String path) {
		CloneCommand cc = new CloneCommand();
		try {
			cc.setCloneAllBranches(cloneAll);
			cc.setBranchesToClone(branches);
			cc.setURI(repoUri);
			URIish urish = new URIish(repoUri.trim());
			String repoName = urish.getHumanishName();
			File directory = new File(CoreConstants.REPO_ROOT + Utils.getRepo(nodeUri) + repoName);
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
		catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @author Marius Iacob
	 */
	public void deleteBranch(String repositoryPath, String branchName) throws Exception {
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
			Git git = new Git(repo);
			git.branchDelete().setForce(true).setBranchNames(branchName).call();
	}
}