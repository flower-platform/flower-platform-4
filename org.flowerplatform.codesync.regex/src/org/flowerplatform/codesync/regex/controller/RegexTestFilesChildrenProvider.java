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

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIGS_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TECHNOLOGY_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreUtils.getRepoFromNode;
import static org.flowerplatform.core.file.FileControllerUtils.getFileAccessController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Elena Posea
 */
public class RegexTestFilesChildrenProvider extends AbstractController implements IChildrenProvider {

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		String nodeSpecificPart = virtualNodeHandler.getTypeSpecificPartFromNodeUri(node.getNodeUri());
		String path = CoreUtils.getRepoFromNode(node) + "/" + REGEX_CONFIGS_FOLDER + "/" + nodeSpecificPart + "/"
				+ CodeSyncRegexConstants.REGEX_TEST_FILES_FOLDER;
		Object file = null;
		try {
			file = getFileAccessController().getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		List<Node> children = new ArrayList<Node>();
		Object[] files = getFileAccessController().listFiles(file);
		if (files != null) {
			for (Object object : files) {
				if (((File) object).isFile()) {
					String typeSpecificPart = nodeSpecificPart + "/" + CodeSyncRegexConstants.REGEX_MATCH_FILES_FOLDER + "/" + ((File) object).getName() + CodeSyncRegexConstants.REGEX_MATCH_EXTENSION; 
					Node child = new Node(virtualNodeHandler.createVirtualNodeUri(getRepoFromNode(node), REGEX_TEST_FILE_NODE_TYPE, typeSpecificPart),
							REGEX_TEST_FILE_NODE_TYPE);
					children.add(child);
				}
			}
		}
		return children;
	}

	protected String getResourceUri(Node node) {
		String repo = CoreUtils.getRepoFromNode(node);
		String specificPartTechnology = CorePlugin.getInstance().getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
		return CoreUtils.createNodeUriWithRepo("fpp", repo, CodeSyncRegexConstants.REGEX_CONFIGS_FOLDER + "/" + specificPartTechnology + "/"
				+ CodeSyncRegexConstants.REGEX_CONFIG_FILE);
		// D:\data\git\runtime-workspace\elena\repo1\.regex-configs\ActionScript\test-files
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		Object file = null;
		String path = CoreUtils.getRepoFromNode(node) + "/" + REGEX_CONFIGS_FOLDER;
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
}
