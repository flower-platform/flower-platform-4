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

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * After the removal of some node of type CanContainComment, attempt to clean/
 * propagate clean (whether this was the last dirty child or not, is tested by
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
