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
package org.flowerplatform.core.node.update.remote;

import org.flowerplatform.core.CoreConstants;

/**
 * @author Cristina Constantinescu
 */
public class PropertyUpdate extends Update {

	private String key;
	
	private Object value;

	/**
	 * @author Claudiu Matei
	 */
	private Object oldValue;

	/**
	 * @author Claudiu Matei
	 */
	private boolean hasOldValue;
	
	private boolean isUnset;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	//CHECKSTYLE:OFF
	public PropertyUpdate setKeyAs(String key) {
		this.key = key;
		return this;
	}
	//CHECKSTYLE:ON
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	//CHECKSTYLE:OFF
	public PropertyUpdate setValueAs(Object properyUpdateValue) {
		this.value = properyUpdateValue;
		return this;
	}
	//CHECKSTYLE:ON

	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * No standard setter was created, in order to avoid serialization of this field 
	 * 
	 * @author Claudiu Matei
	 */
	public PropertyUpdate setOldValueAs(Object propertyOldValue) {
		this.oldValue = propertyOldValue;
		return this;
	}

	public boolean getHasOldValue() {
		return hasOldValue;
	}
	
	/**
	 * No standard setter was created, in order to avoid serialization of this field 
	 * 
	 * @author Claudiu Matei
	 */
	//CHECKSTYLE:OFF
	public PropertyUpdate setHasOldValueAs(boolean hasOldValue) {
		this.hasOldValue = hasOldValue;
		return this;
	}
	//CHECKSTYLE:ON
	
	public boolean getIsUnset() {
		return isUnset;
	}

	public void setIsUnset(boolean isUnset) {
		this.isUnset = isUnset;
	}
		
	//CHECKSTYLE:OFF
	public PropertyUpdate setUnsetAs(boolean isUnset) {
		this.isUnset = isUnset;
		return this;
		//CHECKSTYLE:ON
		
	}
	
	/**
	 *@author see class
	 **/
	public PropertyUpdate() {
		super();
		this.setType(CoreConstants.UPDATE_PROPERTY);
	}
	
	@Override
	public String toString() {
		return "PropertyUpdate [key=" + key + " value=" + value + " isUnset=" + isUnset + " node=" + getFullNodeId() + ", timestamp=" + getTimestamp() + "]";
	}
	
}
