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
import static org.flowerplatform.core.file.FileControllerUtils.createFileNodeUri;
import static org.flowerplatform.core.file.FileControllerUtils.getFileAccessController;
import static org.flowerplatform.core.file.FileControllerUtils.getFilePathWithRepo;
import static org.flowerplatform.core.file.FileControllerUtils.getRepo;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 * @author Mariana Gheorghe
 */
public class FileChildrenController extends AbstractController
		implements IChildrenProvider, IAddNodeController, IRemoveNodeController {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		String path = FileControllerUtils.getFilePathWithRepo(node);
		Object file = null;
		try {
			file = getFileAccessController().getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Object[] files = getFileAccessController().listFiles(file);
		List<Node> children = new ArrayList<Node>();
		for (Object object : files) {
			Node child = new Node(createFileNodeUri(FileControllerUtils.getRepo(node), getFileAccessController().getPath(object)), FILE_NODE_TYPE);
			children.add(child);
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		Object file = null;
		String path = getFilePathWithRepo(node);
		try {
			file = getFileAccessController().getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Object[] files = getFileAccessController().listFiles(file);
		if (files == null) {
			return false;
		}
		if (files.length == 1 && CoreConstants.METADATA.equals(getFileAccessController().getName(files[0]))) {
			// calculate hasChildren without metadata directory
			return false;
		
		}
		return files.length > 0;
	}
	
	@Override
	public void addNode(Node parentNode, Node child, ServiceContext<NodeService> context) {
		Object parentFile;
		String path = getFilePathWithRepo(parentNode);
		try {
			parentFile = getFileAccessController().getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!getFileAccessController().isDirectory(parentFile)) {
			parentFile = getFileAccessController().getParentFile(parentFile);
			path = getFileAccessController().getPath(parentFile);
			Node fileParentNode;
			
			if (path.length() != 0) { // parent File is not the FileSystem node
				fileParentNode = new Node(createFileNodeUri(getRepo(parentNode), path), FILE_NODE_TYPE);
			} else {
				fileParentNode = new Node(createFileNodeUri(getRepo(parentNode), null), FILE_SYSTEM_NODE_TYPE);
			}
			context.getService().addChild(fileParentNode, child, context);
			context.add(DONT_PROCESS_OTHER_CONTROLLERS, true);
			return;
		}
		
		String name = (String) context.get(NAME);
		Object fileToCreate = getFileAccessController().getFile(parentFile, name);
		child.setNodeUri(createFileNodeUri(getRepo(parentNode), getFileAccessController().getPath(fileToCreate)));
		boolean isDir = (boolean) context.get(FILE_IS_DIRECTORY);
		
		if (getFileAccessController().exists(fileToCreate)) {
			throw new RuntimeException("There is already a file with the same name in this location.");
		} else if (!getFileAccessController().createFile(fileToCreate, isDir)) {
			throw new RuntimeException("The filename, directory name, or volume label syntax is incorrect");
		}
		child.getOrPopulateProperties();
	}
	
	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		try {
			getFileAccessController().delete(getFileAccessController().getFile(getFilePathWithRepo(child)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
