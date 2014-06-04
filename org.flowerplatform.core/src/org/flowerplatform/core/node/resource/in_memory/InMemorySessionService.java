package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.session.SessionService;

/**
 * @author Mariana Gheorghe
 */
public class InMemorySessionService extends SessionService {

	private Map<String, SessionInfo> sessionInfos = new HashMap<String, SessionInfo>();
	
	@Override
	protected void doSessionCreated(String sessionId) {
		sessionInfos.put(sessionId, new SessionInfo());
	}
	
	@Override
	protected void doSessionRemoved(String sessionId) {
		sessionInfos.remove(sessionId);
	}
	
	@Override
	public void updateSessionProperty(String sessionId, String property, Object value) {
		sessionInfos.get(sessionId).getProperties().put(property, value);
	}

	@Override
	public Object getSessionProperty(String sessionId, String property) {
		SessionInfo sessionInfo = sessionInfos.get(sessionId);
		if (sessionInfo != null) {
			return sessionInfo.getProperties().get(property);
		}
		return null;
	}
	
	@Override
	public void sessionSubscribedToResource(String sessionId, String resourceUri, ServiceContext<SessionService> context) {
		SessionInfo sessionInfo = sessionInfos.get(sessionId);
		if (sessionInfo == null) {
			sessionInfo = new SessionInfo();
			sessionInfos.put(sessionId, sessionInfo);
		}
		if (!sessionInfo.getSubscribedResourceNodeIds().contains(resourceUri)) {
			sessionInfo.getSubscribedResourceNodeIds().add(resourceUri);
		}
	}

	@Override
	public void sessionUnsubscribedFromResource(String sessionId, String resourceUri, ServiceContext<SessionService> context) {
		sessionInfos.get(sessionId).getSubscribedResourceNodeIds().remove(resourceUri);
	}

	@Override
	public List<String> getSubscribedSessions() {
		return new ArrayList<>(sessionInfos.keySet());
	}
	
	@Override
	public List<String> getResourcesSubscribedBySession(String sessionId) {
		SessionInfo sessionInfo = sessionInfos.get(sessionId);
		if (sessionInfo == null) {
			return new ArrayList<String>();
		}
		return sessionInfo.getSubscribedResourceNodeIds();
	}
}
