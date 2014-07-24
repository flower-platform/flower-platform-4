package org.flowerplatform.team.git.controller;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		String repositoryPath = Utils.getRepo(nodeUri);
		Ref ref = null;
		try {
			Repository repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repositoryPath));
			ref = repo.getRef(GitUtils.getName(nodeUri));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ref;
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		Ref nodeRef = (Ref) rawNodeData;
		String type = GitUtils.getType(nodeUri);
		Node node = new Node(nodeUri, type);
		node.setRawNodeData(nodeRef);
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
