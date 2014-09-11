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
package org.flowerplatform.core.node.remote;

/**
 * @author Cristina Constantinescu
 */
public class StylePropertyWrapper extends PropertyWrapper {

	private boolean isDefault;

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	//CHECKSTYLE:OFF
	public StylePropertyWrapper setIsDefaultAs(boolean isDefault) {
		setIsDefault(isDefault);
		return this;
		//CHECKSTYLE:ON
	}
	
}