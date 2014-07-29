package org.flowerplatform.team.git.controller;

import org.flowerplatform.core.node.resource.BaseResourceHandler;
import org.flowerplatform.util.Utils;

public class GitVirtualNodeResourceHandler extends BaseResourceHandler {

	public GitVirtualNodeResourceHandler(String type) {
		super(type);
	}

	@Override
	public String getResourceUri(String nodeUri) {		
		return Utils.getUri(type, Utils.getRepo(nodeUri), null);
	}
	
}
