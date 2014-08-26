package org.flowerplatform.codesync.sdiff.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * after the removal of some node of type CanContainComment, attempt to clean/
 * propagate clean (whether this was the last dirty child or not, it tested by
 * unsetDirtyAndPropagateToParents() method itself)
 * 
 * @author Elena Posea
 */
public class CanContainCommentRemoveNodeListener extends ContainsCommentPropagator implements IRemoveNodeController {

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> serviceContext) {
		unsetDirtyAndPropagateToParents(child, serviceContext);
	}

}
