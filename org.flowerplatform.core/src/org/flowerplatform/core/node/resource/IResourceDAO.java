package org.flowerplatform.core.node.resource;

import java.util.List;

import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceDAO {

	public void sessionSubscribedToResource(String resourceNodeId, String sessionId);
	
	public void sessionUnsubscribedFromResource(String resourceNodeId, String sessionId);
	
	public Object getRawResourceData(String resourceNodeId);
	
	public String getResourceCategory(String resourceNodeId);
	
	public void setRawResourceData(String resourceNodeId, Object rawResourceData, String resourceCategory);
	
	public void unsetRawResourceData(String resourceNodeId);
	
	public long getUpdateRequestedTimestamp(String resourceNodeId);
	
	public void sessionCreated(String sessionId);
	
	public void sessionRemoved(String sessionId);
	
	public List<String> getSubscribedSessions();
	
	public Object getSessionProperty(String sessionId, String property);
	
	public void updateSessionProperty(String sessionId, String property, Object value);
	
	public List<String> getSessionsSubscribedToResource(String resourceNodeId);
	
	public List<String> getResourcesSubscribedBySession(String sessionId);
	
	public List<String> getResources();
	
	public void addUpdate(String resourceNodeId, Update update);
	
	public List<Update> getUpdates(String resourceNodeId, long timestampOfLastRequest, long timestampOfThisRequest);

}
