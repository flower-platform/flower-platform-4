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
package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class FileSystemNodeController extends AbstractController implements IPropertiesProvider {

	/**
	 *@author see class
	 **/
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(NAME, ResourcesPlugin.getInstance().getMessage("file.fileSystem"));
		
		List<Pair<String, String>> subscribableResources = new ArrayList<Pair<String, String>>();
		subscribableResources.add(new Pair<String, String>(node.getNodeUri(), null));
		node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
		node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
	}
	
}
