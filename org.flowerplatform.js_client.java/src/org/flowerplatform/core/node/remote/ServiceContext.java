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

/**
 * @author Cristina Constantinescu
 */
public class ServiceContext extends HashMap<String, Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4379432176883270799L;

	public void add(String key, Object value) {		
		put(key, value);		
	}

}