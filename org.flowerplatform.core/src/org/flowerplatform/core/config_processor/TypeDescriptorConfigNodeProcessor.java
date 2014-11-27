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
package org.flowerplatform.core.config_processor;

import static org.flowerplatform.core.CoreConstants.NAME;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * Register a type (or category) descriptor in the registry and return it.
 * 
 * @author Mariana Gheorghe
 */
public class TypeDescriptorConfigNodeProcessor extends AbstractController implements
		IConfigNodeProcessor<TypeDescriptor, TypeDescriptorRegistry> {

	public TypeDescriptorConfigNodeProcessor() {
		setSharedControllerAllowed(true);
	}
	
	@Override
	public TypeDescriptor processConfigNode(Node node, TypeDescriptorRegistry registry, ServiceContext<NodeService> context) {
		String name = (String) node.getPropertyValue(NAME);
		TypeDescriptor descriptor = null;
		if (name.startsWith(UtilConstants.CATEGORY_PREFIX)) {
			descriptor = registry.getOrCreateCategoryTypeDescriptor(name);
		} else {
			descriptor = registry.getOrCreateTypeDescriptor(name);
		}
		return descriptor;
	}

}
