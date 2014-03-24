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

import org.flowerplatform.codesync.CodeSyncPropertiesConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class CodeSyncAddNodeController extends AddNodeController {

	public CodeSyncAddNodeController() {
		// must be invoked after the persistence controller
		// because we need the child to be already added to the model before we set the ADDED marker
		setOrderIndex(50000);
	}
	
	@Override
	public void addNode(Node node, Node child, Node insertBeforeNode) {		
		CorePlugin.getInstance().getNodeService().setProperty(child, CodeSyncPropertiesConstants.ADDED, true, CorePlugin.getInstance().getNodeService().getControllerInvocationOptions());
	}

}
