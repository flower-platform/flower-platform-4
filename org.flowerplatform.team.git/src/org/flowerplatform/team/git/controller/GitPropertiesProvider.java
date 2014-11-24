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

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.CURRENT_BRANCH;
import static org.flowerplatform.team.git.GitConstants.CURRENT_COMMIT;

import java.util.Map;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.Repository;
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
public class GitPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		try {
			Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(node.getNodeUri())));
			
			node.getProperties().put(NAME, ResourcesPlugin.getInstance().getMessage("git.git"));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/git.gif"));
			node.getProperties().put(GitConstants.IS_GIT_REPOSITORY, repo != null);	
			
			if (repo != null) {								
				node.getProperties().put(CURRENT_BRANCH, repo.getBranch());
				node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
				
				Map<String, Ref> refs = repo.getRefDatabase().getRefs(RefDatabase.ALL);
					
				for (Ref entry : refs.values()) {
					if (entry.getTarget().getName().equals(repo.getFullBranch())) {
						node.getProperties().put(CURRENT_COMMIT, entry.getObjectId().name());
						break;
					}
				}				
			}					
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
