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
package org.flowerplatform.team.git.controller;

import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAGS_TYPE;

import java.util.ArrayList;
import java.util.List;

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
public class GitChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		try {
			List<Node> children = new ArrayList<Node>();
	
			String repo = Utils.getRepo(node.getNodeUri());
		
			if(GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(node.getNodeUri()))) != null){
				children.add(CorePlugin.getInstance().getResourceService().getResourceHandler(node.getScheme())
				.createNodeFromRawNodeData(GitUtils.getNodeUri(repo, GIT_LOCAL_BRANCHES_TYPE),null));
				children.add(CorePlugin.getInstance().getResourceService().getResourceHandler(node.getScheme())
				.createNodeFromRawNodeData(GitUtils.getNodeUri(repo, GIT_REMOTE_BRANCHES_TYPE),null));
				children.add(CorePlugin.getInstance().getResourceService().getResourceHandler(node.getScheme())
				.createNodeFromRawNodeData(GitUtils.getNodeUri(repo, GIT_TAGS_TYPE),null));
				children.add(CorePlugin.getInstance().getResourceService().getResourceHandler(node.getScheme())
				.createNodeFromRawNodeData(GitUtils.getNodeUri(repo, GIT_REMOTES_TYPE),null));
			}
			
			return children;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		try {
			if(GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(node.getNodeUri()))) != null){
				return true;
			}
			
			return false;
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

}