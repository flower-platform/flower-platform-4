package org.flowerplatform.team.git;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevWalk;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;

import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.codesync.sdiff.IFileContentProvider;
import org.flowerplatform.core.file.FileControllerUtils;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

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

	
	public void configureBranch(String branchNodeUri, String remote, String upstream) throws Exception{
		
			Node branchNode = CorePlugin.getInstance().getResourceService().getNode(branchNodeUri);
			String branchName = (String)branchNode.getPropertyValue(CoreConstants.NAME);
			int index = branchNodeUri.indexOf("|");
			if (index < 0) {
				index = branchNodeUri.length();
			}
			String repositoryPath = branchNodeUri.substring(branchNodeUri.indexOf(":") + 1, index);
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath) );
			
			//get the .git/config file
			StoredConfig config = repo.getConfig();
			
			config.setString("branch", branchName, "remote", remote);
			config.setString("branch", branchName, "merge", upstream);
			config.save();
			
			CorePlugin.getInstance().getNodeService().setProperty(branchNode, GitConstants.CONFIG_REMOTE, 
					remote,  new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
			CorePlugin.getInstance().getNodeService().setProperty(branchNode, GitConstants.CONFIG_UPSTREAM_BRANCH, 
					upstream, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
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
