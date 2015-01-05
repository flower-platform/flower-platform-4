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
package org.flowerplatform.text;

import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;

import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Cristina Constantinescu
 */
public class TextFileSubscribableProvider extends FileSubscribableProvider {

	/**
	 *@author Bogdan Manica
	 **/
	public TextFileSubscribableProvider(String extension, String scheme, String contentType, boolean insertAtBeginning) {
		super(extension, scheme, contentType, insertAtBeginning);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if ((boolean) node.getProperties().get(FILE_IS_DIRECTORY)) {
			return;
		}
		super.populateWithProperties(node, context);
	}
	
}