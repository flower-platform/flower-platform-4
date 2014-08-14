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
package org.flowerplatform.core.preference.remote;

import org.flowerplatform.core.node.remote.PropertyWrapper;

/**
 * @see PreferencePropertiesProvider
 * @author Cristina Constantinescu
 */
public class PreferencePropertyWrapper extends PropertyWrapper {

	private boolean isUsed;

	public boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
	/**
	 * @author see class
	 */
	public PreferencePropertyWrapper setIsUsedAs(boolean isUsed) {
		setIsUsed(isUsed);
		return this;
	}
	
	/**
	 * @author see class
	 */
	public PreferencePropertyWrapper() {
		super();
	}	
	
}