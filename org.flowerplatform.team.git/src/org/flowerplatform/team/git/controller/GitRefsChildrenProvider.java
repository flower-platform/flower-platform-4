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

import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAG_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitRefsChildrenProvider extends AbstractController implements IChildrenProvider {
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		try {
			List<Node> children = new ArrayList<Node>();
			
			String repoPath = Utils.getRepo(node.getNodeUri());			
			Repository repo = GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(repoPath));
		
			String refType;
			String type;
			
			switch (node.getType()) {
				case GIT_LOCAL_BRANCHES_TYPE:
					refType = Constants.R_HEADS;
					type = GIT_LOCAL_BRANCH_TYPE;
					break;
				case GIT_REMOTE_BRANCHES_TYPE:
					refType = Constants.R_REMOTES;
					type = GIT_REMOTE_BRANCH_TYPE;
					break;
				default:
					refType = Constants.R_TAGS;
					type = GIT_TAG_TYPE;
			}
						
			Map<String, org.eclipse.jgit.lib.Ref> local = repo.getRefDatabase().getRefs(refType);
				
			for (Entry<String, Ref> entry : local.entrySet()) {
				children.add(CorePlugin.getInstance().getResourceService().getResourceHandler(node.getScheme())
						.createNodeFromRawNodeData(GitUtils.getNodeUri(repoPath, type, entry.getValue().getName()), entry.getValue()));
			}		
			
			return children;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return getChildren(node, context).size() > 0;
	}

}