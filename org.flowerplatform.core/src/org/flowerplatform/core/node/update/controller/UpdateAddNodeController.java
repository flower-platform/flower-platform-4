/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.core.node.update.controller;

import static org.flowerplatform.core.CoreConstants.UPDATE_CHILD_ADDED;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;

public class UpdateAddNodeController extends AddNodeController {
		
	public UpdateAddNodeController() {
		// must be invoked last; otherwise the modification may not be fully/correctly recorded
		setOrderIndex(100000);
	}
	
	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {		
		Node resourceNode = CoreUtils.getResourceNode(node);
		String insertBeforeFullNodeId = (String) context.get(CoreConstants.INSERT_BEFORE_FULL_NODE_ID);
		if (resourceNode != null) {
			CorePlugin.getInstance().getResourceService()
				.addUpdate(resourceNode.getFullNodeId(), 
						new ChildrenUpdate()
							.setTypeAs(UPDATE_CHILD_ADDED)
							.setTargetNodeAs(child)
							.setFullTargetNodeAddedBeforeIdAs(insertBeforeFullNodeId)
							.setFullNodeIdAs(node.getFullNodeId()));
			
		}
	}

}