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
package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.core.file.FileControllerUtils.getFileAccessController;
import static org.flowerplatform.core.file.FileControllerUtils.getFilePathWithRepo;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Elena Posea
 *
 */
public class RegexTechnologyPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		Object file;
		try {
			file = getFileAccessController().getFile(getFilePathWithRepo(node));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.getProperties().put(CoreConstants.NAME, new VirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri()));
		node.getProperties().put(CoreConstants.HAS_CHILDREN, getFileAccessController().hasChildren(file));
	}
}
