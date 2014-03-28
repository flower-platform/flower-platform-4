package org.flowerplatform.freeplane.style.controller;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class StyleRootPropertiesProvider extends PropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		node.getProperties().put(CoreConstants.NAME, "styles[read only]");
		node.getProperties().put(CoreConstants.HAS_CHILDREN, true);
	}

}
