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
package org.flowerplatform.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.Context;

/**
 * Invoked by the remote method invocation backend (e.g. Flex/BlazeDS, Rest/JSON), when a call from the client arrives.
 * 
 * @author Sebastian Solomon
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 * @author Claudiu Matei 
 */
public class RemoteMethodInvocationListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteMethodInvocationListener.class);
	
	private static final Context LOGGER_CONTEXT = (Context) LoggerFactory.getILoggerFactory();

	public String getSessionId() {
		return CorePlugin.getInstance().getRequestThreadLocal().get().getSession().getId();
	}

	/**
	 * Compares the list of resources the client has with the list of resources that the client is subscribed to. For any
	 * resource that the client is not subscribed to anymore => re-subscribe.
	 * 
	 * <p>
	 * If a resource cannot be reloaded, the result of this invocation will be enriched with a list of {@link CoreConstants#RESOURCE_NODE_IDS_NOT_FOUND},
	 * so the client can react (e.g. close the obsolete editors).
	 */
	public void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		remoteMethodInvocationInfo.setStartTimestamp(new Date().getTime());

		String sessionId = getSessionId();
		List<String> clientResources = remoteMethodInvocationInfo.getResourceUris(); // list is sorted on client
		
		//temporar
		CorePlugin.getInstance().getContextThreadLocal().set(new ContextThreadLocal());
		
		if (clientResources != null) {
			List<String> serverResources = CorePlugin.getInstance().getSessionService().getResourcesSubscribedBySession(sessionId);
			List<String> notFoundResources = new ArrayList<String>();
						
			for (String clientResource : clientResources) {			
				if (serverResources.contains(clientResource)) {
					continue;
				}
				
				// the client is not subscribed to this resource anymore, maybe he went offline?
				// subscribe the client to this resource
				try {
					CorePlugin.getInstance().getResourceService().subscribeToParentResource(sessionId, clientResource, new ServiceContext<ResourceService>(CorePlugin
							.getInstance().getResourceService()));
				} catch (Exception e) {
					// the resource could not be loaded; inform the client
					notFoundResources.add(clientResource);
				}
			}
			
			if (notFoundResources.size() > 0) {
				remoteMethodInvocationInfo.getEnrichedReturnValue().put(CoreConstants.RESOURCE_NODE_IDS_NOT_FOUND, notFoundResources);
			}
		}
	}

	/**
	 * Changes {@link RemoteMethodInvocationInfo#getReturnValue()} to a map containing:
	 * <ul>
	 * 	<li> {@link CoreConstants#MESSAGE_RESULT} -> message invocation result
	 *  <li> {@link CoreConstants#UPDATES} -> map from fullResourceNodeId to a list of recent updates = all updates registered after {@link CoreConstants#LAST_UPDATE_TIMESTAMP}
	 *  <li> {{@link CoreConstants#LAST_UPDATE_TIMESTAMP} -> server timestamp of this request
	 * </ul>
	 * 
	 */
	public void postInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		ContextThreadLocal context = CorePlugin.getInstance().getContextThreadLocal().get();
		try {
			if (LOGGER.isDebugEnabled()) {
				long endTime = new Date().getTime();
				long difference = endTime - remoteMethodInvocationInfo.getStartTimestamp();
				String serviceId = remoteMethodInvocationInfo.getServiceId();
				String methodName = remoteMethodInvocationInfo.getMethodName();
				boolean log = true;
				if (methodName.equals("ping")) {
					String logPing = LOGGER_CONTEXT.getProperty("logNodeServicePingInvocation");
					log = logPing == null ? false : Boolean.parseBoolean(logPing);
				}
				if (log) {
					LOGGER.debug("[{}ms] {}.{}() invoked", new Object[] { difference, serviceId, methodName });
				}
			}
	
			Command command = context.getCommand();
			if (command != null) {
				CorePlugin.getInstance().getResourceSetService().addCommand(command);
			}	
			
			// prepare result
			remoteMethodInvocationInfo.getEnrichedReturnValue().put(CoreConstants.MESSAGE_RESULT, remoteMethodInvocationInfo.getReturnValue());
			
			Long timestampOfLastRequest = remoteMethodInvocationInfo.getTimestampOfLastRequest();
			long timestamp = new Date().getTime();		
			
			// update timestamp
			remoteMethodInvocationInfo.getEnrichedReturnValue().put(CoreConstants.LAST_UPDATE_TIMESTAMP, timestamp);
						
			// get info from header
			List<String> resourceSets = remoteMethodInvocationInfo.getResourceSets();				
			if (resourceSets != null) {		
				// only request updates if the client is subscribed to some resource
				Map<String, List<Update>> resourceNodeIdToUpdates = new HashMap<String, List<Update>>();
				for (String resourceSet : resourceSets) {
					List<Update> updates = CorePlugin.getInstance().getResourceSetService().getUpdates(resourceSet, timestampOfLastRequest);
					if (updates == null || updates.size() > 0) {
						// updates == null -> client must perform a refresh to get all necessary data
						// updates.size == 0 -> ignore, no need to add it in map
						
						resourceNodeIdToUpdates.put(resourceSet, updates);
						
						if (LOGGER.isDebugEnabled()) {
							int size = -1;
							if (updates != null) {
								size = updates.size();
							}
							LOGGER.debug("For resource = {}, timestamp = {}, sending {} updates = {}", new Object[] { resourceSet, timestamp, size, updates });
						}
					}				
				}
				if (resourceNodeIdToUpdates.size() > 0) {
					remoteMethodInvocationInfo.getEnrichedReturnValue().put(CoreConstants.UPDATES, resourceNodeIdToUpdates);
				}
			}
			
			// update timestamps for server resources
			List<String> resources = remoteMethodInvocationInfo.getResourceUris();
			if (resources != null) {
				for (String resource : resources) {
					CorePlugin.getInstance().getResourceService().setUpdateRequestedTimestamp(resource, timestamp);
				}
			}
	
			remoteMethodInvocationInfo.setReturnValue(remoteMethodInvocationInfo.getEnrichedReturnValue());
		} finally {
			Command command = context.getCommand();
			if (command != null) {
				CorePlugin.getInstance().getLockManager().unlock(command.getResourceSet());
			}
			// temporar
			CorePlugin.getInstance().getContextThreadLocal().remove();
		}
		
	}
	
}
