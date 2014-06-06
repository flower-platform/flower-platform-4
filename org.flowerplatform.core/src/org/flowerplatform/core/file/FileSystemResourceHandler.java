package org.flowerplatform.core.file;

import org.flowerplatform.core.node.resource.ResourceHandler;
import org.flowerplatform.util.Utils;

public class FileSystemResourceHandler extends ResourceHandler {

	@Override
	protected Object doLoad(String resourceUri) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getResourceData(Object resource, String nodeUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Object resourceData, String nodeUri) {
		return Utils.getScheme(nodeUri);
	}

	@Override
	protected boolean isDirty(Object resource) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void doSave(Object resource) throws Exception {
		// TODO Auto-generated method stub

	}

}
