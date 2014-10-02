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
package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES;
import static org.flowerplatform.core.CoreConstants.IS_DIRTY;
import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.remote.SubscriptionInfo;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages resources that are requested by the clients.
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceService implements IResourceHolder {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);
	
	private Map<String, IResourceHandler> resourceHandlers = new HashMap<String, IResourceHandler>();
	
	/**
	 *@author see class
	 **/
	public void addResourceHandler(String scheme, IResourceHandler resourceHandler) {
		resourceHandlers.put(scheme, resourceHandler);
	}
	
	@Override
	public IResourceHandler getResourceHandler(String scheme) {
		IResourceHandler resourceHandler = resourceHandlers.get(scheme);
		if (resourceHandler == null) {
			throw new RuntimeException(ResourcesPlugin.getInstance().getMessage(
					"resource.error.invalidScheme", scheme));
		}
		return resourceHandler;
	}
	
	/**
	 * Delegate to a {@link IResourceHandler} based on the scheme.
	 */
	@Override
	public Node getNode(String nodeUri) {		
		return getNode(nodeUri, new ServiceContext<ResourceService>());
	}
	
	/**
	 *@author see class
	 **/
	public Node getNode(String nodeUri, ServiceContext<ResourceService> context) {
		LOGGER.debug("Get node for URI: {}", nodeUri);
	
		String scheme = Utils.getScheme(nodeUri);
		IResourceHandler resourceHandler = getResourceHandler(scheme);
		return getNode(nodeUri, resourceHandler, context);
	}
	
	/**
	 *@author see class
	 **/
	protected Node getNode(String nodeUri, IResourceHandler resourceHandler, ServiceContext<ResourceService> context) {
		String resourceUri = resourceHandler.getResourceUri(nodeUri);
		Object resourceData = resourceUri == null ? null : getResourceData(resourceUri);
		Object rawNodeData = resourceHandler.getRawNodeDataFromResource(nodeUri, resourceData);
		Node node = resourceHandler.createNodeFromRawNodeData(nodeUri, rawNodeData);
		
		if (node == null) {
			return null;
		}
		if (context.getBooleanValue(POPULATE_WITH_PROPERTIES)) {
			node.getOrPopulateProperties(new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		}
		
		return node;
	}
	
	/**
	 * Subscribes to the parent resource of the <code>node</code>.
	 * 
	 * <p>
	 * Note: there is no unsubscribe method, because if the user has two open
	 * applications with the same session ID (e.g. the client app is open in two
	 * browser tabs) and the same resource open in both applications, we do not
	 * want to unsubscribe the client.
	 * 
	 * @return a pair containing the root node, resource node and 
	 * resource set
	 */
	public SubscriptionInfo subscribeToParentResource(String sessionId, String nodeUri, ServiceContext<ResourceService> context) {
		LOGGER.debug("Subscribe session {} to parent of {}", sessionId, nodeUri);
		
		String scheme = Utils.getScheme(nodeUri);
		IResourceHandler resourceHandler = getResourceHandler(scheme);
		String resourceUri = resourceHandler.getResourceUri(nodeUri);
		if (resourceUri == null) {
			return new SubscriptionInfo(getNode(nodeUri, resourceHandler, new ServiceContext<ResourceService>().add(POPULATE_WITH_PROPERTIES, true)));
		}
		
		// subscribe
		sessionSubscribedToResource(sessionId, resourceUri, context);
		CorePlugin.getInstance().getSessionService().sessionSubscribedToResource(sessionId, resourceUri, null);
		
		// get resource node
		Node resourceNode = getNode(resourceUri, resourceHandler, new ServiceContext<ResourceService>().add(POPULATE_WITH_PROPERTIES, true));
		String resourceSet = (String) resourceNode.getProperties().get(CoreConstants.RESOURCE_SET);
		if (resourceSet == null) {
			resourceSet = resourceUri;
		}
		
		// add to resource set
		CorePlugin.getInstance().getResourceSetService().addToResourceSet(resourceSet, resourceUri);
		
		return new SubscriptionInfo(getNode(nodeUri, resourceHandler, new ServiceContext<ResourceService>().add(POPULATE_WITH_PROPERTIES, true)), resourceNode, resourceSet);
	}
	
	/**
	 * Subscribes the client with this <code>sessionId</code> to the <code>resourceUri</code>.
	 * 
	 * <p>
	 * Paired with {@link SessionService#sessionSubscribedToResource(String, String, ServiceContext)}.
	 */
	public void sessionSubscribedToResource(String sessionId, String resourceUri, ServiceContext<ResourceService> context) {
		LOGGER.debug("Subscribe session {} to resource {}", sessionId, resourceUri);
		doSessionSubscribedToResource(sessionId, resourceUri);
	}
	
	/**
	 *@author see class
	 **/
	protected abstract void doSessionSubscribedToResource(String sessionId, String resourceUri);
	
	/**
	 * Unsubscribes the client with this <code>sessionId</code> from the <code>resourceUri</code>.
	 * 
	 * <p>
	 * Paired with {@link SessionService#sessionUnsubscribedFromResource(String, String, ServiceContext)}.
	 */
	public void sessionUnsubscribedFromResource(String sessionId, String resourceUri, ServiceContext<ResourceService> context) {
		LOGGER.debug("Unsubscribe session {} from resource {}", sessionId, resourceUri);
		Node resourceNode = getNode(resourceUri);
		String resourceSet = (String) resourceNode.getProperties().get(CoreConstants.RESOURCE_SET);
		if (resourceSet == null) {
			resourceSet = resourceUri;
		}
		doSessionUnsubscribedFromResource(sessionId, resourceUri);
		
		if (getSessionsSubscribedToResource(resourceUri).isEmpty()) {
			// remove from resource set as well
			CorePlugin.getInstance().getResourceSetService().removeFromResourceSet(resourceSet, resourceUri);
		}
	}
	
	/**
	 *@author see class
	 **/
	protected abstract void doSessionUnsubscribedFromResource(String sessionId, String resourceUri);
	
	/**
	 *@author see class
	 **/
	public void save(String resourceUri, ServiceContext<ResourceService> context) {
		LOGGER.debug("Save resource {}", resourceUri);
		
		String scheme = Utils.getScheme(resourceUri);
		IResourceHandler resourceHandler = getResourceHandler(scheme);
		Object resourceData = getResourceData(resourceUri);
		try {
			resourceHandler.save(resourceData);
		} catch (Exception e) {
			throw new RuntimeException("Error saving resource: " + resourceUri, e);
		}
		
		// update isDirty property
		Node resourceNode = getNode(resourceUri, resourceHandler, new ServiceContext<ResourceService>());
		CorePlugin.getInstance().getNodeService().setProperty(
				resourceNode, 
				IS_DIRTY, 
				resourceHandler.isDirty(resourceData),
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(NODE_IS_RESOURCE_NODE, true)
				.add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
	}

	/**
	 *@author see class
	 **/
	public void reload(String resourceUri, ServiceContext<ResourceService> context) {
		LOGGER.debug("Reload resource {}", resourceUri);
		
		String scheme = Utils.getScheme(resourceUri);
		IResourceHandler resourceHandler = getResourceHandler(scheme);
		Object resourceData = getResourceData(resourceUri);
		try {
			resourceHandler.unload(resourceData);
			registerResourceData(resourceUri, resourceHandler.load(resourceUri));
		} catch (Exception e) {
			throw new RuntimeException("Error reloading resource: " + resourceUri, e);
		}
		
		// update isDirty property
		Node resourceNode = getNode(resourceUri, resourceHandler, new ServiceContext<ResourceService>());
		CorePlugin.getInstance().getNodeService().setProperty(
				resourceNode, 
				IS_DIRTY, 
				resourceHandler.isDirty(resourceData),
				new ServiceContext<NodeService>(CorePlugin.getInstance()
						.getNodeService()).add(NODE_IS_RESOURCE_NODE, true)
						.add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
	}
	
	/**
	 *@author see class
	 **/
	public boolean isDirty(String nodeUri, ServiceContext<ResourceService> serviceContext) {
		IResourceHandler resourceHandler = getResourceHandler(Utils.getScheme(nodeUri));
		String resourceUri = resourceHandler.getResourceUri(nodeUri);
		return resourceHandler.isDirty(getResourceData(resourceUri));
	}

	/**
	 *@author see class
	 **/
	public abstract List<String> getResources();
	
	/**
	 *@author see class
	 **/
	public abstract List<String> getSessionsSubscribedToResource(String resourceNodeId);

	/**
	 *@author see class
	 **/
	public abstract long getUpdateRequestedTimestamp(String resourceNodeId);
	
	/**
	 *@author see class
	 **/
	public abstract void setUpdateRequestedTimestamp(String resourceUri, long timestamp);

	/**
	 *@author see class
	 **/
	public Node getResourceNode(String nodeUri) {
		IResourceHandler resourceHandler = getResourceHandler(Utils.getScheme(nodeUri));
		return getNode(resourceHandler.getResourceUri(nodeUri), resourceHandler, new ServiceContext<ResourceService>());
	}
}
