package org.flowerplatform.team.git;

import static org.flowerplatform.team.git.GitConstants.ADD;
import static org.flowerplatform.team.git.GitConstants.CONFLICTED;
import static org.flowerplatform.team.git.GitConstants.DELETE;
import static org.flowerplatform.team.git.GitConstants.FILE;
import static org.flowerplatform.team.git.GitConstants.MODIFY;
import static org.flowerplatform.team.git.GitConstants.STAGED;
import static org.flowerplatform.team.git.GitConstants.STAGE_ADDED;
import static org.flowerplatform.team.git.GitConstants.STAGE_REMOVED;
import static org.flowerplatform.team.git.GitConstants.UNSTAGED;
import static org.flowerplatform.team.git.GitConstants.UNTRACKED;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.diff.DiffEntry;
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
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.resources.ResourcesPlugin;

/**
 * @author Valentina-Camelia Bojan
 */

public class GitService {

	public Node createStructureDiffFromGitCommits(String oldHash, String newHash, String repoPath, String sdiffOutputPath) {
		IFileContentProvider fileContentProvider = new GitFileContentProvider(newHash, oldHash, repoPath);
		OutputStream patch = new ByteArrayOutputStream();

		// get the patch for the two commits
		try {
			Repository repository = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
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

	public List<Node> stagingList(String repositoryPath, String stagingType) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		Set<String> conflictList = git.status().call().getConflicting();
		boolean ok = false;
		String type = null;

		if (stagingType.equals("unstaged")) {
			List<DiffEntry> unstagedDiffs = git.diff().setShowNameAndStatusOnly(true).call();
			List<Node> unstagedNodes = new ArrayList<Node>();
			for (String currentConflict : conflictList) {
				for (DiffEntry obj : unstagedDiffs) {
					if ((obj.getNewPath().equals(currentConflict) || obj.getOldPath().equals(currentConflict))) {
						ok = true;
						type = obj.getChangeType().name();
					}
				}
				if (ok) {
					Node node = new Node(currentConflict, type);
					node.getProperties().put(
							CoreConstants.ICONS,
							CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl(FILE),
							ResourcesPlugin.getInstance().getResourceUrl(CONFLICTED)));
					unstagedNodes.add(node);
					ok = false;
				}
			}
			for (DiffEntry obj : unstagedDiffs) {
				ok = false;
				for (Node currentNode : unstagedNodes) {
					if ((obj.getNewPath().equals(currentNode.getNodeUri()) || obj.getOldPath().equals(currentNode.getNodeUri()))) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					if (obj.getChangeType().name().equals(DELETE)) {
						Node node = new Node(obj.getOldPath(), obj.getChangeType().name());
						node.getProperties().put(
								CoreConstants.ICONS,
								CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl(FILE),
								ResourcesPlugin.getInstance().getResourceUrl(STAGE_REMOVED)));
						unstagedNodes.add(node);

					} else if (obj.getChangeType().name().equals(ADD)) {
						Node node = new Node(obj.getNewPath(), obj.getChangeType().name());
						node.getProperties().put(
								CoreConstants.ICONS,
								CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl(FILE),
								ResourcesPlugin.getInstance().getResourceUrl(UNTRACKED)));
						unstagedNodes.add(node);

					} else if (obj.getChangeType().name().equals(MODIFY)) {
						Node node = new Node(obj.getNewPath(), obj.getChangeType().name());
						node.getProperties().put(
								CoreConstants.ICONS,
								CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl(FILE),
								ResourcesPlugin.getInstance().getResourceUrl(UNSTAGED)));
						unstagedNodes.add(node);
					}
				}
			}
			return unstagedNodes;
		} else {
			List<DiffEntry> stagedDiffs = git.diff().setCached(true).call();
			Status s = git.status().call();

			Set<String> totalList = new HashSet<String>();
			totalList.addAll(s.getAdded());
			totalList.addAll(s.getChanged());
			totalList.addAll(s.getRemoved());

			List<Node> stagedNodes = new ArrayList<Node>();
			for (String stage : totalList) {
				for (DiffEntry obj : stagedDiffs) {
					if ((obj.getNewPath().equals(stage) || obj.getOldPath().equals(stage))) {
						if (obj.getChangeType().name().equals(DELETE)) {
							Node node = new Node(obj.getOldPath(), obj.getChangeType().name());
							node.getProperties().put(
									CoreConstants.ICONS,
									CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl(FILE),
									ResourcesPlugin.getInstance().getResourceUrl(STAGE_REMOVED)));
							stagedNodes.add(node);
						} else if (obj.getChangeType().name().equals(ADD)) {
							Node node = new Node(obj.getNewPath(), obj.getChangeType().name());
							node.getProperties().put(
									CoreConstants.ICONS,
									CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl(FILE),
									ResourcesPlugin.getInstance().getResourceUrl(STAGE_ADDED)));
							stagedNodes.add(node);
						} else if (obj.getChangeType().name().equals(MODIFY)) {
							Node node = new Node(obj.getNewPath(), obj.getChangeType().name());
							node.getProperties().put(
									CoreConstants.ICONS,
									CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl(FILE),
									ResourcesPlugin.getInstance().getResourceUrl(STAGED)));
							stagedNodes.add(node);
						}
					}
				}
			}
			return stagedNodes;
		}
	}

	public List<String> amendAuthorCommiter(String repositoryPath, boolean ok) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		List<String> list = new ArrayList<String>();
		PersonIdent pi = new PersonIdent(repo);
		list.add(pi.getName() + " <" + pi.getEmailAddress() + ">");
		RevWalk rw = new RevWalk(repo);
		ObjectId headId = repo.resolve(Constants.HEAD + "^{commit}");
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
				list.add(previousCommit.getAuthorIdent().getName() + " <" + previousCommit.getAuthorIdent().getEmailAddress() + ">");
				list.add(previousCommit.getFullMessage());
			} else {
				list.add(pi.getName() + " <" + pi.getEmailAddress() + ">");
				list.add("");
			}
		return list;
	}

	public void commitMethod(String repositoryPath, boolean ok, String message) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		git.commit().setMessage(message).setAmend(ok).call();
	}

	public void addToGitIndex(String repositoryPath, String filePathToAdd) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		git.add().addFilepattern(filePathToAdd).setUpdate(true).call();
		git.add().addFilepattern(filePathToAdd).setUpdate(false).call();
	}

	public void removeFromGitIndex(String repositoryPath, String filePathToRemove) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		git.reset().addPath(filePathToRemove).call();
	}
}
