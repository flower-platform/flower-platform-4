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
package org.flowerplatform.util.diff_update;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class PropertiesDiffUpdate extends DiffUpdate {

	private Map<String, Object> properties = new HashMap<>();

	public Map<String, Object> getProperties() {
		return properties;
	}

	public PropertiesDiffUpdate() {
		setType("updated");
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	/**
	 * 
	 * @author see class
	 *
	 */
	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	@Override
	public String toString() {
		return super.toString() + "[properties=" + properties + "]";
	}
	
}
