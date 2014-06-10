package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.resource.ResourceHandler;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class FileSystemResourceHandler extends ResourceHandler {

	@Override
	public String getResourceUri(String nodeUri) {
		return Utils.getUri(FILE_SCHEME, FileControllerUtils.getRepo(nodeUri), null);
	}
	
	@Override
	protected Object doLoad(String resourceUri) throws Exception {
		return null;
	}

	@Override
	public Object getResourceData(Object resource, String nodeUri) {
		return null;
	}

	@Override
	public String getType(Object resourceData, String nodeUri) {
		if (FileControllerUtils.getFilePath(nodeUri) == null) {
			return CoreConstants.FILE_SYSTEM_NODE_TYPE;
		}
		return CoreConstants.FILE_NODE_TYPE;
	}

	@Override
	protected boolean isDirty(Object resource) {
		return false;
	}

	@Override
	protected void doSave(Object resource) throws Exception {
		// nothing to do
	}

}
