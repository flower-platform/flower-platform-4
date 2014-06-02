package org.flowerplatform.core.node.resource.in_memory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionSubscribedToResource(String sessionId, URI resourceUri) {
		sessionInfos.get(sessionId).getSubscribedResourceNodeIds().add(resourceUri.toString());
	}

	@Override
	public void sessionUnsubscribedFromResource(String sessionId, URI resourceUri) {
		sessionInfos.get(sessionId).getSubscribedResourceNodeIds().remove(resourceUri.toString());
	}

}
