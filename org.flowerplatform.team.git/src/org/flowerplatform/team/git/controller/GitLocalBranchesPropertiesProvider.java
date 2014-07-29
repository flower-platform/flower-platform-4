package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.ICONS_PATH;
import static org.flowerplatform.team.git.GitConstants.FOLDER_ICON;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitLocalBranchesPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(NAME,ResourcesPlugin.getInstance().getMessage("git.localBranches"));
		node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl(ICONS_PATH + FOLDER_ICON));
	}

}
