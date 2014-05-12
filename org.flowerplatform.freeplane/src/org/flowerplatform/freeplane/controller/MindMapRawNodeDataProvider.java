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

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.RawNodeDataProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class MindMapRawNodeDataProvider extends RawNodeDataProvider<NodeModel> {

	@Override
	public NodeModel getRawNodeData(Node node, ServiceContext<NodeService> context) {
		// get the raw resource data for the resource node
		Node resourceNode = CoreUtils.getResourceNode(node);
		MapModel model = (MapModel) CorePlugin.getInstance().getResourceService()
				.getRawResourceData(resourceNode.getFullNodeId());
		// return the node with this id
		return model.getNodeForID(node.getIdWithinResource());
	}

}