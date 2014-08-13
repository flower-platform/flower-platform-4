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

import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;
import static org.flowerplatform.core.CoreConstants.RESOURCE_SET;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.FlowerProperties;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.remote.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public abstract class ResourceSetService {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ResourceSetService.class);
	
	static final String PROP_RESOURCE_UPDATES_MARGIN = "resourceUpdatesMargin"; 
	static final String PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN = "0"; 
	
	public ResourceSetService() {
		CorePlugin.getInstance().getFlowerProperties().addProperty(new FlowerProperties
				.AddIntegerProperty(PROP_RESOURCE_UPDATES_MARGIN, PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN));
	}
	
	public abstract String addToResourceSet(String resourceSet, String resourceUri);
	
	public abstract void removeFromResourceSet(String resourceSet, String resourceUri);
	
	public void save(String resourceSet, ServiceContext<ResourceSetService> context) {
		LOGGER.debug("Save resource set {}", resourceSet);
		ServiceContext<ResourceService> resourceServiceContext = new ServiceContext<ResourceService>();
		resourceServiceContext.setContext(context.getContext());
		for (String resourceUri : getResourceUris(resourceSet)) {
			CorePlugin.getInstance().getResourceService().save(resourceUri, resourceServiceContext);
		}
	}
	
	public void reload(String resourceSet, ServiceContext<ResourceSetService> context) {
		LOGGER.debug("Reload resource set {}", resourceSet);
		doReload(resourceSet);
		ServiceContext<ResourceService> resourceServiceContext = new ServiceContext<ResourceService>();
		resourceServiceContext.setContext(context.getContext());
		for (String resourceUri : getResourceUris(resourceSet)) {
			CorePlugin.getInstance().getResourceService().reload(resourceUri, resourceServiceContext);
		}
	}

	protected abstract void doReload(String resourceSet);
	
	public abstract void addUpdate(String resourceSet, Update update);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public void addUpdate(Node node, Update update, ServiceContext<?> context) {
		String resourceSet = null;
		Node resourceNode;
		if (context.getBooleanValue(NODE_IS_RESOURCE_NODE)) {
			resourceNode = node;
		} else {
			resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());
			
		}
		if (resourceNode == null) {
			return;
		}
		resourceSet = (String) resourceNode.getProperties().get(RESOURCE_SET);
		if (resourceSet == null) {
			resourceSet = resourceNode.getNodeUri();
		}		
		addUpdate(resourceSet, update);		
	}
	
	public List<Update> getUpdates(String resourceSet, long timestampOfLastRequest) {
		List<Update> updates = getUpdates(resourceSet);
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		if (updates == null) {
			return updatesAddedAfterLastRequest;
		}
		
		if (timestampOfLastRequest > 0
			&& getLoadedTimestamp(resourceSet) > timestampOfLastRequest + Integer.valueOf(CorePlugin.getInstance()
			.getFlowerProperties().getProperty(PROP_RESOURCE_UPDATES_MARGIN))) {
			// if resource was reloaded after -> tell client to perform full refresh
			return null;
		}
		
		// iterate updates reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = updates.size() - 1; i >= 0; i--) {
			Update update = updates.get(i);			
			if (update.getTimestamp() <= timestampOfLastRequest) { 
				// an update was registered before timestampOfLastRequest
				break;
			}
			updatesAddedAfterLastRequest.add(update);
		}
		return updatesAddedAfterLastRequest;
	}
	
	protected abstract List<Update> getUpdates(String resourceSet);
	
	protected abstract long getLoadedTimestamp(String resourceSet);
	
	public abstract List<String> getResourceSets();
	
	public abstract List<String> getResourceUris(String resourceSet);
	
}