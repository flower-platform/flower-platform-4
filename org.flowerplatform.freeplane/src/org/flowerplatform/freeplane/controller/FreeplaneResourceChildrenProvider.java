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
package org.flowerplatform.freeplane.controller;

import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * Returns the root node from the freeplane file corresponding to the node.
 * It must be invoked <b>before</b> the {@link FileChildrenProvider}
 * and also set the {@link CoreConstants#DONT_PROCESS_OTHER_CONTROLLERS}
 * to make sure that other providers do not return children.
 * 
 * @author Mariana Gheorghe
 */
public class FreeplaneResourceChildrenProvider extends AbstractController implements IChildrenProvider {

	public FreeplaneResourceChildrenProvider() {
		super();
		// children controller must be invoked before the file children provider
		setOrderIndex(-10000);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		if (!CoreUtils.isSubscribable(node.getOrPopulateProperties())) {
			return Collections.emptyList();
		}
		
		NodeModel nodeModel = (NodeModel) node.getOrRetrieveRawNodeData();
		List<Node> children = Collections.singletonList(FreeplanePlugin.getInstance().getStandardNode(nodeModel, node.getFullNodeId()));
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return CoreUtils.isSubscribable(node.getProperties());
	}
	
}
