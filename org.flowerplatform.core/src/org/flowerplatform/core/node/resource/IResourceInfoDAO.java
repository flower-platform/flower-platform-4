package org.flowerplatform.core.node.resource;

import java.util.List;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceInfoDAO {

	public void sessionSubscribedToResource(String rootNodeId, String sessionId);
	
	public void sessionUnsubscribedFromResource(String rootNodeId, String sessionId);
	
	public Object getRawResourceData(String rootNodeId);
	
	public void setRawResourceData(String rootNodeId, Object rawResourceData);
	
	public void sessionCreated(String sessionId);
	
	public void sessionRemoved(String sessionId);
	
	public List<String> getSubscribedSessions();
	
	public Object getSessionProperty(String sessionId, String property);
	
	public void updateSessionProperty(String sessionId, String property, Object value);
	
	public List<String> getSessionsSubscribedToResource(String rootNodeId);
	
	public List<String> getResourcesSubscribedBySession(String sessionId);

}
