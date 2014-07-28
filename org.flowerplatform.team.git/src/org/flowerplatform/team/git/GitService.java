package org.flowerplatform.team.git;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
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
			List<DiffEntry> l = git.diff().setShowNameAndStatusOnly(true).call();
			git.branchDelete().setForce(true).setBranchNames(branchName).call();
	}
	public List<Node> stagingList(String repositoryPath, String stagingType) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo); 
		if(stagingType.equals("unstaged")) {
			List<DiffEntry> unstagedDiffs = git.diff().setShowNameAndStatusOnly(true).call();
			List<Node> unstagedNodes = new ArrayList<Node>();
			for (DiffEntry obj:unstagedDiffs){
				if(obj.getChangeType().name().equals("DELETE")) {
					Node node = new Node(obj.getOldPath(), obj.getChangeType().name());
					unstagedNodes.add(node);
				} else {
					Node node = new Node(obj.getNewPath(), obj.getChangeType().name());
					unstagedNodes.add(node);
				}
			}
			return unstagedNodes;
		}
		else
		{
			List<DiffEntry> stagedDiffs = git.diff().setCached(true).call();
			List<Node> stagedNodes = new ArrayList<Node>();
			for (DiffEntry obj:stagedDiffs){
				if(obj.getChangeType().name().equals("DELETE")) {
					Node node = new Node(obj.getOldPath(), obj.getChangeType().name());
					stagedNodes.add(node);
				} else {
					Node node = new Node(obj.getNewPath(), obj.getChangeType().name());
					stagedNodes.add(node);
				}
			}
			return stagedNodes;
		}
	}

	public String amendMethod(String repositoryPath, boolean ok) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		RevWalk rw = new RevWalk(repo);
		ObjectId headId = repo.resolve(Constants.HEAD + "^{commit}"); //$NON-NLS-1$
		if (headId == null && ok)
			return null;
		List<ObjectId> parents = new ArrayList<ObjectId>();
		if (headId != null)
			if (ok) {
				RevCommit previousCommit = rw.parseCommit(headId);
				for (RevCommit p : previousCommit.getParents()) {
					parents.add(p.getId());
				}
				rw.dispose();
				if (ok) {
					return previousCommit.getFullMessage();
				}
			} else
				return null;
		return null;
	}
	public void commitMethod(String repositoryPath, boolean ok, String message) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		git.commit().setMessage(message).setAmend(ok).call();
	}
	public List<String> authorAndCommiter(String repositoryPath) throws Exception {
		List<String> list = new ArrayList<String>();
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		PersonIdent pi = new PersonIdent(repo);
		list.add(pi.getName() + " <" + pi.getEmailAddress() + ">");
		return list;
	}
	public void addToGitIndex(String repositoryPath, String filePathToAdd) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		git.add().addFilepattern(filePathToAdd).call();
	}
	public void removeFromGitIndex(String repositoryPath, String filePathToRemove) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		git.reset().addPath(filePathToRemove).call();
	}
}
