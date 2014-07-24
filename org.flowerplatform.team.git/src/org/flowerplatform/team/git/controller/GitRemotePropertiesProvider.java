package org.flowerplatform.team.git.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitRemotePropertiesProvider extends AbstractController implements IPropertiesProvider  {

	@Override
	public void populateWithProperties(Node node,ServiceContext<NodeService> context) {
		// TODO populate remote with properties
		
	}

}
