/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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

/**
 * @author Eduard Cojocea
 */
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
