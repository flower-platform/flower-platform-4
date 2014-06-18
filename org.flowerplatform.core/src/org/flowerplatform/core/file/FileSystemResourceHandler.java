package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.FILE_SYSTEM_NODE_TYPE;
import static org.flowerplatform.core.file.FileControllerUtils.getFilePathWithoutRepo;
import static org.flowerplatform.core.file.FileControllerUtils.getRepo;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class FileSystemResourceHandler implements IResourceHandler {

	@Override
	public String getResourceUri(String nodeUri) {
		return Utils.getUri(FILE_SCHEME, getRepo(nodeUri), null);
	}

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		return null;
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		String type = getFilePathWithoutRepo(nodeUri) == null ? FILE_SYSTEM_NODE_TYPE : FILE_NODE_TYPE; 
		return new Node(nodeUri, type);
	}

	@Override
	public Object load(String resourceUri) throws Exception {
		return null;
	}

	@Override
	public void save(Object resourceData) throws Exception {
		// nothing to do
	}

	@Override
	public boolean isDirty(Object resourceData) {
		return false;
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// nothing to do
	}

}
