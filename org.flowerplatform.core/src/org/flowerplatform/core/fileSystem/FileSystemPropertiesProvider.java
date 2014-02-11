package org.flowerplatform.core.fileSystem;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileSystemPropertiesProvider extends PropertiesProvider<Object> {

	@Override
	public void populateWithProperties(Node node, Object rawNodeData) {		
		node.getOrCreateProperties().put("body", "fileSystem");
		node.getOrCreateProperties().put("hasChildren", true);
		node.getOrCreateProperties().put("isRoot", false);
	}
	
}