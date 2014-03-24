/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.getOriginalPropertyName;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.setSyncFalseAndPropagateToParents;
import static org.flowerplatform.codesync.controller.CodeSyncControllerUtils.setSyncTrueAndPropagateToParents;
import static org.flowerplatform.codesync.feature_provider.FeatureProvider.FEATURE_PROVIDER;

import java.util.Map;

import org.flowerplatform.codesync.CodeSyncPropertiesConstants;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncPropertySetter extends PropertySetter {

	public CodeSyncPropertySetter() {
		// invoked before the persistence controllers
		// to cache the current value of the property before it is overwritten
		setOrderIndex(-100000);
	}
	
	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper wrapper, ServiceContext context) {		
		NodeService service = (NodeService) CorePlugin.getInstance().getNodeService();
		
		// if the node is newly added or marked removed => propagate sync flag false
		if (CodeSyncPropertiesConstants.REMOVED.equals(property) || CodeSyncPropertiesConstants.ADDED.equals(property)) {
			setSyncFalseAndPropagateToParents(node);
			return;
		}
		
		// check if property is synchronizable
		if (!isSyncProperty(node, property)) {
			return;
		}
		
		boolean isOriginalPropertySet = false;
		Object originalValue = null;
		String originalProperty = getOriginalPropertyName(property);
		// get the original value from property.original or property
		if (node.getOrPopulateProperties().containsKey(originalProperty)) {
			isOriginalPropertySet = true;
			originalValue = node.getOrPopulateProperties().get(originalProperty);
		} else if (node.getOrPopulateProperties().containsKey(property)) {
			originalValue = node.getOrPopulateProperties().get(property);
		} else {
			originalValue = wrapper.getPropertyValue();
		}
		
		if (!Utils.safeEquals(originalValue, wrapper.getPropertyValue())) {
			if (!isOriginalPropertySet) {
				// trying to set a different value; keep the old value in property.original if it does not exist
				service.setProperty(node, originalProperty, originalValue, new ServiceContext());
				setSyncFalseAndPropagateToParents(node);
			}
		} else {
			if (isOriginalPropertySet) {
				// trying to set the same value as the original (a revert operation); unset the original value
				service.unsetProperty(node, originalProperty, new ServiceContext());
				setSyncTrueAndPropagateToParents(node);
			}
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext context) {
		// nothing to do
	}
	
	private boolean isSyncProperty(Node node, String property) {
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(node.getType());
		if (descriptor == null) {
			return false;
		}
		
		FeatureProvider featureProvider = descriptor.getSingleController(FEATURE_PROVIDER, node);
		if (featureProvider.getValueFeatures(node).contains(property)) {
			return true;
		}
		return false;
	}
	
}
