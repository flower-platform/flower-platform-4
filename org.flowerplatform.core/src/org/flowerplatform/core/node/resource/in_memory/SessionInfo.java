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
package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mariana Gheorghe
 */
public class SessionInfo {

	private List<String> subscribedResourceUris = new ArrayList<String>();
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public List<String> getSubscribedResourceUris() {
		return subscribedResourceUris;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
}
