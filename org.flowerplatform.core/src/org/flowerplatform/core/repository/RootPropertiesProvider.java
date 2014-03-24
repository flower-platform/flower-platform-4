package org.flowerplatform.core.repository;

import static org.flowerplatform.core.NodePropertiesConstants.NAME;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RootPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		node.getProperties().put(NAME, "root");
	}

}
