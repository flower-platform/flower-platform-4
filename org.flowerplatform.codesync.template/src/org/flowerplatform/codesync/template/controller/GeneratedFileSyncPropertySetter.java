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

import java.util.Map;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public class GeneratedFileSyncPropertySetter extends GeneratedFileSyncPropertiesController implements IPropertySetter {

	public GeneratedFileSyncPropertySetter() {
		setOrderIndex(-1000);
	}

	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
		if (isGeneratedFile(node)) {
			properties.remove(CodeSyncConstants.SYNC);
			properties.remove(CodeSyncConstants.CONFLICT);
			properties.remove(CodeSyncConstants.CHILDREN_SYNC);
			properties.remove(CodeSyncConstants.CHILDREN_CONFLICT);
		}
		
		// no more properties to set, stop the controllers
		if (properties.isEmpty()) {
			context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		// nothing to do
	}

}