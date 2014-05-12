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
package org.flowerplatform.core.file;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Sebastian Solomon
 */
public class FileChildrenProvider extends ChildrenProvider {
	private static IFileAccessController fileAccessController = CorePlugin
			.getInstance().getFileAccessController();

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		String path;
		path = node.getIdWithinResource();

		Object file = null;
		try {
			file = fileAccessController.getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Object[] files = fileAccessController.listFiles(file);
		List<Node> children = new ArrayList<Node>();
		for (Object object : files) {
			if (CoreConstants.METADATA.equals(fileAccessController.getName(object)) && path == null) {
				// don't show metadata directory from workspace
				continue;
			}
			children.add(new Node(CoreConstants.FILE_NODE_TYPE, 
					node.getType().equals(CoreConstants.FILE_SYSTEM_NODE_TYPE) ? node.getFullNodeId() : node.getResource(),
					fileAccessController.getPath(object), null));
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		Object file = null;
		try {
			file = fileAccessController.getFile(node.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Object[] files = fileAccessController.listFiles(file);
		if (files == null) {
			return false;
		}
		if (files.length == 1 && CoreConstants.METADATA.equals(fileAccessController.getName(files[0]))) {
			// calculate hasChildren without metadata directory
			return false;
		
		}
		return files.length > 0;
	}

}