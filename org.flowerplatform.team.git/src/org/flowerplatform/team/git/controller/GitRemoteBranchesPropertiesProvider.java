package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.NAME;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitRemoteBranchesPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node,ServiceContext<NodeService> context) {
		node.getProperties().put(NAME,ResourcesPlugin.getInstance().getMessage("git.remoteBranches"));
		node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
	}

}
