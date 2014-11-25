/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CONTAINS_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.NODE_URI_TO_BE_IGNORED;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.DirtyPropagatorController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * This class describes the behavior of comment propagation: what does "dirty" mean, the logic/way of propagating clean/dirty.
 * 
 * @author Elena Posea
 */
public class ContainsCommentPropagator extends DirtyPropagatorController {

	@Override
	public void setDirty(Node node, ServiceContext<NodeService> serviceContext) {
		// nothing to do
	}

	@Override
	public void unsetDirty(Node node, ServiceContext<NodeService> serviceContext) {
		// I can't unset dirty on a comment; comment is, because of the fact that it is a comment, dirty
		// but I don't want isDirty to return true for this node, because it doesn't matter what it is, I want to erase it anyway
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
		// we always ignore the current node/ the node on which we invoked unsetDirty as it should not count as dirty/ we want to erase it now
		return b != null && b && !(node.getNodeUri().equals(serviceContext.getContext().get(NODE_URI_TO_BE_IGNORED)));
	}

}
