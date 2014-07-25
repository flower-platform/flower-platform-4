package org.flowerplatform.team.git;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Parameter;

import org.eclipse.egit.core.op.ResetOperation;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.codesync.sdiff.IFileContentProvider;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.remote.Node;


/**
 * @author Valentina-Camelia Bojan
 */

public class GitService {
	
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
	/**
	 * @author Marius Iacob
	 */
	public void deleteBranch(String repositoryPath, String branchName) throws Exception {
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
			Git git = new Git(repo);
			git.branchDelete().setForce(true).setBranchNames(branchName).call();
	}
	
	/**
	 * @author Diana Balutoiu
	 * @return true if the reset was successful
	 * @param nodeUri - the Uri of the selected branch
	 */
	public boolean reset(String nodeUri, int type) throws Exception {
		
		int index = nodeUri.indexOf("|");
		if (index < 0) {
			index = nodeUri.length();
		}
		String repositoryPath = nodeUri.substring(nodeUri.indexOf(":") + 1, index);
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath) );
		
		ResetType resetType;
		if(type == 0){
			resetType = ResetType.SOFT;
		}
		else if(type == 2){
			resetType = ResetType.MIXED;
		}
		else{
			resetType = ResetType.HARD;
		}
		
		String refName = nodeUri.substring(nodeUri.indexOf("$") + 1, nodeUri.length());

		new ResetOperation(repo, refName, resetType);
		//TODO: when and where does it fail?!?
		return true;
	}
}
