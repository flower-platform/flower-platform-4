package org.flowerplatform.core.fileSystem;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;
import static org.flowerplatform.core.NodePropertiesConstants.NAME;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileSystemPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {		
		node.getProperties().put(TEXT, CorePlugin.FILE_SYSTEM_NODE_TYPE);
		node.getProperties().put(NAME, CorePlugin.FILE_SYSTEM_NODE_TYPE);
		node.getProperties().put(HAS_CHILDREN, true);
	}
	
}