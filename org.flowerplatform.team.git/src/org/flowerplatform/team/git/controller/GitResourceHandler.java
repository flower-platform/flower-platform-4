package org.flowerplatform.team.git.controller;

import static org.flowerplatform.team.git.GitConstants.GIT_REPO_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTES_TYPE;

import java.io.File;
import java.io.IOException;

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
			String repositoryPath = Utils.getRepo(nodeUri);
			Repository repo = null;
			Ref ref = null;
			repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
			
			
			if (GitUtils.getType(nodeUri).equals(GIT_REMOTES_TYPE)) {
				return GitUtils.getName(nodeUri);
			}
			
			ref = repo.getRef(GitUtils.getName(nodeUri));	
			
			return ref;
		} catch (Exception e){
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
