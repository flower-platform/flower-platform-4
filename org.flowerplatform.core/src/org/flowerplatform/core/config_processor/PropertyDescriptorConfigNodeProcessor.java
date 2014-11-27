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

import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_LABEL;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_PROPERTY_DEFAULT_VALUE;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_PROPERTY_GROUP;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_PROPERTY_LINE_RENDERER;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_PROPERTY_READ_ONLY;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_PROPERTY_TYPE;
import static org.flowerplatform.core.CoreConstants.META_TYPE_FEATURE_PROPERTY_WRITEABLE_ON_CREATE;
import static org.flowerplatform.core.CoreConstants.NAME;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * Create a {@link PropertyDescriptor} based on the config node and add it to the type descriptor.
 * 
 * @author Mariana Gheorghe
 */
public class PropertyDescriptorConfigNodeProcessor extends AbstractController implements
		IConfigNodeProcessor<PropertyDescriptor, TypeDescriptor> {

	@Override
	public PropertyDescriptor processConfigNode(Node node, TypeDescriptor type, ServiceContext<NodeService> context) {
		PropertyDescriptor property = new PropertyDescriptor()
			.setNameAs((String) node.getPropertyValue(NAME))
			.setLabelAs((String) node.getPropertyValue(META_TYPE_FEATURE_LABEL))
			.setTypeAs((String) node.getPropertyValue(META_TYPE_FEATURE_PROPERTY_TYPE))
			.setGroupAs((String) node.getPropertyValue(META_TYPE_FEATURE_PROPERTY_GROUP))
			.setReadOnlyAs(getSafeBoolean(node.getPropertyValue(META_TYPE_FEATURE_PROPERTY_READ_ONLY)))
			.setDefaultValueAs(node.getPropertyValue(META_TYPE_FEATURE_PROPERTY_DEFAULT_VALUE))
			.setPropertyLineRendererAs((String) node.getPropertyValue(META_TYPE_FEATURE_PROPERTY_LINE_RENDERER))
			.setContributesToCreationAs(getSafeBoolean(node.getPropertyValue(META_TYPE_FEATURE_PROPERTY_WRITEABLE_ON_CREATE)));
		type.addAdditiveController(UtilConstants.FEATURE_PROPERTY_DESCRIPTORS, property);
		return property;
	}

	private boolean getSafeBoolean(Object object) {
		Boolean b = (Boolean) object;
		return b != null && b;
	}
	
}
