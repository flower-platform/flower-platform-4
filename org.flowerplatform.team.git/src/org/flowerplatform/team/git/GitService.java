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

import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAG_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.codesync.sdiff.IFileContentProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.team.git.remote.GitBranch;
import org.flowerplatform.util.Utils;

/**
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
	public ArrayList<GitBranch> getBranches(String nodeUri) {
		ArrayList<GitBranch> branches = new ArrayList<GitBranch>();
		try {
			String repoPath = Utils.getRepo(nodeUri);
			Repository repository = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
			Map <String, Ref> allRefs = repository.getAllRefs();
			
			Set<String> keys = allRefs.keySet();
			for (String key : keys) {
				if (!key.startsWith("HEAD")) {
					GitBranch branch;
					String branchType = "";
					
					if (key.startsWith("refs/heads")) {
						branchType = GIT_LOCAL_BRANCH_TYPE;
					} else if (key.startsWith("refs/remotes")) {
						branchType = GIT_REMOTE_BRANCH_TYPE;
					} else if (key.startsWith("refs/tags")) {
						branchType = GIT_TAG_TYPE;
					}
					
					branch = new GitBranch(key, branchType);
					branches.add(branch);
				}
			}
			
		} catch (Exception e) {
			return null;
		}
		
		return branches;
	}
	
	/**
	 * Creates new branch
	 *  
	 */
	public void createBranch(String parentUri, String name, String startPoint, boolean configureUpstream, boolean track, boolean setUpstream, boolean checkoutBranch) throws GitAPIException,
			RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, Exception {
		SetupUpstreamMode upstreamMode;

		String repoPath = Utils.getRepo(parentUri);
		Repository repository = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
		
		Git git = new Git(repository);
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
		
		if (checkoutBranch) {
			/* call checkout Branch method */
		}
	
		/* create child */
		Node child = new Node("refs/heads/" + name, GIT_LOCAL_BRANCH_TYPE);
		
		/* get the parent */
		Node parent = CorePlugin.getInstance().getResourceService().getNode(parentUri);
		CorePlugin.getInstance().getNodeService().addChild(parent, child, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}


	public void deleteGitRepository(String nodeUri, Boolean keepWorkingDirectoryContent) throws Exception {
		String repositoryPath = GitUtils.getNodePath(nodeUri);
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
		RepositoryCache.close(repo);
		repo.getAllRefs().clear();	
		repo.close();
		
		if(keepWorkingDirectoryContent){
			GitUtils.delete(repo.getDirectory());
		}else{
			GitUtils.delete(repo.getDirectory().getParentFile());
		}
		Node gitNode = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		CorePlugin.getInstance().getNodeService().setProperty(gitNode, GitConstants.IS_REPO, false, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
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
	public void deleteBranch(String parentUri, String childUri) throws Exception {
			Node childNode = CorePlugin.getInstance().getResourceService().getNode(childUri);
			Node parentNode = CorePlugin.getInstance().getResourceService().getNode(parentUri);
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(childUri)));
			Git git = new Git(repo);
			git.branchDelete().setForce(true).setBranchNames(childNode.getPropertyValue(GitConstants.NAME).toString()).call();
			CorePlugin.getInstance().getNodeService().removeChild(parentNode, childNode, new ServiceContext<NodeService>());
	}

	/* get all names of branches from repository */
	public ArrayList<String> getAllNamesOfBranches(String repoPath){
		return null;
	}
	
	/* rename the branch with the new name */
	public void renameBranch(String nodeUri,String oldName,String newName) throws Exception {
		int index = nodeUri.indexOf("|");
	
		if (index < 0) {
			index = nodeUri.length();
		}
		String pathNode = nodeUri.substring(nodeUri.indexOf(":") + 1, index);
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(pathNode));
		Git gitInstance = new Git(repo);
		/* set the new name */
		gitInstance.branchRename().setOldName(oldName).setNewName("origin/" + newName).call();
		/* refresh File System node */
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		CorePlugin.getInstance().getNodeService().setProperty(node,CoreConstants.NAME,newName,new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));	
	}

}

