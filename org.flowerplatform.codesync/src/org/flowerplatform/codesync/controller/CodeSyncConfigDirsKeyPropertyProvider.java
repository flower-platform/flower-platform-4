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
package org.flowerplatform.codesync.controller;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncConfigDirsKeyPropertyProvider extends AbstractController implements IPropertiesProvider {

	public CodeSyncConfigDirsKeyPropertyProvider() {
		// invoke after persistence controller
		setOrderIndex(10000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String configDirs = (String) node.getProperties().get(CodeSyncConstants.CODE_SYNC_CONFIG_PROPERTY_DIRS);
		if (configDirs != null) {
			String path = CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getPathFromResourceNode(node, null);
			node.getProperties().put(CodeSyncConstants.CODE_SYNC_CONFIG_PROPERTY_DIRS_KEY,
					CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getCodeSyncConfigDirsKey(configDirs, path));
		}
	}

}