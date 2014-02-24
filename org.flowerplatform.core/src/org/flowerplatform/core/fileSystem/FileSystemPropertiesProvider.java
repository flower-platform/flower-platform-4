package org.flowerplatform.core.fileSystem;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileSystemPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {		
		node.getProperties().put("body", "fileSystem");
		node.getProperties().put("hasChildren", true);
		node.getProperties().put("isRoot", false);
	}
	
}