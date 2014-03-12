package org.flowerplatform.core.node.resource;

import java.util.List;

import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceInfoDAO {

	public void sessionSubscribedToResource(String rootNodeId, String sessionId);
	
	public void sessionUnsubscribedFromResource(String rootNodeId, String sessionId);
	
	public Object getRawResourceData(String rootNodeId);
	
	public void setRawResourceData(String rootNodeId, Object rawResourceData);
	
	public long getUpdateRequestedTimestamp(String rootNodeId);
	
	public void sessionCreated(String sessionId);
	
	public void sessionRemoved(String sessionId);
	
	public List<String> getSubscribedSessions();
	
	public Object getSessionProperty(String sessionId, String property);
	
	public void updateSessionProperty(String sessionId, String property, Object value);
	
	public List<String> getSessionsSubscribedToResource(String rootNodeId);
	
	public List<String> getResourcesSubscribedBySession(String sessionId);
	
	public List<String> getResources();
	
	public void addUpdate(String rootNodeId, Update update);
	
	public List<Update> getUpdates(String rootNodeId, long timestampOfLastRequest, long timestampOfThisRequest);

}
