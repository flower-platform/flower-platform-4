package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitTagsPropertiesProvider extends AbstractController implements IPropertiesProvider  {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(NAME, ResourcesPlugin.getInstance().getMessage("git.tags"));
		node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/" + "tags.gif"));
		node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND,true);
	}

}
