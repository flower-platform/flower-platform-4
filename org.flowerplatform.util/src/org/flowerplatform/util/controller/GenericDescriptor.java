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
package org.flowerplatform.util.controller;
//CHECKSTYLE:OFF
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cristian Spiescu
 */
public class GenericDescriptor extends AbstractController implements IDescriptor {
	
	private Object value;
	
	private Map<String, Object> extraInfo;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Map<String, Object> getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Map<String, Object> extraInfo) {
		this.extraInfo = extraInfo;
	}

	public GenericDescriptor(Object value) {
		super();
		this.value = value;
	}
	
	/**
	 * 
	 */
	public GenericDescriptor addExtraInfoProperty(String key, String val) {
		if (extraInfo == null) {
			extraInfo = new HashMap<String, Object>();
		}
		extraInfo.put(key, val);
		return this;
	}
	
}
