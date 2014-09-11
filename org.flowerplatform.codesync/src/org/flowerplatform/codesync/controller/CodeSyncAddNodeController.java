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

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class CodeSyncAddNodeController extends AbstractController implements IAddNodeController {

	/**
	 *@author Mariana Gheorghe
	 **/
	public CodeSyncAddNodeController() {
		// must be invoked after the persistence controller
		// because we need the child to be already added to the model before we set the ADDED marker
		setOrderIndex(50000);
	}
	
	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {		
		// disable the controllers during the execution of sync algorithm
		if (context.getBooleanValue(CodeSyncConstants.SYNC_IN_PROGRESS)) {
			return;
		}

		context.getService().setProperty(child, CodeSyncConstants.ADDED, true, context);
	}

}
