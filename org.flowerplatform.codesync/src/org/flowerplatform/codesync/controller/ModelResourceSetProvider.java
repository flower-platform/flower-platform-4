package org.flowerplatform.codesync.controller;

import static org.flowerplatform.core.CoreConstants.RESOURCE_SET;

import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class ModelResourceSetProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String resourceSet = "model@" + FileControllerUtils.getRepo(node);
		node.getProperties().put(RESOURCE_SET, resourceSet);
	}

}
