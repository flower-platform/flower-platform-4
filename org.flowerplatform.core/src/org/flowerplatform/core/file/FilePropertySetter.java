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

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Sebastian Solomon
 */
public class FilePropertySetter extends PropertySetter {
	
	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper value, ServiceContext<NodeService> context) {
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		if (CoreConstants.NAME.equals(property)) {
			Object file;
			if (!node.getOrPopulateProperties().get(CoreConstants.NAME).equals(value.getPropertyValue())) {
				try {
					throw new UnsupportedOperationException();
//					file = fileAccessController.getFile(node.getIdWithinResource());
//					String parentPath = fileAccessController.getParent(file);
//					Object parent = fileAccessController.getFile(parentPath);
//					Object dest = fileAccessController.getFile(parent, value.getPropertyValue().toString());
//					if (fileAccessController.exists(dest)) {
//						throw new RuntimeException("There is already a file with the same name in this location.");
//					}
//					if (!fileAccessController.rename(file, dest)) {
//						throw new RuntimeException("The filename, directory name, or volume label syntax is incorrect");
//					}
//					node.getProperties().put(NAME, value.getPropertyValue());
//					node.setIdWithinResource(fileAccessController.getAbsolutePath(dest));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		node.getOrPopulateProperties().remove(property);
	}
	
}