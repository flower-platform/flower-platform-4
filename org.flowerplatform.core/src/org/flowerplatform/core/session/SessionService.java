package org.flowerplatform.core.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public abstract class SessionService implements ISessionListener {

	protected final static Logger logger = LoggerFactory.getLogger(SessionService.class);
	
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
			sessionUnsubscribedFromResource(resources.get(i), sessionId, new ServiceContext<SessionService>(this));
		}
		
		doSessionRemoved(sessionId);
	}

	protected abstract void doSessionRemoved(String sessionId);
	
	public abstract void updateSessionProperty(String sessionId, String property, Object value);
	
	public abstract Object getSessionProperty(String sessionId, String property);
	
	public abstract void sessionSubscribedToResource(String sessionId, String resourceUri, ServiceContext<SessionService> context);
	
	public abstract void sessionUnsubscribedFromResource(String sessionId, String resourceUri, ServiceContext<SessionService> context);
	
	public abstract List<String> getSubscribedSessions();
	
	public abstract List<String> getResourcesSubscribedBySession(String sessionId);
}
