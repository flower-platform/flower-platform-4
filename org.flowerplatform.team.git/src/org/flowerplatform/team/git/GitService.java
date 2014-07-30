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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevWalk;
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

	private static final int NETWORK_TIMEOUT_MSEC = 15000;
	
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
	
	public int validateRepoURL(String url) {
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

	public ArrayList<String> getRemoteBranches(String uri) {
//        System.out.println("Listing local branches:");
		try {
			Repository repository = new FileRepository(new File("/tmp"));		
			Git git = new Git(repository);
			LsRemoteCommand rc = git.lsRemote();
			rc.setRemote(uri.toString()).setTimeout(30);
			
	        Collection<Ref> call = rc.call();
	        ArrayList<String> branches = new ArrayList<String>();
	        String name;
	        
	        for (Ref ref : call) {
	        	name = ref.getName();
//	            System.out.println("Branch: " + name);
	            if (!name.equalsIgnoreCase("HEAD")) {
	            	String[] words = ref.getName().split("/");
//		        	System.out.print(words[0] +" " + words[1]);
		        	if (words[0].equalsIgnoreCase("refs") && words[1].equalsIgnoreCase("heads")) {
		        		branches.add(words[2]);
		        	}
	            }
	        }
			repository.close();
			Collections.sort(branches, new Comparator<String>() {
				@Override
				public int compare(String s1, String s2) {
					return s1.compareTo(s2);
				}
				 
			});
			System.out.println(branches);
			return branches;
		} 
		catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
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
				new ServiceContext<NodeService>().add(CoreConstants.EXECUTE_ONLY_FOR_UPDATER, true));
	}
	
	/**
	 * @author Tita Andreea
	 */
	/* rename the branch with the new name */
	public void renameBranch(String nodeUri,String newName) throws Exception {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		/* path for repository */
		String pathNode =  Utils.getRepo(nodeUri);
		Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(pathNode));
		Git gitInstance = new Git(repo);
		/* set the new name */
		gitInstance.branchRename().setOldName((String)node.getPropertyValue(CoreConstants.NAME)).setNewName(newName).call();
		/* refresh File System node */
		CorePlugin.getInstance().getNodeService().setProperty(node, CoreConstants.NAME, newName, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));	
	}

	public int cloneRepo(String nodeUri, String repoUri, Collection<String> branches, boolean cloneAll) {
		CloneCommand cc = new CloneCommand();
		try {
			cc.setCloneAllBranches(cloneAll);
			cc.setBranchesToClone(branches);
			cc.setURI(repoUri);
			URIish urish = new URIish(repoUri.trim());
			String repoName = urish.getHumanishName();
			File directory = (File) FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(nodeUri));
			cc.setDirectory(directory);
			cc.call();
			return 0;
		} 
		catch (InvalidRemoteException e) {
			System.out.println(e.getMessage());
			return -1;
		} 
		catch (TransportException e) {
			System.out.println(e.getMessage());
			return -2;
		} 
		catch (GitAPIException e) {
			System.out.println(e.getMessage());
			return -3;
		} 
		catch (URISyntaxException e) {
			System.out.println(e.getMessage());
			return -4;
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
			return -5;
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
		g.gc().getRepository().close();
		g.gc().call();
	}

}

