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
package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceHolder {

	/**
	 * @author see class
	 */
	IResourceHandler getResourceHandler(String scheme);
	
	/**
	 * @author see class
	 */
	Node getNode(String nodeUri);
	
	/**
	 * @author see class
	 */
	Object getResourceData(String resourceUri);
	
	/**
	 * @author see class
	 */
	void registerResourceData(String resourceUri, Object resourceData);
	
}