package org.flowerplatform.core.repository;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RepositoryPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext context) {		
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		Object file;
		try {
			file = fileAccessController.getFile(node.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.getProperties().put(CoreConstants.NAME, fileAccessController.getName(file));
	}
	
}