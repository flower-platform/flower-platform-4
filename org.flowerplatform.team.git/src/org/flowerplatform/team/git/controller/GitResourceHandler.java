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

import static org.flowerplatform.team.git.GitConstants.GIT_REMOTES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REPO_TYPE;

import java.io.File;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.Utils;

/**
 * @author Cojocea Marius Eduard
 */
public class GitResourceHandler implements IResourceHandler {
	
	@Override
	public String getResourceUri(String nodeUri) {
		return Utils.getUri(Utils.getScheme(nodeUri), Utils.getRepo(nodeUri) + "|" + GIT_REPO_TYPE, null);
	}

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		try {
			if (GitUtils.getType(nodeUri).equals(GIT_REMOTES_TYPE) || GitUtils.getType(nodeUri).equals(GIT_REMOTE_TYPE)) {
				return GitUtils.getName(nodeUri);
			}
			
			String repositoryPath = Utils.getRepo(nodeUri);
			Repository repo = null;
			Ref ref = null;
			repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
			ref = repo.getRef(GitUtils.getName(nodeUri));	
			
			return ref;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		String type = GitUtils.getType(nodeUri);
		Node node = new Node(nodeUri, type);
		node.setRawNodeData(rawNodeData);
		return node;
	}

	@Override
	public Object load(String resourceUri) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Object resourceData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDirty(Object resourceData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// TODO Auto-generated method stub
		
	}

}