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
import static org.flowerplatform.team.git.GitConstants.AUTHOR;
import static org.flowerplatform.team.git.GitConstants.COMMITTER;
import static org.flowerplatform.team.git.GitConstants.DELETE;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_PREFIX_SESSION;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REPO_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAG_TYPE;
import static org.flowerplatform.team.git.GitConstants.MODIFY;
import static org.flowerplatform.team.git.GitConstants.NETWORK_TIMEOUT_SEC;
import static org.flowerplatform.team.git.GitConstants.PREVIOUS_AUTHOR;
import static org.flowerplatform.team.git.GitConstants.PREVIOUS_COMMIT_MESSAGE;
import static org.flowerplatform.team.git.GitConstants.TEPORARY_LOCATION;

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

import org.eclipse.jgit.api.CherryPickResult;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeCommand.FastForwardMode;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.AnyObjectId;
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
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
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
import org.flowerplatform.team.git.history.internal.GitHistoryConstants;
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
	 * @author Tita Andreea
	 */
	public String mergeBranch(String nodeUri, boolean setSquash, boolean commit, int fastForwardOptions, String idCommit) throws Exception {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		
		Git gitInstance = new Git(repo);
		MergeCommand mergeCmd;
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
		if (idCommit != null) {
			mergeCmd = gitInstance.merge().include((AnyObjectId) repo.resolve(idCommit));
		} else {
			mergeCmd = gitInstance.merge().include(repo.getRef((String) node.getPropertyValue(GitConstants.NAME)));
		}

		MergeResult mergeResult = mergeCmd.setSquash(setSquash).setFastForward(fastForwardMode).setCommit(commit).call();

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
	public void createBranch(String parentUri, String name, String startPoint, boolean configureUpstream, boolean track, boolean setUpstream, boolean checkoutBranch, String commitId) throws Exception {
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
		
		Ref createdBranch = git.branchCreate().setName(name).setUpstreamMode(upstreamMode).setStartPoint(commitId == null ? startPoint : commitId).call();
				
		if (checkoutBranch) {
			/* call checkout branch method */
			checkout(GitUtils.getNodeUri(repoPath, GIT_LOCAL_BRANCH_TYPE, createdBranch.getName()));
		} else {
			Node parent = CorePlugin.getInstance().getResourceService().getNode(parentUri);
			CorePlugin.getInstance().getResourceSetService().addUpdate(
					parent,
					new Update().setFullNodeIdAs(GitUtils.getNodeUri(repoPath, GitConstants.GIT_LOCAL_BRANCHES_TYPE)).setTypeAs(UPDATE_REQUEST_REFRESH), 
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		}
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
		String name = GitUtils.getName(nodeUri);
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
				
		new Git(repo).checkout().setName(name).call();
		
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(nodeUri), 
				new Update().setFullNodeIdAs(GitUtils.getNodeUri(repoPath, GIT_REPO_TYPE)).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		
		String fileSystemNodeUri = Utils.getUri(FILE_SCHEME, repoPath);		
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(fileSystemNodeUri), 
				new Update().setFullNodeIdAs(fileSystemNodeUri).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}

	/**
	 * 
	 * @author Cojocea Marius Eduard
	 */	
	public String rebase(String nodeUri, String hash) throws Exception {
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
		
		RebaseResult result = new Git(repo).rebase().setUpstream(hash).call();
		
		String fileSystemNodeUri = Utils.getUri(FILE_SCHEME, repoPath);
		
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(fileSystemNodeUri), 
				new Update().setFullNodeIdAs(fileSystemNodeUri).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(nodeUri), 
				new Update().setFullNodeIdAs(GitUtils.getNodeUri(repoPath, GIT_REPO_TYPE)).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		
		return GitUtils.handleRebaseResult(result);
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
				
		if (keepWorkingDirectoryContent) {
			FileControllerUtils.getFileAccessController().delete(repo.getDirectory());
		} else {
			File[] repoFiles = repo.getDirectory().getParentFile().listFiles();
			for (File file : repoFiles) {
				FileControllerUtils.getFileAccessController().delete(file);
			}
		}
		
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(Utils.getUri(FILE_SCHEME, repositoryPath)), 
				new Update().setFullNodeIdAs(Utils.getUri(FILE_SCHEME, repositoryPath)).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(nodeUri), 
				new Update().setFullNodeIdAs(GitUtils.getNodeUri(repositoryPath, GIT_REPO_TYPE)).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
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
	 * @author Cristina Brinza
	 */
	@SuppressWarnings("unchecked")
	public String fetch(String nodeUri, String fetchNodeUri, ArrayList<String> fetchRefMappings) throws Exception {	
		String repoPath = Utils.getRepo(nodeUri);
		Repository repository = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		FetchCommand fetchCommand = new Git(repository).fetch();
		GitCredentials credentials = new GitCredentials();
		
		if (GitConstants.GIT_REMOTE_TYPE.equals(node.getType())) {
			fetchCommand.setRemote(GitUtils.getName(nodeUri));
			credentials = getCredentials(((ArrayList<String>) node.getPropertyValue(GitConstants.REMOTE_URIS)).get(0));
		} else {
			List<RefSpec> fetchRefSpecsList = new ArrayList<RefSpec>();
			if (fetchRefMappings != null) {
				for (String fetchRefSpecString : fetchRefMappings) {
					fetchRefSpecsList.add(new RefSpec(fetchRefSpecString));
				}
			}
			fetchCommand.setRemote(fetchNodeUri).setRefSpecs(fetchRefSpecsList);			
			credentials = getCredentials(fetchNodeUri);			
		}

		// provide credentials for use in connecting to repositories 
		if (credentials != null) {
			fetchCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(credentials.getUsername(), credentials.getPassword()));
		}
		
		FetchResult fetchResult = fetchCommand.call();
		
		// refresh Remote Branches node
		String remoteBranchesUri = GitUtils.getNodeUri(repoPath, GIT_REMOTE_BRANCHES_TYPE);
		CorePlugin.getInstance().getResourceSetService().addUpdate(
				CorePlugin.getInstance().getResourceService().getNode(remoteBranchesUri), 
				new Update().setFullNodeIdAs(remoteBranchesUri).setTypeAs(UPDATE_REQUEST_REFRESH), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		
		return GitUtils.handleFetchResult(fetchResult);	
	}
	
	/**
	 * @author Andreea Tita
	 */
	@SuppressWarnings("unchecked")
	public String push(String nodeUri, String pushNodeUri, ArrayList<String> pushRefMappings) throws Exception {
		String repoPath =  Utils.getRepo(nodeUri);
		Repository  repository = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		PushCommand pushCommand;
		GitCredentials credentials = new GitCredentials();

		if (node.getType().equals(GitConstants.GIT_REMOTE_TYPE)) {
			pushCommand = new Git(repository).push().setRemote(GitUtils.getName(nodeUri));

			//check if credentials for remote are set
			credentials = getCredentials(((ArrayList<String>)node.getPropertyValue(GitConstants.REMOTE_URIS)).get(0));
		} else {
			List<RefSpec> specsList = new ArrayList<RefSpec>();
			if (pushRefMappings != null)  {
				for (String refMapping : pushRefMappings) {
					specsList.add(new RefSpec(refMapping));
				}
			}
		
			pushCommand = new Git(repository).push().setRemote(new URIish(pushNodeUri).toPrivateString()).setRefSpecs(specsList);
		
			//check if credentials for pushNode are set
			credentials = getCredentials(pushNodeUri);
		}
		
		// provide credentials for use in connecting to repositories 
		if (credentials != null) {
			pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(credentials.getUsername(),credentials.getPassword()));
		}
		Iterable<PushResult> resultIterable = pushCommand.call();
	
		return GitUtils.handlePushResult(resultIterable.iterator().next());
	}
	
	/**
	 * @author Alina Bratu
	 * 
	 * @param nodeUri 
	 * 		node URI of the repository
	 * @param commitId 
	 * 		id of the commit to be cherry-picked
	 * @return message describing the result of the cherry-picking (successful, with conflicts, failed, already done)
	 * @throws Exception
	 */
	public String cherryPickCommit(String nodeUri, String commitId) throws Exception {
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(nodeUri)));
		
		CherryPickResult result = new Git(repo).cherryPick().include(repo.resolve(commitId)).call();
		
		RevCommit newHead = result.getNewHead();
		if (newHead != null && result.getCherryPickedRefs().isEmpty()) {
			return ResourcesPlugin.getInstance().getMessage("team.git.history.cherryPick.null");
		}
		
		if (newHead == null) {
			switch (result.getStatus()) {
				case CONFLICTING:
					return ResourcesPlugin.getInstance().getMessage("team.git.history.cherryPick.conflicts");					
				case FAILED:
					return ResourcesPlugin.getInstance().getMessage("team.git.history.cherryPick.fail");
				default:
					break;				
			} 
		}		
		return ResourcesPlugin.getInstance().getMessage("team.git.history.cherryPick.ok");
	}
	
	/**
	 * Reverts the commit identified by the id passed through <code>commitId</code>
	 * 
	 * @author Alina Bratu
	 * @param nodeUri node URI of the repository
	 * @param commitId id of the commit to be reverted
	 * @throws Exception
	 */
	public String revertCommit(String nodeUri, String commitId) throws Exception {
		String repoPath = Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		
		RevertCommand cmd = new Git(repo).revert().include(repo.resolve(commitId));
		RevCommit newHead = cmd.call();
		
		if (newHead != null && cmd.getRevertedRefs().isEmpty()) {
			return ResourcesPlugin.getInstance().getMessage("team.git.history.revert.alreadyReverted.message");
		}
		if (newHead == null) {
			return cmd.getFailingResult().toString();
		}
		return null;
	}

	/** 
	 * @author Andreea Tita
	 */
	public GitCredentials getCredentials(String remote) throws Exception {
		HttpSession session = CorePlugin.getInstance().getRequestThreadLocal().get().getSession();
		
		synchronized (session) {
			String attr = GIT_PREFIX_SESSION + remote;
			if (session.getAttribute(attr) != null) {
				return (GitCredentials) session.getAttribute(attr);
			}
			return null;
		}		
	}
	
	/** 
	 * @author Andreea Tita
	 */
	public void setCredentials(String remote, GitCredentials credentials) {
		HttpSession session = CorePlugin.getInstance().getRequestThreadLocal().get().getSession();
		
		synchronized (session) {
			session.setAttribute(GIT_PREFIX_SESSION + remote, credentials);			
		}
		
	}

	/**
	 * @author Marius Iacob
	 */
	public List<Object> getStageAndUnstageFiles(String repositoryPath) throws Exception {
		Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		
		if (repo == null) { // not a git repo
			return null;
		}
		
		Git git = new Git(repo);
		List<Object> stagingList = new ArrayList<Object>();
		Set<String> conflictList = git.status().call().getConflicting();
		boolean ok = false;
		String type = null;
		
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
				Node node = new Node(getNodeStagingUri(currentConflict, type), type);
				node.getProperties().put(GitConstants.FILE_PATH, currentConflict);
				node.getProperties().put(
						CoreConstants.ICONS,
						CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl("images/core/file.gif"),
						ResourcesPlugin.getInstance().getResourceUrl("images/team.git/conflict.gif")));
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
				switch (obj.getChangeType().name()) {
					case DELETE:
						Node nodeDelete = new Node(getNodeStagingUri(obj.getOldPath(), DELETE), DELETE);
						nodeDelete.getProperties().put(GitConstants.FILE_PATH, obj.getOldPath());
						nodeDelete.getProperties().put(
								CoreConstants.ICONS,
								CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl("images/core/file.gif"),
								ResourcesPlugin.getInstance().getResourceUrl("images/team.git/staged_removed.gif")));
						unstagedNodes.add(nodeDelete);
						break;

					case ADD:  
						Node nodeAdd = new Node(getNodeStagingUri(obj.getNewPath(), ADD), ADD);
						nodeAdd.getProperties().put(GitConstants.FILE_PATH, obj.getNewPath());
						nodeAdd.getProperties().put(
								CoreConstants.ICONS,
								CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl("images/core/file.gif"),
								ResourcesPlugin.getInstance().getResourceUrl("images/team.git/untracked.gif")));
						unstagedNodes.add(nodeAdd);
						break;

					case MODIFY: 
						Node nodeModify = new Node(getNodeStagingUri(obj.getNewPath(), MODIFY), MODIFY);
						nodeModify.getProperties().put(GitConstants.FILE_PATH, obj.getNewPath());
						nodeModify.getProperties().put(
								CoreConstants.ICONS,
								CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl("images/core/file.gif"),
								ResourcesPlugin.getInstance().getResourceUrl("images/team.git/unstaged.gif")));
						unstagedNodes.add(nodeModify);
						break;
				}
			}
		}
		stagingList.add(unstagedNodes);
			
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
					switch (obj.getChangeType().name()) {
						case DELETE:
							Node nodeDelete = new Node(getNodeStagingUri(obj.getOldPath(), DELETE), DELETE);
							nodeDelete.getProperties().put(GitConstants.FILE_PATH, obj.getOldPath());
							nodeDelete.getProperties().put(
									CoreConstants.ICONS,
									CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl("images/core/file.gif"),
									ResourcesPlugin.getInstance().getResourceUrl("images/team.git/staged_removed.gif")));
							stagedNodes.add(nodeDelete);
							break;
						case ADD:
							Node nodeAdd = new Node(getNodeStagingUri(obj.getNewPath(), ADD), ADD);
							nodeAdd.getProperties().put(GitConstants.FILE_PATH, obj.getNewPath());
							nodeAdd.getProperties().put(
									CoreConstants.ICONS,
									CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl("images/core/file.gif"),
									ResourcesPlugin.getInstance().getResourceUrl("images/team.git/staged_added.gif")));
							stagedNodes.add(nodeAdd);
							break;
						case MODIFY:
							Node nodeModify = new Node(getNodeStagingUri(obj.getNewPath(), MODIFY), MODIFY);
							nodeModify.getProperties().put(GitConstants.FILE_PATH, obj.getNewPath());
							nodeModify.getProperties().put(
									CoreConstants.ICONS,
									CorePlugin.getInstance().getImageComposerUrl(ResourcesPlugin.getInstance().getResourceUrl("images/core/file.gif"),
									ResourcesPlugin.getInstance().getResourceUrl("images/team.git/staged.gif")));
							stagedNodes.add(nodeModify);
							break;
					}
				}
			}
		}
		stagingList.add(stagedNodes);

		Node authorInfoNode = new Node(null, null);
		
		PersonIdent pi = new PersonIdent(repo);
		authorInfoNode.getProperties().put(COMMITTER, String.format("%s <%s>", pi.getName(), pi.getEmailAddress()));		
		authorInfoNode.getProperties().put(AUTHOR, String.format("%s <%s>", pi.getName(), pi.getEmailAddress()));
		
		ObjectId headId = repo.resolve(Constants.HEAD + "^{commit}");		
		if (headId != null) {
			RevWalk rw = new RevWalk(repo);
			RevCommit previousCommit = rw.parseCommit(headId);				
			rw.dispose();
			authorInfoNode.getProperties().put(PREVIOUS_AUTHOR, String.format("%s <%s>", previousCommit.getAuthorIdent().getName(), previousCommit.getAuthorIdent().getEmailAddress()));
			authorInfoNode.getProperties().put(PREVIOUS_COMMIT_MESSAGE, previousCommit.getFullMessage());
		}
		
		stagingList.add(authorInfoNode);
		
		return stagingList;
	}

	/**
	 * @author Marius Iacob
	 */
	private String getNodeStagingUri(String path, String type) {
		String nodeUri;
		int index = path.lastIndexOf("/");
		if (index == -1) {
			nodeUri = path.substring(index + 1);
		} else {
			nodeUri = path.substring(index + 1) + " - " + path.substring(0, index);
		}
		if (MODIFY.equals(type)) {
			nodeUri = String.format("> %s", nodeUri);
		}
		return nodeUri;
	}
	
	/**
	 * @author Marius Iacob
	 */
	public void commitAndPush(String repositoryPath, boolean amend, String message) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		git.commit().setMessage(message).setAmend(amend).call();

		// TODO CC: add push 
	}

	/**
	 * @author Marius Iacob
	 */
	public void addToGitIndex(String repositoryPath, List<String> filesToAdd) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		for (String file : filesToAdd) {
			git.add().addFilepattern(file).setUpdate(true).call();
			git.add().addFilepattern(file).setUpdate(false).call();
		}
	}

	/**
	 * @author Marius Iacob
	 */
	public void removeFromGitIndex(String repositoryPath, List<String> filesToRemove) throws Exception {
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		Git git = new Git(repo);
		for (String file : filesToRemove) {
			git.reset().addPath(file).call();
		}
	}	
	
}

