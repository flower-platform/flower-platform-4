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
package org.flowerplatform.core.preference;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.preference.remote.PreferencePropertyWrapper;
import org.flowerplatform.util.controller.AbstractController;

/**
 * This provider considers that a node's raw properties have been already stored by an earlier provider (persistence/merged mechanism). <br>
 * 
 * Raw Properties (listed ascending based on their priority, x = preference name):
 * <ul>
 * 	<li> x.default -> default preference value
 *  <li> x.global -> global preference value
 *  <li> x.user -> user preference value
 * </ul>
 * 
 * <p>
 * Processes default/global/user preferences by wrapping them in {@link PreferencePropertyWrapper}s.
 * 
 * <p>
 * Preference value will be calculated based on this three preferences priorities (the user preference has the greatest priority).
 * 
 * <p>
 * Note: default preference must be always available
 * 
 * @see PreferencePropertyWrapper
 * 
 * @author Cristina Constantinescu
 */
public class PreferencePropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String defaultPropertyName = getProperty(node, CoreConstants.DEFAULT_SUFFIX);
		if (defaultPropertyName == null) {
			// no default property name found -> stop
			return;
		}
		Object defaultPropertyValue = node.getProperties().get(defaultPropertyName);
				
		String globalPropertyName = getProperty(node, CoreConstants.GLOBAL_SUFFIX);
		Object globalPropertyValue = null;
		if (globalPropertyName != null) {
			globalPropertyValue = node.getProperties().get(globalPropertyName);			
		}
		
		String userPropertyName = getProperty(node, CoreConstants.USER_SUFFIX);
		Object userPropertyValue = null;
		if (userPropertyName != null) {
			userPropertyValue = node.getProperties().get(userPropertyName);
		}
		
		String propertyName = defaultPropertyName.substring(0, defaultPropertyName.lastIndexOf(CoreConstants.DEFAULT_SUFFIX));
		String mergedPropertyName;
		
		if (userPropertyValue != null) {
			mergedPropertyName = userPropertyName;
		} else if (globalPropertyValue != null) {
			mergedPropertyName = globalPropertyName;
		} else {
			mergedPropertyName = defaultPropertyName;
		}
		
		if (userPropertyName == null) {
			userPropertyName = propertyName + CoreConstants.USER_SUFFIX;
			userPropertyValue = "";
		}
		node.getProperties().put(userPropertyName, new PreferencePropertyWrapper().setIsUsedAs(mergedPropertyName.equals(userPropertyName)).setValueAs(userPropertyValue));
				
		if (globalPropertyName == null) {
			globalPropertyName = propertyName + CoreConstants.GLOBAL_SUFFIX;
			globalPropertyValue = "";
		}
		node.getProperties().put(globalPropertyName, new PreferencePropertyWrapper().setIsUsedAs(mergedPropertyName.equals(globalPropertyName)).setValueAs(globalPropertyValue));
		
		node.getProperties().put(defaultPropertyName, new PreferencePropertyWrapper().setIsUsedAs(mergedPropertyName.equals(defaultPropertyName)).setValueAs(defaultPropertyValue));
		
		node.getProperties().put(propertyName, node.getProperties().get(mergedPropertyName));
	}

	private String getProperty(Node node, String suffix) {
		for (String property : node.getProperties().keySet()) {
			if (property.endsWith(suffix)) {
				return property;
			}
		}
		return null;
	}
	
}