package org.flowerplatform.core.node.root_node.in_memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.resource.IResourceInfoDAO;
import org.flowerplatform.core.node.resource.RootNodeInfo;

/**
 * Keeps in-memory maps of {@link RootNodeInfo}s and information about subscribed clients (sessions).
 * 
 * @author Mariana Gheorghe
 */
public class InMemoryResourceInfoDAO implements IResourceInfoDAO {

	private Map<String, RootNodeInfo> resourceNodeIdToInfo = new HashMap<String, RootNodeInfo>();

	private Map<String, SessionInfo> sessionIdToSessionInfo = new HashMap<String, SessionInfo>();

	@Override
	public void sessionSubscribedToResource(String rootNodeId, String sessionId) {
		if (!getRootNodeInfoForRootNodeId(rootNodeId).getSessions().contains(sessionId)) {
			getRootNodeInfoForRootNodeId(rootNodeId).getSessions().add(sessionId);
		}
		if (!getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().contains(rootNodeId)) {
			getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().add(rootNodeId);
		}
	}

	@Override
	public void sessionUnsubscribedFromResource(String rootNodeId, String sessionId) {
		getRootNodeInfoForRootNodeId(rootNodeId).getSessions().remove(sessionId);
		getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().remove(rootNodeId);
	}

	@Override
	public void sessionCreated(String sessionId) {
		// nothing to do; session info will be lazy-initialized
	}

	@Override
	public void sessionRemoved(String sessionId) {
		sessionIdToSessionInfo.remove(sessionId);
	}

	@Override
	public List<String> getSubscribedSessions() {
		return new ArrayList<>(sessionIdToSessionInfo.keySet());
	}
	
	@Override
	public Object getSessionProperty(String sessionId, String property) {
		return getSessionInfoForSessionId(sessionId).getProperties().get(property);
	}

	@Override
	public void updateSessionProperty(String sessionId, String property, Object value) {
		getSessionInfoForSessionId(sessionId).getProperties().put(property, value);
	}

	@Override
	public Object getRawResourceData(String rootNodeId) {
		return getRootNodeInfoForRootNodeId(rootNodeId).getRawResourceData();
	}

	@Override
	public void setRawResourceData(String rootNodeId, Object rawResourceData) {
		getRootNodeInfoForRootNodeId(rootNodeId).setRawResourceData(rawResourceData);
	}
	
	@Override
	public List<String> getSessionsSubscribedToResource(String rootNodeId) {
		return getRootNodeInfoForRootNodeId(rootNodeId).getSessions();
	}

	@Override
	public List<String> getResourcesSubscribedBySession(String sessionId) {
		return getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds();
	}

	private RootNodeInfo getRootNodeInfoForRootNodeId(String rootNodeId) {
		RootNodeInfo info = resourceNodeIdToInfo.get(rootNodeId);
		if (info == null) {
			info = new RootNodeInfo();
			resourceNodeIdToInfo.put(rootNodeId, info);
		}
		return info;
	}
	
	private SessionInfo getSessionInfoForSessionId(String sessionId) {
		SessionInfo info = sessionIdToSessionInfo.get(sessionId);
		if (info == null) {
			info = new SessionInfo();
			sessionIdToSessionInfo.put(sessionId, info);
		}
		return info;
	}

}
