package org.flowerplatform.team.git;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.team.git.remote.GitBranch;
import org.flowerplatform.util.Utils;

import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAG_TYPE;


/**
 * @author Valentina-Camelia Bojan
 */
public class GitService {
	
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
		Node child = new Node("refs/heads" + name, GIT_LOCAL_BRANCH_TYPE);
		
		/* get the parent */
		Node parent = CorePlugin.getInstance().getResourceService().getNode(parentUri);
		CorePlugin.getInstance().getNodeService().addChild(parent, child, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}

}