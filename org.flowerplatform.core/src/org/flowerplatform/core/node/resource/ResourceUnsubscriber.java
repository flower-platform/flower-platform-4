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

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.FlowerProperties;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.session.SessionService;

/**
 * Scheduled task that periodically checks if the subscribed clients are still active.
 * If a resource was not pinged recently, then all the clients subscribed are forcefully 
 * unsubscribed from the resource; when all the clients are removed, the resource will
 * be unloaded.
 * 
 * @author Mariana Gheorghe
 */
public class ResourceUnsubscriber extends TimerTask {

	protected static final String PROP_RESOURCE_UNSUBSCRIBER_DELAY = "resourceUnsubscriberDelay"; 
	protected static final String PROP_DEFAULT_RESOURCE_UNSUBSCRIBER_DELAY = "600000"; 
	
	/**
	 *@author Cristina Constantinescu
	 **/
	public ResourceUnsubscriber() {
		super();
		CorePlugin.getInstance().getFlowerProperties().addProperty(new FlowerProperties
				.AddIntegerProperty(PROP_RESOURCE_UNSUBSCRIBER_DELAY, PROP_DEFAULT_RESOURCE_UNSUBSCRIBER_DELAY));
	}

	@Override
	public void run() {
		long now = new Date().getTime();
		ResourceService service = CorePlugin.getInstance().getResourceService();
		List<String> resourceUris = service.getResources();
		for (String resourceUri : resourceUris) {
			long lastPing = service.getUpdateRequestedTimestamp(resourceUri);
			if (now - lastPing > Long.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(PROP_RESOURCE_UNSUBSCRIBER_DELAY))) {
				List<String> sessionIds = service.getSessionsSubscribedToResource(resourceUri);
				for (int i = sessionIds.size() - 1; i >= 0; i--) {
					String sessionId = sessionIds.get(i);
					CorePlugin.getInstance().getResourceService().sessionUnsubscribedFromResource(sessionId, resourceUri,
							new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
					CorePlugin.getInstance().getSessionService().sessionUnsubscribedFromResource(sessionId, resourceUri,
							new ServiceContext<SessionService>(CorePlugin.getInstance().getSessionService()));
				}
			}
		}
	}
	
	/**
	 *@author see class
	 **/
	public void start() {
		new Timer().schedule(this, 0, Long.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(PROP_RESOURCE_UNSUBSCRIBER_DELAY)));
	}
	
}
