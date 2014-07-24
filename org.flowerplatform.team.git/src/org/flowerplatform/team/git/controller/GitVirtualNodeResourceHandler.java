package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.file.FileControllerUtils.getRepo;

import org.flowerplatform.core.node.resource.BaseResourceHandler;
import org.flowerplatform.util.Utils;

public class GitVirtualNodeResourceHandler extends BaseResourceHandler {

	public GitVirtualNodeResourceHandler(String type) {
		super(type);
	}

	@Override
	public String getResourceUri(String nodeUri) {		
		return Utils.getUri(type, getRepo(nodeUri), null);
	}
	
}
