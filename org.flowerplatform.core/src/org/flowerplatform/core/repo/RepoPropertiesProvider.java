package org.flowerplatform.core.repo;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;
import static org.flowerplatform.core.NodePropertiesConstants.NAME;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RepoPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {		
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		Object file;
		try {
			file = fileAccessController.getFile(node.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.getProperties().put(TEXT, fileAccessController.getName(file));
		node.getProperties().put(NAME, fileAccessController.getName(file));
		node.getProperties().put(HAS_CHILDREN, true);
	}
	
}