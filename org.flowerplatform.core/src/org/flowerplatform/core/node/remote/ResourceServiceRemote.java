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
package org.flowerplatform.core.node.remote;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.util.Utils;

/**
 * @see ResourceService
 * 
 * @author Mariana Gheorghe
 */
@Path("/resourceService")
public class ResourceServiceRemote {
	
	/**
	 * @author see class
	 */
	@POST
	@Path("/subscribeToParentResource")
	@Produces(MediaType.APPLICATION_JSON)
	public SubscriptionInfo subscribeToParentResource(String nodeUri) {
		String sessionId = CorePlugin.getInstance().getRequestThreadLocal().get().getSession().getId();
		return CorePlugin.getInstance().getResourceService()
				.subscribeToParentResource(sessionId, nodeUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
	}
	
	/**
	 * This is simply a dummy method that is invoked periodically
	 * by the client. Node updates will be sent automatically
	 * via the {@link RemoteMethodInvocationListener} every time
	 * it is invoked.
	 */
	public void ping() {
		// nothing to do
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public void save(String resourceNodeId) {
		CorePlugin.getInstance().getResourceSetService().save(resourceNodeId, 
				new ServiceContext<ResourceSetService>(CorePlugin.getInstance().getResourceSetService()));
	}
	
	/**
	 *@author see class
	 **/
	public void reload(String resourceNodeId) {
		CorePlugin.getInstance().getResourceSetService().reload(resourceNodeId, 
				new ServiceContext<ResourceSetService>(CorePlugin.getInstance().getResourceSetService()));
	}

	/**
	 * @author Claudiu Matei
	 */
	public void undo(String commandNodeUri) {
		String resourceSet = Utils.getSchemeSpecificPart(commandNodeUri);
		String commandId = Utils.getFragment(commandNodeUri);
		CorePlugin.getInstance().getResourceSetService().undo(resourceSet, commandId);
	}

	/**
	 * @author Claudiu Matei
	 */
	public void redo(String commandNodeUri) {
		String resourceSet = Utils.getSchemeSpecificPart(commandNodeUri);
		String commandId = Utils.getFragment(commandNodeUri);
		CorePlugin.getInstance().getResourceSetService().redo(resourceSet, commandId);
	}

}
