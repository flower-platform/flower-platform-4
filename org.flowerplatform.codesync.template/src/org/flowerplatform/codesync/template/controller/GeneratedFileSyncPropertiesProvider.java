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
package org.flowerplatform.codesync.template.controller;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public class GeneratedFileSyncPropertiesProvider extends GeneratedFileSyncPropertiesController implements
		IPropertiesProvider {

	public GeneratedFileSyncPropertiesProvider() {
		setOrderIndex(1000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if (isGeneratedFile(node)) {
			node.getProperties().put(CodeSyncConstants.ADDED, true);
		}
	}

}