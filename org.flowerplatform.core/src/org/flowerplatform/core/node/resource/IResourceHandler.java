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
package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceHandler {

	/**
	 *@author see class
	 **/
	String getResourceUri(String nodeUri);
	
	/**
	 *@author see class
	 **/
	Object getRawNodeDataFromResource(String nodeUri, Object resourceData);
	
	/**
	 * Creates a new {@link Node} and sets it <code>rawNodeData</code>,
	 * URI and type.
	 */
	Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData);

	/**
	 *@author see class
	 **/
	Object load(String resourceUri) throws Exception;
	
	/**
	 *@author see class
	 **/
	void save(Object resourceData) throws Exception;
	
	/**
	 *@author see class
	 **/
	boolean isDirty(Object resourceData);
	
	/**
	 *@author see class
	 **/
	void unload(Object resourceData) throws Exception;
	
}