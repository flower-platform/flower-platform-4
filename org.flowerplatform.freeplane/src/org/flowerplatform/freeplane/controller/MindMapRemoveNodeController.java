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
package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapRemoveNodeController extends AbstractController implements IRemoveNodeController {

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) child.getRawNodeData());
		rawNodeData.getParentNode().remove(rawNodeData.getParentNode().getChildPosition(rawNodeData));
		rawNodeData.getMap().unregistryNodes(rawNodeData);
		rawNodeData.getMap().setSaved(false);
	}

}
