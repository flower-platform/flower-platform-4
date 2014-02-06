/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.controller;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.NodeModelAdapter;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncAddNodeController extends AddNodeController {

	public CodeSyncAddNodeController() {
		// must be invoked after the persistence controller
		setOrderIndex(50000);
	}
	
	@Override
	public void addNode(Node node, Node child) {
		if (!child.getType().equals(CodeSyncPlugin.CATEGORY)) {
			CodeSyncPlugin.getInstance().getNodeService().setProperty(child, NodeModelAdapter.ADDED, true);
		}
	}

}
