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
package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.freeplane.FreeplanePlugin.MIND_MAP_STYLE;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Sebastian Solomon
 */
public class MindMapStyleChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> serviceContext) {
		// idWithinResource == null -> path to workspace location
		NodeModel model = (NodeModel) node.getRawNodeData();
		
		Enumeration<NodeModel> styles = model.children();
		if (styles == null) {	
			return null;
		}
		List<Node> children = new ArrayList<Node>();
		while (styles.hasMoreElements()) {
			for (NodeModel styleNodeModel : styles.nextElement().getChildren()) {
				String scheme = Utils.getScheme(node.getNodeUri());
				String ssp = Utils.getSchemeSpecificPart(node.getNodeUri());
				String styleNodeUri = Utils.getUri(scheme, ssp, styleNodeModel.createID());
				Node child = new Node(styleNodeUri, MindMapConstants.MINDMAP_NODE_TYPE);
				child.setRawNodeData(styleNodeModel);
				children.add(child);
			}
		}		
		return children;
	}
	
	
	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> serviceContext) {
		if (node.getType().equals(MIND_MAP_STYLE)) {
			return false;
		}
		return true;
	}

}
