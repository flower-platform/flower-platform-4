package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAGS_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTES_TYPE;


import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

public class GitVirtualChildPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if (node.getType().equals(GIT_LOCAL_BRANCHES_TYPE)) {
			node.getProperties().put(NAME, ResourcesPlugin.getInstance().getMessage("git.localBranches"));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/folder.gif"));
		} else if (node.getType().equals(GIT_REMOTE_BRANCHES_TYPE)) {
			node.getProperties().put(NAME, ResourcesPlugin.getInstance().getMessage("git.remoteBranches"));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/folder.gif"));
		} else if (node.getType().equals(GIT_TAGS_TYPE)) {
			node.getProperties().put(NAME, ResourcesPlugin.getInstance().getMessage("git.tags"));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/tags.gif"));
		} else if (node.getType().equals(GIT_REMOTES_TYPE)) {
			node.getProperties().put(NAME, ResourcesPlugin.getInstance().getMessage("git.remotes"));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/remotes.gif"));
		}		
	}
	
}
