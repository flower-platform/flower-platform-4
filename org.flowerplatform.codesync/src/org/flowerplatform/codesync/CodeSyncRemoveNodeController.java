package org.flowerplatform.codesync;

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Sebastian Solomon
 */
public class CodeSyncRemoveNodeController extends RemoveNodeController {
	
	public CodeSyncRemoveNodeController() {
		// must be executed before others.
		setOrderIndex(-100000);
	}

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		context.getService().setProperty(child, CodeSyncConstants.REMOVED, true, new ServiceContext<NodeService>(context.getService()));
		context.add(DONT_PROCESS_OTHER_CONTROLLERS, true);
	}

}
