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
package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Add the current node as a child of the state node.
 * 
 * @author Elena Posea
 */
public class AddAsChildOfStateNodeAction extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		Node currentNode = (Node) param.context.get(CodeSyncRegexConstants.CURRENT_NODE);
		// make sure to also copy the properties
		serviceContext.setContext(currentNode.getProperties());
		@SuppressWarnings("unchecked")
		State top = (State) ((ArrayList<Object>) param.context.get(CodeSyncRegexConstants.STATE_STACK)).get(0);
		CorePlugin.getInstance().getNodeService().addChild((Node) top.node, currentNode, serviceContext);
	}

}
