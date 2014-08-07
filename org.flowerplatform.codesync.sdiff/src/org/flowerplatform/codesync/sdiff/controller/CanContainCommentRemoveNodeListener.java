package org.flowerplatform.codesync.sdiff.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Elena Posea
 */
public class CanContainCommentRemoveNodeListener extends ContainsCommentPropagator implements IRemoveNodeController {

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> serviceContext) {
		unsetDirtyAndPropagateToParents(child, serviceContext);
	}

}
