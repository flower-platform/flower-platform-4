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
package org.flowerplatform.core.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public abstract class SessionService implements ISessionListener {

	protected final static Logger logger = LoggerFactory.getLogger(SessionService.class);
	
	public SessionService() {
		CorePlugin.getInstance().addSessionListener(this);
	}
	
	@Override
	public void sessionCreated(String sessionId) {
		HttpServletRequest request = CorePlugin.getInstance().getRequestThreadLocal().get();
		if (request == null) {
			// request doesn't come from FlowerMessageBrokerServlet, ignore it
			return;
		}
		logger.debug("Session created {}", sessionId);
		
		doSessionCreated(sessionId);
		
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		updateSessionProperty(sessionId, "ip", ipAddress);
	}

	protected abstract void doSessionCreated(String sessionId);
	
	@Override
	public void sessionRemoved(String sessionId) {
		logger.debug("Session removed {}", sessionId);
		
		List<String> resources = getResourcesSubscribedBySession(sessionId);
		for (int i = resources.size() - 1; i >= 0; i--) {
			String resource = resources.get(i);
			CorePlugin.getInstance().getResourceService().sessionUnsubscribedFromResource(sessionId, resource,
					new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
			sessionUnsubscribedFromResource(resource, sessionId, new ServiceContext<SessionService>(this));
		}
		
		doSessionRemoved(sessionId);
	}

	protected abstract void doSessionRemoved(String sessionId);
	
	public abstract void updateSessionProperty(String sessionId, String property, Object value);
	
	public abstract Object getSessionProperty(String sessionId, String property);
	
	/**
	 * Paired with {@link ResourceService#sessionSubscribedToResource(String, java.net.URI)}.
	 */
	public abstract void sessionSubscribedToResource(String sessionId, String resourceUri, ServiceContext<SessionService> context);
	
	/**
	 * Paired with {@link ResourceService#sessionUnsubscribedFromResource(String, java.net.URI)}.
	 */
	public abstract void sessionUnsubscribedFromResource(String sessionId, String resourceUri, ServiceContext<SessionService> context);
	
	public abstract List<String> getSubscribedSessions();
	
	public abstract List<String> getResourcesSubscribedBySession(String sessionId);
}