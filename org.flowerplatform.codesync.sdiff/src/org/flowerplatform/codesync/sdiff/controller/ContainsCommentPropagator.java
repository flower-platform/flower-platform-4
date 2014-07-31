package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CONTAINS_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.NODE_URI_TO_BE_IGNORED;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.DirtyPropagatorController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Elena Posea
 */
public class ContainsCommentPropagator extends DirtyPropagatorController {

	@Override
	public void setDirty(Node node, ServiceContext<NodeService> serviceContext) {
		// nothing to do
	}

	@Override
	public void unsetDirty(Node node, ServiceContext<NodeService> serviceContext) {
		// if(node.getType().equals(COMMENT)) // if it is not a comment, isDirty
		// will return false anyway, but isChildrenDirty won't
		serviceContext.add(NODE_URI_TO_BE_IGNORED, node.getNodeUri());
	}

	@Override
	public void setChildrenDirty(Node node, ServiceContext<NodeService> context) {
		context.getService().setProperty(node, CONTAINS_COMMENT, true, context);
	}

	@Override
	public void unsetChildrenDirty(Node node, ServiceContext<NodeService> context) {
		context.getService().unsetProperty(node, CONTAINS_COMMENT, context);
	}

	@Override
	public boolean isDirty(Node node, ServiceContext<NodeService> serviceContext) {
		if (node.getNodeUri().equals(serviceContext.getContext().get(NODE_URI_TO_BE_IGNORED))) {
			return false;
		}
		return node.getType().equals(CodeSyncSdiffConstants.COMMENT);
	}

	@Override
	public boolean isChildrenDirty(Node node, ServiceContext<NodeService> serviceContext) {
		Boolean b = (Boolean) node.getPropertyValue(CONTAINS_COMMENT); 
		// is something that has a child comment, and it should not be ignored
		return b != null && b && !(node.getNodeUri().equals(serviceContext.getContext().get(NODE_URI_TO_BE_IGNORED)));
	}

}
