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

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Sebastian Solomon
 */
public class FileRemoveNodeController extends RemoveNodeController {

	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		IFileAccessController fileAccessController = CorePlugin
				.getInstance().getFileAccessController();	
		try {
			fileAccessController.delete(fileAccessController
					.getFile(child.getIdWithinResource()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}