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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cristina Constantinescu
 */
public class ServiceContext<T> {

	private T service;
	
	private Map<String, Object> context;
	
	public T getService() {
		return service;
	}
	
	public void setService(T service) {
		this.service = service;
	}

	/**
	 *@author see class
	 **/
	public Map<String, Object> getContext() {
		if (context == null) {
			context = new HashMap<>();
		}
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
	/**
	 *@author see class
	 **/
	public ServiceContext() {	
	}
	
	/**
	 *@author see class
	 **/
	public ServiceContext(T service) {
		this.service = service;
	}
	
	/**
	 *@author see class
	 **/
	public boolean getBooleanValue(String key) {
		return getContext().containsKey(key) && (boolean) getContext().get(key);
	}

	/**
	 *@author see class
	 **/
	public Object get(String key) {
		return getContext().get(key);
	}
		
	/**
	 *@author see class
	 **/
	public ServiceContext<T> add(String key, Object value) {		
		getContext().put(key, value);
		return this;
	}

}