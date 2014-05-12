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
package org.flowerplatform.codesync;

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Sebastian Solomon
 */
public class CodeSyncRemoveNodeController extends RemoveNodeController {
	
	public CodeSyncRemoveNodeController() {
		// must be executed before others.
		setOrderIndex(-100000);
	}

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		context.getService().setProperty(child, CodeSyncConstants.REMOVED, true, new ServiceContext<NodeService>(context.getService()));
		context.add(DONT_PROCESS_OTHER_CONTROLLERS, true);
	}

}