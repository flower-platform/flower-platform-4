package org.flowerplatform.core.repository;

import static org.flowerplatform.core.NodePropertiesConstants.TEXT;

import java.util.Map;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RootPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node, Map<String, Object> options) {
		node.getProperties().put(TEXT, "root");
	}

}
