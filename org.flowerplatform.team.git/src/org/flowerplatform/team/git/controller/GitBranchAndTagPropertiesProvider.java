package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.team.git.GitConstants.BRANCH_ICON;
import static org.flowerplatform.team.git.GitConstants.COMMIT_ID;
import static org.flowerplatform.team.git.GitConstants.COMMIT_MESSAGE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_REBASE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_REMOTE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_UPSTREAM_BRANCH;
import static org.flowerplatform.team.git.GitConstants.FULL_NAME;
import static org.flowerplatform.team.git.GitConstants.ICONS_PATH;
import static org.flowerplatform.team.git.GitConstants.IS_CHECKEDOUT;
import static org.flowerplatform.team.git.GitConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.REMOTE_ICON;
import static org.flowerplatform.team.git.GitConstants.TAG_ICON;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.team.git.GitConstants;
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitBranchAndTagPropertiesProvider extends AbstractController implements IPropertiesProvider  {

	@Override
	public void populateWithProperties(Node node,ServiceContext<NodeService> context) {
		
		Repository repo = null;
		String repoPath = Utils.getRepo(node.getNodeUri());
		try {
			repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String name = Repository.shortenRefName(((Ref) node.getRawNodeData()).getName());
		String message = "";
		
		RevWalk walk = new RevWalk(repo);
		try {
			RevCommit commit = walk.parseCommit(repo.getRef(name).getObjectId());
			message = commit.getShortMessage();
		} catch (MissingObjectException e1) {
			e1.printStackTrace();
		} catch (IncorrectObjectTypeException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		StoredConfig config = repo.getConfig();
		String configRemote = config.getString(ConfigConstants.CONFIG_BRANCH_SECTION, 
				name,ConfigConstants.CONFIG_KEY_REMOTE);
		if (configRemote == null) {
			configRemote = "";
		}
		
		String configUpstreamBranch = config.getString(ConfigConstants.CONFIG_BRANCH_SECTION, 
				name, ConfigConstants.CONFIG_KEY_MERGE);
		if (configUpstreamBranch == null){
			configUpstreamBranch = "";
		}
		
		
		Boolean configRebase = config.getBoolean(ConfigConstants.CONFIG_BRANCH_SECTION, 
				name,ConfigConstants.CONFIG_KEY_REBASE, false);
		
		node.getProperties().put(NAME, name);
		node.getProperties().put(FULL_NAME, ((Ref) node.getRawNodeData()).getName());
		node.getProperties().put(CONFIG_REMOTE, configRemote);
		node.getProperties().put(CONFIG_UPSTREAM_BRANCH, configUpstreamBranch);
		node.getProperties().put(CONFIG_REBASE, configRebase);				
		node.getProperties().put(ICONS, setIcon(node));
		node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
		try {
			node.getProperties().put(IS_CHECKEDOUT, repo.getBranch().equals(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			node.getProperties().put(COMMIT_ID, repo.getRef(name).getObjectId().name());
		} catch (IOException e) {
			e.printStackTrace();
		}
		node.getProperties().put(COMMIT_MESSAGE, message);
	}
	
	
	public String setIcon(Node node){
		String icon = null;
		
		if(GitUtils.getType(node.getNodeUri()).equals("gitLocalBranch")){
			icon = ResourcesPlugin.getInstance().getResourceUrl(ICONS_PATH + BRANCH_ICON);
		}else if(GitUtils.getType(node.getNodeUri()).equals("gitRemoteBranch")){
			icon = ResourcesPlugin.getInstance().getResourceUrl(ICONS_PATH + BRANCH_ICON);
		}else if(GitUtils.getType(node.getNodeUri()).equals("gitTag")){
			icon = ResourcesPlugin.getInstance().getResourceUrl(ICONS_PATH + TAG_ICON);
		}else if(GitUtils.getType(node.getNodeUri()).equals("gitRemote")){
			icon = ResourcesPlugin.getInstance().getResourceUrl(ICONS_PATH + REMOTE_ICON);
		}
		
		return icon;
	}
	
}
