package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CAN_CONTAIN_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.IMG_TYPE_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.NODE_URI_TO_BE_IGNORED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.SHOULD_NOT_BE_CONSIDERED_DIRTY;
import static org.flowerplatform.core.CoreConstants.ICONS;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
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
		context.getService().setProperty(node, CAN_CONTAIN_COMMENT, true, context);
		node.getProperties().put(ICONS, CodeSyncSdiffPlugin.getInstance().getImagePath(IMG_TYPE_COMMENT));

	}

	// private String append(String icon, String decorator) {
	// return CodeSyncJavaPlugin.getInstance().getImageComposerUrl(icon,
	// getImagePath(decorator));
	// }

	@Override
	public void unsetChildrenDirty(Node node, ServiceContext<NodeService> context) {
		context.getService().unsetProperty(node, CAN_CONTAIN_COMMENT, context);
		context.getService().unsetProperty(node, ICONS, context);
	}

	@Override
	public boolean isDirty(Node node, ServiceContext<NodeService> serviceContext) {
		if (node.getNodeUri().equals(serviceContext.getContext().get(NODE_URI_TO_BE_IGNORED)))
			return false;
		return node.getType().equals(CodeSyncSdiffConstants.COMMENT) && (!serviceContext.getContext().containsKey(SHOULD_NOT_BE_CONSIDERED_DIRTY));
	}

	@Override
	public boolean isChildrenDirty(Node node, ServiceContext<NodeService> serviceContext) {
		Boolean b = (Boolean) node.getPropertyValue(CAN_CONTAIN_COMMENT); // is
																			// something
																			// that
																			// has
																			// a
																			// child
																			// comment,
																			// and
																			// it
																			// should
																			// not
																			// be
																			// ignored
		return b != null && b && !(node.getNodeUri().equals(serviceContext.getContext().get(NODE_URI_TO_BE_IGNORED)));
	}

}
