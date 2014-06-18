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
package org.flowerplatform.core.node.resource;

import java.util.List;

import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceDAO {

	static final String PROP_RESOURCE_UPDATES_MARGIN = "resourceUpdatesMargin"; 
	static final String PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN = "0"; 
	
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