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
package org.flowerplatform.core.node.update.remote;

/**
 * @author Cristina Constantinescu
 */
public class PropertyUpdate extends Update {

	private String key;
	
	private Object value;
	
	private boolean isUnset;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public PropertyUpdate setKeyAs(String key) {
		this.key = key;
		return this;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public PropertyUpdate setValueAs(Object value) {
		this.value = value;
		return this;
	}
	
	public boolean getIsUnset() {
		return isUnset;
	}

	public void setIsUnset(boolean isUnset) {
		this.isUnset = isUnset;
	}
		
	public PropertyUpdate setUnsetAs(boolean isUnset) {
		this.isUnset = isUnset;
		return this;
	}
	
	@Override
	public String toString() {
		return "PropertyUpdate [key=" + key + " value=" + value + " isUnset=" + isUnset + " node=" + getFullNodeId() + ", timestamp=" + getTimestamp() + "]";
	}
	
}