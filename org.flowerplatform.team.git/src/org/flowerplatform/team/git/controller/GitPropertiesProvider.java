package org.flowerplatform.team.git.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

import static org.flowerplatform.core.CoreConstants.NAME;

/**
 * @author Cojocea Marius Eduard
 */
public class GitPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(NAME,ResourcesPlugin.getInstance().getMessage("git.git"));
	}

}
