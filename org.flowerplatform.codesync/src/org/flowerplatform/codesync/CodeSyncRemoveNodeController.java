package org.flowerplatform.codesync;

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class CodeSyncRemoveNodeController extends RemoveNodeController {

	@Override
	public void removeNode(Node node, Node child, ServiceContext context) {
		CorePlugin.getInstance().getNodeService().setProperty(node, CodeSyncConstants.REMOVED, true, new ServiceContext());
		context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
	}

}