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
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreUtils.getRepoFromNode;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.CodeSyncRegexPlugin;
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
		String technology = virtualNodeHandler.getTypeSpecificPartFromNodeUri(node.getNodeUri());
		String path = CoreUtils.getRepoFromNode(node) + "/" + REGEX_CONFIGS_FOLDER + "/" + technology + "/"
				+ CodeSyncRegexConstants.REGEX_TEST_FILES_FOLDER;
		Object testFilesFolder = null;
		try {
			testFilesFolder = CorePlugin.getInstance().getFileAccessController().getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		List<Object> testFiles = CodeSyncRegexPlugin.getInstance().getRegexService().getTestFiles(testFilesFolder);
		List<Node> children = new ArrayList<Node>();
		for (Object testFile : testFiles) {
			String relativePath = CorePlugin.getInstance().getFileAccessController().getPathRelativeToFile(testFile, testFilesFolder);
			String typeSpecificPart = technology + "$" + relativePath;
			Node child = new Node(virtualNodeHandler.createVirtualNodeUri(getRepoFromNode(node),
					REGEX_TEST_FILE_NODE_TYPE, typeSpecificPart), REGEX_TEST_FILE_NODE_TYPE);
			children.add(child);
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return getChildren(node, context).size() > 0;
	}
}
