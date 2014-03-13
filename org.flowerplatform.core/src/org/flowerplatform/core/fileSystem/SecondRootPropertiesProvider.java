package org.flowerplatform.core.fileSystem;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.NAME;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;

import java.util.Map;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class SecondRootPropertiesProvider extends PropertiesProvider {


	@Override
	public void populateWithProperties(Node node, Map<String, Object> options) {		
		node.getProperties().put(TEXT, "root");
		node.getProperties().put(NAME, "root");
		node.getProperties().put(HAS_CHILDREN, true);
	}
		
}
