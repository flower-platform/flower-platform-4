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
package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IController;

/**
 * @author Mariana Gheorghe
 */
public class PropertyDescriptorDefaultPropertyValueProvider extends AbstractController implements IDefaultPropertyValueProvider {

	/**
	 * @author Solomon Sebastian
	 */
	@Override
	public Object getDefaultValue(Node node, String property, ServiceContext<NodeService> context) {
		List<AbstractController> propertyDescriptorList = context.getService().getPropertyDescriptors(node);
		
		for (IController element : propertyDescriptorList) {
			PropertyDescriptor propertyDescriptor = (PropertyDescriptor) element;
			if (propertyDescriptor.getName().equals(property)) {
				return propertyDescriptor.getDefaultValue();
			}
		}
		return null;
	}

}
