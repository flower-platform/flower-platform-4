/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.core.node.resource.in_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class InMemoryResourceService extends ResourceService {

	protected Map<String, InMemoryResourceInfo> resourceInfos = new HashMap<String, InMemoryResourceInfo>();
	
	/**
	 * Load the resource on first subscription.
	 */
	@Override
	protected void doSessionSubscribedToResource(String sessionId, String resourceUri) {
		if (getSessionsSubscribedToResource(resourceUri).isEmpty()) {
			// load on first subscription
			LOGGER.debug("First subscription; load resource {}", resourceUri);
			String scheme = Utils.getScheme(resourceUri);
			IResourceHandler resourceHandler = getResourceHandler(scheme);
			try {
				registerResourceData(resourceUri, resourceHandler.load(resourceUri));
			} catch (Exception e) {
				throw new RuntimeException("Error loading resource: " + resourceUri, e);
			}
		}
		
		InMemoryResourceInfo resourceInfo = resourceInfos.get(resourceUri);
		if (resourceInfo != null && !resourceInfo.getSessionIds().contains(sessionId)) {
			// add only if not yet added
			resourceInfo.getSessionIds().add(sessionId);
		}
	}
	
	/**
	 * Unload the resource on last unsubscription.
	 */
	@Override
	public void doSessionUnsubscribedFromResource(String sessionId, String resourceUri) {
		InMemoryResourceInfo resourceInfo = resourceInfos.get(resourceUri);
		if (resourceInfo != null) {
			resourceInfo.getSessionIds().remove(sessionId);
			if (resourceInfo.getSessionIds().isEmpty()) {
				// remove the info from the map if it was the last session
				resourceInfos.remove(resourceUri);
			}
		}
		
		if (getSessionsSubscribedToResource(resourceUri).isEmpty()) {
			// unload on last unsubscription
			LOGGER.debug("Last unsubscription; unload resource {}", resourceUri);
			String scheme = Utils.getScheme(resourceUri);
			IResourceHandler resourceHandler = getResourceHandler(scheme);
			try {
				resourceHandler.unload(resourceUri);
			} catch (Exception e) {
				throw new RuntimeException("Error unloading resource: " + resourceUri, e);
			}
		}
	}
	
	@Override
	public Object getResourceData(String resourceUri) {
		InMemoryResourceInfo resourceInfo = resourceInfos.get(resourceUri);
		if (resourceInfo == null) {
//			throw new RuntimeException("Resource " + resourceUri + " is not loaded");
			// dirty is requested from getOrPopulateProperties() for root, repo
			// which are not registered
			return null;
		}
		return resourceInfo.getResourceData();
	}


	@Override
	public void registerResourceData(String resourceUri, Object resourceData) {
		InMemoryResourceInfo resourceInfo = resourceInfos.get(resourceUri);
		if (resourceInfo == null) {
			resourceInfo = new InMemoryResourceInfo();
			resourceInfos.put(resourceUri, resourceInfo);
		}
		resourceInfo.setResourceData(resourceData);
		// get resource node
		Node resourceNode = CorePlugin.getInstance().getResourceService().getNode(resourceUri);

		// get config dirs property
		String configDirsKey = (String) resourceNode.getPropertyValue("codeSyncConfigDirsKey");
		resourceInfo.setConfigDirsKey(configDirsKey);
	}

	@Override
	public List<String> getResources() {
		return new ArrayList<>(resourceInfos.keySet());
	}

	@Override
	public List<String> getSessionsSubscribedToResource(String resourceUri) {
		InMemoryResourceInfo resourceInfo = resourceInfos.get(resourceUri);
		if (resourceInfo == null) {
			return Collections.emptyList();
		}
		return resourceInfo.getSessionIds();
	}

	@Override
	public long getUpdateRequestedTimestamp(String resourceNodeId) {
		return resourceInfos.get(resourceNodeId).getLastPing();
	}

	@Override
	public void setUpdateRequestedTimestamp(String resourceUri, long timestamp) {
		resourceInfos.get(resourceUri).setLastPing(timestamp);
	}

	/**
	 * Temporary.
	 */
	public String getConfigDirsKey(String resourceUri) {
		InMemoryResourceInfo info = resourceInfos.get(resourceUri);
		return info == null ? null : info.getConfigDirsKey();
	}
	
}