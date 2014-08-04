package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.team.git.GitConstants.COMMIT_ID;
import static org.flowerplatform.team.git.GitConstants.COMMIT_MESSAGE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_REBASE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_REMOTE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_UPSTREAM_BRANCH;
import static org.flowerplatform.team.git.GitConstants.FULL_NAME;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAG_TYPE;
import static org.flowerplatform.team.git.GitConstants.IS_CHECKEDOUT;
import static org.flowerplatform.team.git.GitConstants.NAME;

import java.io.File;

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
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitRefPropertiesProvider extends AbstractController implements IPropertiesProvider  {
	
	@Override
	public void populateWithProperties(Node node,ServiceContext<NodeService> context) throws RuntimeException {
		try {
			Repository repo = null;
			String repoPath = Utils.getRepo(node.getNodeUri());
			repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
			
			String name = Repository.shortenRefName(((Ref) node.getRawNodeData()).getName());
			String message = "";
			
			RevWalk walk = new RevWalk(repo);		
			RevCommit commit = walk.parseCommit(repo.getRef(name).getObjectId());
			message = commit.getShortMessage();
			
			StoredConfig config = repo.getConfig();
			String configRemote = config.getString(ConfigConstants.CONFIG_BRANCH_SECTION, name,ConfigConstants.CONFIG_KEY_REMOTE);
			if (configRemote == null) {
				configRemote = "";
			}
			
			String configUpstreamBranch = config.getString(ConfigConstants.CONFIG_BRANCH_SECTION, name, ConfigConstants.CONFIG_KEY_MERGE);
			if (configUpstreamBranch == null){
				configUpstreamBranch = "";
			}
		
			Boolean configRebase = config.getBoolean(ConfigConstants.CONFIG_BRANCH_SECTION, name,ConfigConstants.CONFIG_KEY_REBASE, false);
			
			node.getProperties().put(NAME, name);
			node.getProperties().put(FULL_NAME, ((Ref) node.getRawNodeData()).getName());
			node.getProperties().put(CONFIG_REMOTE, configRemote);
			node.getProperties().put(CONFIG_UPSTREAM_BRANCH, configUpstreamBranch);
			node.getProperties().put(CONFIG_REBASE, configRebase);				
			node.getProperties().put(ICONS, setIcon(node));
			node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
			node.getProperties().put(IS_CHECKEDOUT, repo.getBranch().equals(name));
			node.getProperties().put(COMMIT_ID, repo.getRef(name).getObjectId().name());
			node.getProperties().put(COMMIT_MESSAGE, message);
		} catch (Exception e){	
		}
	}
	
	public String setIcon(Node node){
		String icon = null;	
		String type = GitUtils.getType(node.getNodeUri());
		
		switch (type) {
		case GIT_LOCAL_BRANCH_TYPE :
			icon = ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/" + "branch.gif");
			break;
		case GIT_REMOTE_BRANCH_TYPE :
			icon = ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/" + "branch.gif");
			break;
		case GIT_TAG_TYPE :
			icon = ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/" + "tag.gif");
			break;
		case GIT_REMOTE_TYPE :
			icon = ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/" + "remote.gif");
			break;
		}
		
		return icon;
	}
	
}
