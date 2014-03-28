package org.flowerplatform.core.repository;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RootPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		node.getProperties().put(CoreConstants.NAME, "root");
	}

}
