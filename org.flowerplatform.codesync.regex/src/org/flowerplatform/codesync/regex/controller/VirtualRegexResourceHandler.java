package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_SCHEME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.VIRTUAL_REGEX_TYPE;
import static org.flowerplatform.core.file.FileControllerUtils.getRepo;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public class VirtualRegexResourceHandler extends AbstractController implements IResourceHandler {

	@Override
	public String getResourceUri(String nodeUri) {		
		return Utils.getUri(REGEX_SCHEME, getRepo(nodeUri), null);
	}

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {		
		return null;
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {			
		return new Node(nodeUri, VIRTUAL_REGEX_TYPE);
	}

	@Override
	public Object load(String resourceUri) throws Exception {		
		return null;
	}

	@Override
	public void save(Object resourceData) throws Exception {
		// do nothing
	}

	@Override
	public boolean isDirty(Object resourceData) {		
		return false;
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// do nothing
	}

}
