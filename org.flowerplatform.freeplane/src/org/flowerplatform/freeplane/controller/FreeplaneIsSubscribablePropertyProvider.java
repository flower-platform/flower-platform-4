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
package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.file.FilePropertiesProvider;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.freeplane.features.url.UrlManager;

/**
 * Sets the <code>IS_SUBSCRIBABLE</code> property to <code>true</code>
 * if the node is a file with the .mm extension. Needs to be invoked <b>after</b>
 * the {@link FilePropertiesProvider} responsible with setting the <code>NAME</code>
 * and <code>IS_DIRECTORY</code> properties for the node.
 * 
 * @author Mariana Gheorghe
 */
public class FreeplaneIsSubscribablePropertyProvider extends PropertiesProvider {

	public FreeplaneIsSubscribablePropertyProvider() {
		super();
		
		// must be invoked after the file properties providers
		// so the name and is_directory flag are already set
		setOrderIndex(10000);
	}
	
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		boolean isDirectory = (boolean) node.getProperties().get(CoreConstants.FILE_IS_DIRECTORY);
		if (!isDirectory) {
			String path = (String) node.getProperties().get(CoreConstants.NAME);
			if (path.endsWith(UrlManager.FREEPLANE_FILE_EXTENSION)) {
				node.getProperties().put(CoreConstants.IS_SUBSCRIBABLE, true);
			}
		}
	}
	
}