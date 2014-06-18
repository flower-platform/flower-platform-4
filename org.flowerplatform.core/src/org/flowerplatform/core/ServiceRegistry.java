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
package org.flowerplatform.core;


import java.util.HashMap;
import java.util.Map;

/**
 * Central registry for application services.
 * 
 * <p>
 * As there is no type check of the client side (i.e. the callers of the services),
 * <strong>please pay special attention when the signature of a service method changes</strong>,
 * because you won't get any compiler error. Look carefully in the whole workspace (using CTRL + H
 * and the name of the service or method), in order to modify ALL places that invokes that particular
 * method.
 * 
 * @author Cristian Spiescu
 */
public class ServiceRegistry {
	
	/**
	 * Contains the registered services.
	 */
	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * Registers a new service.
	 */
	public void registerService(String id, Object serviceInstance) {
		map.put(id, serviceInstance);
	}

	/**
	 * Gets the service by its string id.
	 * 
	 * @return The registered service or <code>null</code> if nothing found.
	 */
	public Object getService(String id) {
		return map.get(id);
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public Map<String, Object> getMap() {
		return map;
	}
	
}