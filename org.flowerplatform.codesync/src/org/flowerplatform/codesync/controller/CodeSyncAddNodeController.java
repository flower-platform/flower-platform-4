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

import java.util.Map;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.NodeModelAdapter;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class CodeSyncAddNodeController extends AddNodeController {

	public CodeSyncAddNodeController() {
		// must be invoked after the persistence controller and update controller
		setOrderIndex(500000);
	}
	
	@Override
	public void addNode(Node node, Map<String, Object> properties, Node insertBeforeNode) {
		Node child = (Node) properties.get(CorePlugin.NODE_KEY);
		if (child == null) {
			throw new RuntimeException(String.format("Property %s should be contained in map, probably added by the persistence AddNodeController!", CorePlugin.NODE_KEY));
		}		
		if (!child.getType().equals(CodeSyncPlugin.CATEGORY)) {
			CorePlugin.getInstance().getNodeService().setProperty(child, NodeModelAdapter.ADDED, true);
		}
	}

}
