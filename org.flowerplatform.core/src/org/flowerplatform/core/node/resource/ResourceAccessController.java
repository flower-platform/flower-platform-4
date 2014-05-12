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

import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Responsible with working with raw resource data (load on the first client subscribed and unload
 * on the last client unsubscribed, save and reload).
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceAccessController extends AbstractController {

	public abstract void firstClientSubscribed(String resourceNodeId, ServiceContext<ResourceService> context) throws Exception;
	
	public abstract void lastClientUnubscribed(String resourceNodeId, ServiceContext<ResourceService> context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract void save(String resourceNodeId, ServiceContext<ResourceService> context);
	
	public abstract void reload(String resourceNodeId, ServiceContext<ResourceService> context) throws Exception;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract boolean isDirty(String resourceNodeId, ServiceContext<ResourceService> context);
	
}