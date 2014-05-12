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

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SYSTEM_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.NAME;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Sebastian Solomon
 */
public class FileAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node parentNode, Node child, ServiceContext<NodeService> context) {
		IFileAccessController fileAccessController = CorePlugin.getInstance()
				.getFileAccessController();
		Object parentFile;

		try {
			parentFile = fileAccessController.getFile(parentNode.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!fileAccessController.isDirectory(parentFile)) {
			parentFile = fileAccessController.getParentFile(parentFile);
			String path = fileAccessController.getPath(parentFile);
			Node fileParentNode;
			
			if (path.length() != 0) { // parent File is not the FileSystem node
				fileParentNode = new Node(FILE_NODE_TYPE, parentNode.getResource(), path, null);
			} else {
				fileParentNode = new Node(FILE_SYSTEM_NODE_TYPE, CoreConstants.SELF_RESOURCE, null, null);
			}
			context.getService().addChild(fileParentNode, child, context);
			context.add(DONT_PROCESS_OTHER_CONTROLLERS, true);
			return;
		}

		String name = (String) context.get(NAME);
		Object fileToCreate = fileAccessController.getFile(parentFile, name);
		child.setIdWithinResource(fileAccessController.getPath(fileToCreate));
		boolean isDir = (boolean) context.get(FILE_IS_DIRECTORY);
		
		if (fileAccessController.exists(fileToCreate)) {
			throw new RuntimeException("There is already a file with the same name in this location.");
		} else if (!fileAccessController.createFile(fileToCreate, isDir)) {
			throw new RuntimeException("The filename, directory name, or volume label syntax is incorrect");
		}
		child.getOrPopulateProperties();
	}

}