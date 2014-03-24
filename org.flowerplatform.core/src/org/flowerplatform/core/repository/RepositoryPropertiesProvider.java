package org.flowerplatform.core.repository;

import static org.flowerplatform.core.NodePropertiesConstants.NAME;

import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RepositoryPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node, Map<String, Object> options) {		
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		Object file;
		try {
			file = fileAccessController.getFile(node.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.getProperties().put(NAME, fileAccessController.getName(file));
	}
	
}