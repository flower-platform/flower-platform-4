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
package org.flowerplatform.codesync.controller;

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public class CodeSyncRemoveNodeController extends AbstractController implements IRemoveNodeController {
	
	/**
	 *@author see class
	 **/
	public CodeSyncRemoveNodeController() {
		// must be executed before others.
		setOrderIndex(-100000);
	}

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		// disable the controllers during the execution of sync algorithm
		if (context.getBooleanValue(CodeSyncConstants.SYNC_IN_PROGRESS)) {
			return;
		}
		
		context.getService().setProperty(child, CodeSyncConstants.REMOVED, true, new ServiceContext<NodeService>(context.getService()));
		context.add(DONT_PROCESS_OTHER_CONTROLLERS, true);
	}

}
