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
package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.CODESYNC;
import static org.flowerplatform.codesync.CodeSyncConstants.MDA;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIGS_NODE_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Adds CodeSync nodes to the repository.
 * 
 * @author Mariana Gheorghe
 */
public class CodeSyncRepositoryChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		String repo = CoreUtils.getRepoFromNode(node);
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, CODESYNC, null), null));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, MDA, null), null));
		children.add(virtualNodeHandler.createNodeFromRawNodeData(
				virtualNodeHandler.createVirtualNodeUri(repo, REGEX_CONFIGS_NODE_TYPE, null), null));
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}