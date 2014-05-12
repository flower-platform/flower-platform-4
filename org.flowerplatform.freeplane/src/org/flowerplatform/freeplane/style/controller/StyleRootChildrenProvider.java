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
package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.freeplane.FreeplanePlugin.STYLE_ROOT_NODE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.freeplane.features.map.NodeModel;

/**
 * @author Sebastian Solomon
 */
public class StyleRootChildrenProvider extends ChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> list = new ArrayList<>(); 
		if (((NodeModel)node.getOrRetrieveRawNodeData()).getMap().getRootNode().equals(node.getOrRetrieveRawNodeData())) {
			Node styleNode = new Node(STYLE_ROOT_NODE , null, new Node(node.getResource()).getIdWithinResource(), null);
			list.add(styleNode);
		}
		return list;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return ((NodeModel)node.getOrRetrieveRawNodeData()).getMap().getRootNode().equals(node.getOrRetrieveRawNodeData());
	}

}