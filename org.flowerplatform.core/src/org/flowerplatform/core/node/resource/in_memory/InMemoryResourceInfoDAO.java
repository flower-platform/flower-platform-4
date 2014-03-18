package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.resource.IResourceInfoDAO;
import org.flowerplatform.core.node.update.remote.Update;

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
		SessionInfo sessionInfo = sessionIdToSessionInfo.get(sessionId);
		if (sessionInfo != null) {
			return sessionInfo.getProperties().get(property);
		}
		return null;
	}

	@Override
	public void updateSessionProperty(String sessionId, String property, Object value) {
		getSessionInfoForSessionId(sessionId).getProperties().put(property, value);
	}

	@Override
	public Object getRawResourceData(String rootNodeId) {
		RootNodeInfo info = resourceNodeIdToInfo.get(rootNodeId);
		if (info != null) {
			return info.getRawResourceData();
		}
		return null;
	}

	@Override
	public String getResourceCategory(String resourceNodeId) {
		RootNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			return info.getResourceCategory();
		}
		return null;
	}
	
	@Override
	public void setRawResourceData(String rootNodeId, Object rawResourceData, String resourceCategory) {
		getRootNodeInfoForRootNodeId(rootNodeId).setRawResourceData(rawResourceData);
		getRootNodeInfoForRootNodeId(rootNodeId).setResourceCategory(resourceCategory);
	}
	
	@Override
	public void unsetRawResourceData(String rootNodeId) {
		resourceNodeIdToInfo.remove(rootNodeId);
	}
	
	@Override
	public long getUpdateRequestedTimestamp(String rootNodeId) {
		RootNodeInfo info = resourceNodeIdToInfo.get(rootNodeId);
		if (info != null) {
			return info.getUpdateRequestedTimestamp();
		}
		return -1;
	}
	
	@Override
	public List<String> getSessionsSubscribedToResource(String rootNodeId) {
		RootNodeInfo info = resourceNodeIdToInfo.get(rootNodeId);
		if (info != null) {
			return info.getSessions();
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> getResourcesSubscribedBySession(String sessionId) {
		SessionInfo sessionInfo = sessionIdToSessionInfo.get(sessionId);
		if (sessionInfo != null) {
			return sessionInfo.getSubscribedResourceNodeIds();
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> getResources() {
		return new ArrayList<String>(resourceNodeIdToInfo.keySet());
	}
	
	@Override
	public void addUpdate(String rootNodeId, Update update) {
		RootNodeInfo info = getRootNodeInfoForRootNodeId(rootNodeId);
		info.getUpdates().add(update);
	}

	@Override
	public List<Update> getUpdates(String rootNodeId, long timestampOfLastRequest, long timestampOfThisRequest) {
		List<Update> updates = null;
		RootNodeInfo info = resourceNodeIdToInfo.get(rootNodeId);
		if (info != null) {
			updates = info.getUpdates();
		}
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		if (updates == null) {
			return updatesAddedAfterLastRequest;
		}
		
		boolean updatesBeforeLastRequestFound = timestampOfLastRequest != -1;
		// iterate updates reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = updates.size() - 1; i >= 0; i--) {
			Update update = updates.get(i);			
			if (update.getTimestamp() <= timestampOfLastRequest) { 
				// an update was registered before timestampOfLastRequest
				updatesBeforeLastRequestFound = true;
				break;
			}
			updatesAddedAfterLastRequest.add(0, update);
		}
		
		info.setUpdateRequestedTimestamp(timestampOfThisRequest);
		
		// if no updates registered before -> tell client to perform full refresh
		return updatesBeforeLastRequestFound ? updatesAddedAfterLastRequest : null;
	}

	/**
	 * Lazy-init mechanism. Called from methods that need to add/update info.
	 * Getters should instead check if the info exists, to avoid memory leaks.
	 */
	private RootNodeInfo getRootNodeInfoForRootNodeId(String rootNodeId) {
		RootNodeInfo info = resourceNodeIdToInfo.get(rootNodeId);
		if (info == null) {
			info = new RootNodeInfo();
			resourceNodeIdToInfo.put(rootNodeId, info);
		}
		return info;
	}
	
	/**
	 * Lazy-init mechanism. Called from methods that need to add/update info.
	 * Getters should instead check if the info exists, to avoid memory leaks.
	 */
	private SessionInfo getSessionInfoForSessionId(String sessionId) {
		SessionInfo info = sessionIdToSessionInfo.get(sessionId);
		if (info == null) {
			info = new SessionInfo();
			sessionIdToSessionInfo.put(sessionId, info);
		}
		return info;
	}

}
