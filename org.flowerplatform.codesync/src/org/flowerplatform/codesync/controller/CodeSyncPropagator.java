package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.ADDED;
import static org.flowerplatform.codesync.CodeSyncConstants.CHILDREN_SYNC;
import static org.flowerplatform.codesync.CodeSyncConstants.ORIGINAL_SUFFIX;
import static org.flowerplatform.codesync.CodeSyncConstants.SYNC;
import static org.flowerplatform.codesync.CodeSyncConstants.REMOVED;
import static org.flowerplatform.codesync.CodeSyncConstants.NODE_URI_TO_BE_IGNORED;

import org.flowerplatform.core.DirtyPropagatorController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Elena Posea
 */
public class CodeSyncPropagator extends DirtyPropagatorController {

	@Override
	public void setDirty(Node node, ServiceContext<NodeService> serviceContext) {
		serviceContext.getService().setProperty(node, SYNC, false, new ServiceContext<NodeService>(serviceContext.getService()));
	}

	@Override
	public void unsetDirty(Node node, ServiceContext<NodeService> serviceContext) {
		serviceContext.getService().setProperty(node, SYNC, true, new ServiceContext<NodeService>(serviceContext.getService()));
	}

	@Override
	public void setChildrenDirty(Node node, ServiceContext<NodeService> serviceContext) {
		serviceContext.getService().setProperty(node, CHILDREN_SYNC, false, new ServiceContext<NodeService>(serviceContext.getService()));
	}

	@Override
	public void unsetChildrenDirty(Node node, ServiceContext<NodeService> serviceContext) {
		serviceContext.getService().setProperty(node, CHILDREN_SYNC, true, new ServiceContext<NodeService>(serviceContext.getService()));
	}

	@Override
	public boolean isDirty(Node node, ServiceContext<NodeService> serviceContext) {
		if (node.getNodeUri().equals(serviceContext.getContext().get(NODE_URI_TO_BE_IGNORED))) {
			return false;
		}

		if (!isSync(node)) {
			// already set
			return true;
		}

		// added/removed nodes are not sync
		if (isAdded(node) || isRemoved(node)) {
			return true;
		}

		// check if it has any property.original set
		boolean sync = true;
		for (String property : node.getProperties().keySet()) {
			if (isOriginalPropertyName(property)) {
				sync = false;
			}
		}
		if (!sync) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isChildrenDirty(Node node, ServiceContext<NodeService> serviceContext) {
		return !hasFlagTrue(node, CHILDREN_SYNC);
	}

	public static boolean isSync(Node node) {
		return hasFlagTrue(node, SYNC);
	}

	public static boolean isChildrenSync(Node node) {
		return hasFlagTrue(node, CHILDREN_SYNC);
	}

	public static boolean isAdded(Node node) {
		return hasFlagTrue(node, ADDED);
	}

	public static boolean isRemoved(Node node) {
		return hasFlagTrue(node, REMOVED);
	}

	private static boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getPropertyValue(flag);
		return b != null && b;
	}

	public static boolean isOriginalPropertyName(String property) {
		return property.endsWith(ORIGINAL_SUFFIX);
	}

}
