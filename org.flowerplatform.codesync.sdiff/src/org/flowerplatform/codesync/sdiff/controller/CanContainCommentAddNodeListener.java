package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.NODE_URI_TO_BE_IGNORED;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * Whenever you add a new node of type comment, that new node has to be marked as dirty,
 * and this flag has to be propagated to parents.
 * 
 * @author Elena Posea
 */
public class CanContainCommentAddNodeListener extends ContainsCommentPropagator implements IAddNodeController {

	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> serviceContext) {
		if (child.getType().equals(COMMENT)) {
			setDirtyAndPropagateToParents(child, serviceContext.add(NODE_URI_TO_BE_IGNORED, child.getNodeUri()));
		}
	}
}
