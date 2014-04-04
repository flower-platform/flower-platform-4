package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.resource.IResourceDAO;
import org.flowerplatform.core.node.update.remote.Update;

/**
 * Keeps in-memory maps of {@link ResourceNodeInfo}s and information about subscribed clients (sessions).
 * 
 * @author Mariana Gheorghe
 */
public class InMemoryResourceDAO implements IResourceDAO {

	private Map<String, ResourceNodeInfo> resourceNodeIdToInfo = new HashMap<String, ResourceNodeInfo>();

	private Map<String, SessionInfo> sessionIdToSessionInfo = new HashMap<String, SessionInfo>();

	@Override
	public void sessionSubscribedToResource(String resourceNodeId, String sessionId) {
		if (!getResourceNodeInfoForResourceNodeId(resourceNodeId).getSessions().contains(sessionId)) {
			getResourceNodeInfoForResourceNodeId(resourceNodeId).getSessions().add(sessionId);
		}
		if (!getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().contains(resourceNodeId)) {
			getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().add(resourceNodeId);
		}
	}

	@Override
	public void sessionUnsubscribedFromResource(String resourceNodeId, String sessionId) {
		getResourceNodeInfoForResourceNodeId(resourceNodeId).getSessions().remove(sessionId);
		getSessionInfoForSessionId(sessionId).getSubscribedResourceNodeIds().remove(resourceNodeId);
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
	public Object getRawResourceData(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			return info.getRawResourceData();
		}
		return null;
	}

	@Override
	public String getResourceCategory(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			return info.getResourceCategory();
		}
		return null;
	}
	
	@Override
	public void setRawResourceData(String resourceNodeId, Object rawResourceData, String resourceCategory) {
		ResourceNodeInfo resourceNodeInfo = getResourceNodeInfoForResourceNodeId(resourceNodeId);
		resourceNodeInfo.setRawResourceData(rawResourceData);
		resourceNodeInfo.setResourceCategory(resourceCategory);
	}
	
	@Override
	public void unsetRawResourceData(String resourceNodeId) {
		resourceNodeIdToInfo.remove(resourceNodeId);
	}
	
	@Override
	public long getUpdateRequestedTimestamp(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			return info.getUpdateRequestedTimestamp();
		}
		return -1;
	}
	
	@Override
	public List<String> getSessionsSubscribedToResource(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
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
	public void addUpdate(String resourceNodeId, Update update) {
		ResourceNodeInfo info = getResourceNodeInfoForResourceNodeId(resourceNodeId);
		info.getUpdates().add(update);
	}

	@Override
	public List<Update> getUpdates(String resourceNodeId, long timestampOfLastRequest, long timestampOfThisRequest) {
		List<Update> updates = null;
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info != null) {
			updates = info.getUpdates();
			info.setUpdateRequestedTimestamp(timestampOfThisRequest);
		}
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		if (updates == null) {
			return updatesAddedAfterLastRequest;
		}
		
		if (timestampOfLastRequest > 0 && 
			info.getLoadedTimestamp() > timestampOfLastRequest + Integer.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(IResourceDAO.PROP_RESOURCE_UPDATES_MARGIN))) {
			// if resource was reloaded after -> tell client to perform full refresh
			return null;
		}
		
		// iterate updates reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = updates.size() - 1; i >= 0; i--) {
			Update update = updates.get(i);			
			if (update.getTimestamp() < timestampOfLastRequest) { 
				// an update was registered before timestampOfLastRequest
				break;
			}
			updatesAddedAfterLastRequest.add(0, update);
		}
		return updatesAddedAfterLastRequest;
	}

	/**
	 * Lazy-init mechanism. Called from methods that need to add/update info.
	 * Getters should instead check if the info exists, to avoid memory leaks.
	 */
	private ResourceNodeInfo getResourceNodeInfoForResourceNodeId(String resourceNodeId) {
		ResourceNodeInfo info = resourceNodeIdToInfo.get(resourceNodeId);
		if (info == null) {
			info = new ResourceNodeInfo();
			resourceNodeIdToInfo.put(resourceNodeId, info);
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
