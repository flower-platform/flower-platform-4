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
package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_LEGEND_CHILD;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.ADDED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.REMOVED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MODIFIED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MODIFIED_BODY;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MODIFIED_CHILDREN;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.ADDED_COMMENT;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Creates the list of legend's node children and set legend's node properties.
 * 
 * @author Alexandra Topoloaga
 */
public class StructureDiffLegendController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

	/**
	 * @author see class
	 */
	public StructureDiffLegendController() {
		setOrderIndex(-1000);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		String repo = CoreUtils.getRepoFromNode(node);
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		children.add(virtualNodeHandler.createNodeFromRawNodeData(virtualNodeHandler.createVirtualNodeUri(repo, STRUCTURE_DIFF_LEGEND_CHILD, ADDED), null));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(virtualNodeHandler.createVirtualNodeUri(repo, STRUCTURE_DIFF_LEGEND_CHILD, REMOVED), null));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(virtualNodeHandler.createVirtualNodeUri(repo, STRUCTURE_DIFF_LEGEND_CHILD, MODIFIED), null));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(virtualNodeHandler.createVirtualNodeUri(repo, STRUCTURE_DIFF_LEGEND_CHILD, MODIFIED_BODY), null));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(virtualNodeHandler.createVirtualNodeUri(repo, STRUCTURE_DIFF_LEGEND_CHILD, MODIFIED_CHILDREN), null));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(virtualNodeHandler.createVirtualNodeUri(repo, STRUCTURE_DIFF_LEGEND_CHILD, ADDED_COMMENT), null));
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(CoreConstants.NAME, "Legend");
	}
}