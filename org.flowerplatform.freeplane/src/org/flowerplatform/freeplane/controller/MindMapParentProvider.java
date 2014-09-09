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
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IParentProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Mariana Gheorghe
 */
public class MindMapParentProvider extends AbstractController implements IParentProvider {

	@Override
	public Node getParent(Node node, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) node.getRawNodeData());
		NodeModel parentNodeModel = rawNodeData.getParentNode();
		if (parentNodeModel == null) {
			return null;
		}
		String scheme = Utils.getScheme(node.getNodeUri());
		String ssp = Utils.getSchemeSpecificPart(node.getNodeUri());
		String parentUri = scheme + ":" + ssp;
		// this statement ensures that the string after "#" is not concatenated for root node (as this node should not contain it)
		if (!parentNodeModel.getMap().getRootNode().createID().equals(parentNodeModel.createID())) {
			parentUri += "#" + parentNodeModel.createID();
		}
		IResourceHandler resourceHandler = CorePlugin.getInstance().getResourceService().getResourceHandler(scheme);
		return resourceHandler.createNodeFromRawNodeData(parentUri, parentNodeModel);
	}

}
