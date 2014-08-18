/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.team.git;

import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.UPDATE_REQUEST_REFRESH;
import static org.flowerplatform.team.git.GitConstants.ADD;
import static org.flowerplatform.team.git.GitConstants.CONFLICTED;
import static org.flowerplatform.team.git.GitConstants.DELETE;
import static org.flowerplatform.team.git.GitConstants.FILE;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REPO_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAG_TYPE;
import static org.flowerplatform.team.git.GitConstants.MODIFY;
import static org.flowerplatform.team.git.GitConstants.NETWORK_TIMEOUT_SEC;
import static org.flowerplatform.team.git.GitConstants.STAGED;
import static org.flowerplatform.team.git.GitConstants.STAGE_ADDED;
import static org.flowerplatform.team.git.GitConstants.STAGE_REMOVED;
import static org.flowerplatform.team.git.GitConstants.TEPORARY_LOCATION;
import static org.flowerplatform.team.git.GitConstants.UNSTAGED;
import static org.flowerplatform.team.git.GitConstants.UNTRACKED;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeCommand.FastForwardMode;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.codesync.sdiff.IFileContentProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.team.git.remote.GitCredentials;
import org.flowerplatform.team.git.remote.GitRef;
import org.flowerplatform.util.Utils;

/**
 * 
 * @author Valentina-Camelia Bojan
 */
public class GitService {

	public Node createStructureDiffFromGitCommits(String oldHash, String newHash, String repoPath, String sdiffOutputPath) {
		IFileContentProvider fileContentProvider = new GitFileContentProvider(newHash, oldHash, repoPath);
		OutputStream patch = new ByteArrayOutputStream();

		// get the patch for the two commits
		try {
			Repository repository = GitUtils.getRepository(FileControllerUtils
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
			Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repositoryPath));
			if (repo == null) {
				return false;
			}
			ObjectId resolved = repo.resolve(hash);
			if (resolved == null){
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
	 * @author Tita Andreea
	 */
	
	/* Merge branch */
	public String mergeBranch(String nodeUri, Boolean setSquash, boolean commit, int fastForwardOptions) throws Exception {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
		Ref ref = repo.getRef((String)node.getPropertyValue(GitConstants.NAME));
		
		Git gitInstance = new Git(repo);
		FastForwardMode fastForwardMode = FastForwardMode.FF;
		
		// set the parameters for Fast Forward options 
		switch (fastForwardOptions){
			case 0:
				fastForwardMode = FastForwardMode.FF;
				break;
			case 1:
				fastForwardMode = FastForwardMode.NO_FF;
				break;
			case 2:
				fastForwardMode = FastForwardMode.FF_ONLY;
				break;
		}
		
		// call merge operation 
		MergeCommand mergeCmd = gitInstance.merge().include(ref).setSquash(setSquash).setFastForward(fastForwardMode).setCommit(commit);
		MergeResult mergeResult = mergeCmd.call();
	   
		String fileSystemNodeUri = Utils.getUri(FILE_SCHEME, repoPath);
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(fileSystemNodeUri), 
				new Update().setFullNodeIdAs(fileSystemNodeUri).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		
		return GitUtils.handleMergeResult(mergeResult);		
	}

	/**
	 * @author Cristina Brinza
	 * 
	 * Get all branches from a certain repository
	 * 
	 */
	public ArrayList<GitRef> getBranches(String nodeUri) throws Exception {
		ArrayList<GitRef> branches = new ArrayList<GitRef>();

		String repoPath = Utils.getRepo(nodeUri);
		Repository repository = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		Git git = new Git(repository);

		List<Ref> localBranches = git.branchList().call();
		for (Ref ref : localBranches) {
			GitRef gitRef = new GitRef(ref.getName(), GIT_LOCAL_BRANCH_TYPE);
			branches.add(gitRef);
		}

		List<Ref> remoteBranches = git.branchList().setListMode(ListMode.REMOTE).call();
		for (Ref ref : remoteBranches) {
			GitRef gitRef = new GitRef(ref.getName(), GIT_REMOTE_BRANCH_TYPE);
			branches.add(gitRef);
		}

		List<Ref> tags = git.tagList().call();
		for (Ref tag : tags) {
			GitRef gitRef = new GitRef(tag.getName(), GIT_TAG_TYPE);
			branches.add(gitRef);
		}

		return branches;
	}
	
	/**
	 * @author Cristina Brinza
	 * 
	 * Creates new branch
	 * 
	 */
	public void createBranch(String parentUri, String name, String startPoint, boolean configureUpstream, boolean track, boolean setUpstream, boolean checkoutBranch) throws Exception {	
		String repoPath = Utils.getRepo(parentUri);
		Repository repository = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		
		Git git = new Git(repository);
		
		SetupUpstreamMode upstreamMode;
		/* see with what options the branch will be created */
		if (!configureUpstream) {
			upstreamMode = null;
		} else if (!track && !setUpstream) {
			/* use --no-track */
			upstreamMode = SetupUpstreamMode.NOTRACK;
		} else if (track) {
			/* use --track */
			upstreamMode = SetupUpstreamMode.TRACK;
		} else {
			/* use --set-upstream */
			upstreamMode = SetupUpstreamMode.SET_UPSTREAM;
		}
		
		/* createBranch */
		Ref createdBranch = git.branchCreate().setName(name).setUpstreamMode(upstreamMode).setStartPoint(startPoint).call();

		/* uri for the child to be created */
		String childUri = GitUtils.getNodeUri(repoPath, GIT_LOCAL_BRANCH_TYPE, createdBranch.getName());
		
		if (checkoutBranch) {
			/* call checkout branch method */
			checkout(childUri);
		}
		
		Node parent = CorePlugin.getInstance().getResourceService().getNode(parentUri);
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				parent,
				new Update().setFullNodeIdAs(GitUtils.getNodeUri(repoPath, GitConstants.GIT_LOCAL_BRANCHES_TYPE)).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));		
	}
	
	/**
	 * @param url URL to validate
	 * @return 0 if no exception thrown 
	 * @author Alina Bratu
	 */	
	public int validateRepoURL(String url) throws Exception {
		try {
			new URIish(url.trim());			
		} catch (Exception e) {
			throw e;
		}
		return 0;
	}

	/**
	 * 
	 * @param uri Repository URI from where the branches will be listed
	 * @return list of branches in the given repository
	 * 
	 * @author Alina Bratu
	 */
	public List<GitRef> getRemoteBranches(String uri) throws Exception {			
		List<GitRef> branches = new ArrayList<>();
		Repository repository = null;
		try {
			repository = new FileRepositoryBuilder()
				.setGitDir(FileControllerUtils.getFileAccessController().getFileAsFile(FileControllerUtils.getFileAccessController().getFile(TEPORARY_LOCATION)))
				.build();		
			
			Git git = new Git(repository);
						
	        Collection<Ref> refs = git.lsRemote().setRemote(uri.toString()).setTimeout(NETWORK_TIMEOUT_SEC).call();	     
	        
	        for (Ref ref : refs) {
	        	String name = ref.getName();
				if (!name.startsWith(Constants.R_HEADS)) {
					continue;
				}
				branches.add(new GitRef(Repository.shortenRefName(name), GIT_REMOTE_BRANCH_TYPE, name));			
	        }
		} finally {
			if (repository != null) {
				repository.close();
			}
		}		
		Collections.sort(branches);
		return branches;
	}

	/**
	 * @author Diana Balutoiu
	 */
	public void configureBranch(String branchNodeUri, String remote, String upstream, Boolean rebase) throws Exception {
			Node branchNode = CorePlugin.getInstance().getResourceService().getNode(branchNodeUri);
			String branchName = (String) branchNode.getPropertyValue(CoreConstants.NAME);		
			Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(branchNodeUri)));
			
			//get the .git/config file
			StoredConfig config = repo.getConfig();
			if (!remote.isEmpty()) {
				config.setString(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_REMOTE, remote);
			} else {
				config.unset(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_REMOTE);
			}
			if (!upstream.isEmpty()) {
				config.setString(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_MERGE, upstream);
			} else {
				config.unset(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_MERGE);
			}
			if (rebase) {
				config.setBoolean(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_REBASE, true);
			} else {
				config.unset(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_REBASE);
			}
			config.save();
			
			CorePlugin.getInstance().getResourceSetService().addUpdate(
					branchNode, 
					new Update().setFullNodeIdAs(branchNodeUri).setTypeAs(UPDATE_REQUEST_REFRESH), 
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}

	/**
	 * @author Marius Iacob
	 */
	public void deleteRef(String parentUri, String childUri) throws Exception {
		Node childNode = CorePlugin.getInstance().getResourceService().getNode(childUri);
		
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(childUri)));
		Git git = new Git(repo);
		
		String refName = (String) childNode.getPropertyValue(GitConstants.NAME);
		if (childNode.getType().equals(GitConstants.GIT_TAG_TYPE)) {
			git.tagDelete().setTags(refName).call();
		} else {
			git.branchDelete().setForce(true).setBranchNames(refName).call();
		}  
		
		// register update
		CorePlugin.getInstance().getNodeService().removeChild(
				CorePlugin.getInstance().getResourceService().getNode(parentUri),
				childNode, 
				new ServiceContext<NodeService>().add(EXECUTE_ONLY_FOR_UPDATER, true));
	}
	
	/**
	 * @author Diana Balutoiu
	 */
	public void reset(String nodeUri, String type, String hash) throws Exception {
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		
		ResetType resetType;
		switch (type) {
			case GitConstants.RESET_SOFT:
				resetType = ResetType.SOFT;
				break;
			case GitConstants.RESET_MIXED:
				resetType = ResetType.MIXED;
				break;
			default:
				resetType = ResetType.HARD;
		}
				
		new Git(repo).reset().setMode(resetType).setRef(hash).call();
		
		String fileSystemNodeUri = Utils.getUri(FILE_SCHEME, repoPath);
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(fileSystemNodeUri), 
				new Update().setFullNodeIdAs(fileSystemNodeUri).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}
	
	/**
	 * @author Tita Andreea
	 */	
	public void renameBranch(String parentUri, String nodeUri, String newName) throws Exception {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		// path for repository		
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(nodeUri)));
		Git git = new Git(repo);
		
		// set the new name
		git.branchRename().setOldName((String) node.getPropertyValue(CoreConstants.NAME)).setNewName(newName).call();
		
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				node, 
				new Update().setFullNodeIdAs(parentUri).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}
	
	/**
	 * 
	 * @param nodeUri URI of the parent node of where the repository will be cloned
	 * @param repoUri URI of the repository to be cloned
	 * @param branches List of branch names to be cloned
	 * @param cloneAll Indicates whether to clone all branches or not. If cloneAll is set to true, the list of branches will be ignored. 
	 *
	 * @author Alina Bratu
	 * @throws Exception 
	 */
	public void cloneRepository(String nodeUri, String repoUri, List<String> branches, boolean cloneAll) throws Exception {		
		String repoPath = Utils.getRepo(nodeUri);
		Repository repository = null;
		
		try {			
			CloneCommand clone = Git.cloneRepository();
			
			clone.setDirectory(FileControllerUtils.getFileAccessController().getFileAsFile(FileControllerUtils.getFileAccessController().getFile(repoPath)));
			clone.setURI(new URIish(repoUri.trim()).toString());
			clone.setCloneAllBranches(cloneAll);
			clone.setCloneSubmodules(false);	
			clone.setBranchesToClone(branches);
			
			Git git = clone.call();
			repository = git.getRepository();
		} finally {
			if (repository != null) {
				repository.close();
			}
			CorePlugin.getInstance().getResourceSetService().addUpdate(
					CorePlugin.getInstance().getResourceService().getNode(Utils.getUri(FILE_SCHEME, repoPath)), 
					new Update().setFullNodeIdAs(Utils.getUri(FILE_SCHEME, repoPath)).setTypeAs(UPDATE_REQUEST_REFRESH), 
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
			
			CorePlugin.getInstance().getResourceSetService().addUpdate(
					CorePlugin.getInstance().getResourceService().getNode(nodeUri), 
					new Update().setFullNodeIdAs(GitUtils.getNodeUri(repoPath, GIT_REPO_TYPE)).setTypeAs(UPDATE_REQUEST_REFRESH), 
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		}		
	}

	/** 
	 * @author Vlad Bogdan Manica
	 * @param nodeUri This is the name of a branch/tag.
	 * @param createNew If is set to 'true' we create a new local branch. 
	 * @throws Exception
	 */
	public void checkout(String nodeUri) throws Exception {				
		String Name = GitUtils.getName(nodeUri);
		String repositoryPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
				
		Git g = new Git(repo);	
		
		g.checkout().setName(Name).call();	
//		g.gc().getRepository().close();
//		g.gc().call();
	}

	/** 
	 * @author Catalin Burcea
	 */	
	public void deleteGitRepository(String nodeUri, Boolean keepWorkingDirectoryContent) throws Exception {
		String repositoryPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		
		RepositoryCache.close(repo);
		repo.getAllRefs().clear();
		repo.close();
			
		Node gitNode = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		CorePlugin.getInstance().getNodeService().setProperty(gitNode, GitConstants.IS_GIT_REPOSITORY, false, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(CoreConstants.EXECUTE_ONLY_FOR_UPDATER, true));
		
		if (keepWorkingDirectoryContent) {
			FileControllerUtils.getFileAccessController().delete(repo.getDirectory());
		} else {
			FileControllerUtils.getFileAccessController().delete(repo.getDirectory().getParentFile());
		}
	}

	/**
	 * @author Cristina Brinza
	 * 
	 * Create / Configure Remote
	 */
	public void configureRemote(String nodeUri, String remoteName, String remoteUri, boolean pushConfig, List<String> refSpecs) throws Exception {
		String repoPath = Utils.getRepo(nodeUri);
		Repository repository = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));

		RemoteConfig config = new RemoteConfig(repository.getConfig(), remoteName);

		List<URIish> URIs = config.getURIs();
		if (URIs.size() == 0) {
			config.addURI(new URIish(remoteUri));
		}

		if (pushConfig) {
			/* remove all push refspec */
			List<RefSpec> pushRefSpecs = config.getPushRefSpecs();
			for (int i = 0; i < pushRefSpecs.size(); i++) {
				config.removePushRefSpec(pushRefSpecs.get(i));
			}
		} else {
			/* remove all fetch refspecs */
			List<RefSpec> fetchRefSpecs = config.getFetchRefSpecs();
			for (int i = 0; i < fetchRefSpecs.size(); i++) {
				config.removeFetchRefSpec(fetchRefSpecs.get(i));
			}
		}

		for (String refSpecString : refSpecs) {
			RefSpec refSpec = new RefSpec(refSpecString);
			if (pushConfig) {
				/* push refspec */
				config.addPushRefSpec(refSpec);
			} else {
				/* fetch refspec */
				config.addFetchRefSpec(refSpec);
			}
		}

		config.update(repository.getConfig());
		repository.getConfig().save();
		
		/* refresh node */
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				node, 
				new Update().setFullNodeIdAs(nodeUri).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}
	
	/**
	 * @author Cristina Brinza
	 * 
	 * Delete Remote
	 */
	public void deleteRemote(String childUri, String parentUri) throws Exception {
		Node child = CorePlugin.getInstance().getResourceService().getNode(childUri);
		Node parent = CorePlugin.getInstance().getResourceService().getNode(parentUri);
		
		String repoPath = Utils.getRepo(childUri);
		Repository repository = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));

		StoredConfig config = repository.getConfig();
		config.unsetSection("remote", (String)child.getPropertyValue(GitConstants.NAME));
		config.save();
		
		/* refresh parent node */
		CorePlugin.getInstance().getNodeService().removeChild(
			    parent,
			    child, 
			    new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(EXECUTE_ONLY_FOR_UPDATER, true));
	}


	/** 
	 * @author Andreea Tita
	 */
	public GitCredentials getCredentials(String remote) throws Exception {
		HttpSession session = CorePlugin.getInstance().getRequestThreadLocal().get().getSession();
		
		if ((GitCredentials)session.getAttribute(remote) != null ) {
			return  (GitCredentials)session.getAttribute(remote);
		}
		
		return null;
	}
	
	/** 
	 * @author Andreea Tita
	 */
	public void setCredentials(String remote, GitCredentials credentials) {
		HttpSession session = CorePlugin.getInstance().getRequestThreadLocal().get().getSession();
			
			if (credentials == null) {
				if ((GitCredentials)session.getAttribute(remote) != null) {
					return;
				} else {
					session.setAttribute(remote, null);
				}
			} else {
				session.setAttribute(remote, credentials);
			}
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

