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
package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.NodeService;

/**
 * The property value is wrapped to allow other {@link IPropertySetter} to modify it.
 *  
 * @see NodeService#setProperty(org.flowerplatform.core.node.remote.Node, String, Object)
 * @author Cristina Constantinescu
 */
public class PropertyValueWrapper {

	private Object propertyValue;

	public PropertyValueWrapper(Object value) {
		this.propertyValue = value;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}
		
}
