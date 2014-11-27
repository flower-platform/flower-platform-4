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

import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_CHILD_TYPE;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_DYNAMIC_CHILD_TYPE;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_ICON;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_LABEL;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * Create an {@link AddChildDescriptor} based on the config node and add it to the type descriptor.
 * 
 * @author Mariana Gheorghe
 */
public class AddChildDescriptorConfigNodeProcessor extends AbstractController implements
		IConfigNodeProcessor<AddChildDescriptor, TypeDescriptor> {

	@Override
	public AddChildDescriptor processConfigNode(Node node, TypeDescriptor type, ServiceContext<NodeService> context) {
		AddChildDescriptor addChild = new AddChildDescriptor()
			.setChildTypeAs((String) node.getPropertyValue(META_TYPE_FEATURE_CHILD_TYPE))
			.setDynamicChildTypeAs((String) node.getPropertyValue(META_TYPE_FEATURE_DYNAMIC_CHILD_TYPE))
			.setLabelAs((String) node.getPropertyValue(META_TYPE_FEATURE_LABEL))
			.setIconAs((String) node.getPropertyValue(META_TYPE_FEATURE_ICON));
		type.addAdditiveController(CoreConstants.ADD_CHILD_DESCRIPTOR, addChild);
		return addChild;
	}

}
