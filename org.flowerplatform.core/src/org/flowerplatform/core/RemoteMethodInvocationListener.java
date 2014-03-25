package org.flowerplatform.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.update.remote.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoked by the remote method invocation backend (e.g. Flex/BlazeDS, Rest/JSON), when a call from the client arrives.
 * 
 * @author Sebastian Solomon
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class RemoteMethodInvocationListener {

	private final static Logger logger = LoggerFactory.getLogger(RemoteMethodInvocationListener.class);

	public static final String LAST_UPDATE_TIMESTAMP = "timestampOfLastUpdate";
	public static final String ROOT_NODE_IDS = "resourceNodeIds";
	public static final String ROOT_NODE_IDS_NOT_FOUND = "resourceNodeIdsNotFound";
	
	public static final String MESSAGE_RESULT = "messageResult";
	public static final String UPDATES = "updates";
	
	/**
	 * Compares the list of resources the client has with the list of resources that the client is subscribed to. For any
	 * resource that the client is not subscribed to anymore => re-subscribe.
	 * 
	 * <p>
	 * If a resource cannot be reloaded, the result of this invocation will be enriched with a list of {@link #ROOT_NODE_IDS_NOT_FOUND},
	 * so the client can react (e.g. close the obsolete editors).
	 */
	public void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		remoteMethodInvocationInfo.setStartTimestamp(new Date().getTime());

		String sessionId = CorePlugin.getInstance().getRequestThreadLocal().get().getSession().getId();
		List<String> clientResourceNodeIds = remoteMethodInvocationInfo.getResourceNodeIds();
		
		if (clientResourceNodeIds != null) {
			List<String> serverResourceNodeIds = CorePlugin.getInstance().getResourceService().getResourcesSubscribedBySession(sessionId);
			List<String> notFoundResourceNodeIds = new ArrayList<String>();
			for (String clientResourceNodeId : clientResourceNodeIds) {
				if (serverResourceNodeIds.contains(clientResourceNodeId)) {
					continue;
				}
				
				// the client is not subscribed to this resource anymore, maybe he went offline?
				// subscribe the client to this resource
				try {
					CorePlugin.getInstance().getResourceService().sessionSubscribedToResource(clientResourceNodeId, sessionId, new ServiceContext());
				} catch (Exception e) {
					// the resource could not be loaded; inform the client
					notFoundResourceNodeIds.add(clientResourceNodeId);
				}
			}
			
			if (notFoundResourceNodeIds.size() > 0) {
				remoteMethodInvocationInfo.getEnrichedReturnValue().put(ROOT_NODE_IDS_NOT_FOUND, notFoundResourceNodeIds);
			}
		}
	}

	/**
	 * Changes {@link RemoteMethodInvocationInfo#getReturnValue()} to a map containing:
	 * <ul>
	 * 	<li> {@link #MESSAGE_RESULT} -> message invocation result
	 *  <li> {@link #UPDATES} -> map from fullResourceNodeId to a list of recent updates = all updates registered after {@link #LAST_UPDATE_TIMESTAMP}
	 *  <li> {{@link #LAST_UPDATE_TIMESTAMP} -> server timestamp of this request
	 * </ul>
	 * 
	 */
	public void postInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		if (logger.isDebugEnabled()) {
			long endTime = new Date().getTime();
			long difference = endTime - remoteMethodInvocationInfo.getStartTimestamp();
			String serviceId = remoteMethodInvocationInfo.getServiceId();
			String methodName = remoteMethodInvocationInfo.getMethodName();
			logger.debug("[{}ms] {}.{}() invoked", new Object[] { difference, serviceId, methodName });
		}
		
		// prepare result
		remoteMethodInvocationInfo.getEnrichedReturnValue().put(MESSAGE_RESULT, remoteMethodInvocationInfo.getReturnValue());
		
		// get info from header
		List<String> resourceNodeIds = remoteMethodInvocationInfo.getResourceNodeIds();
				
		if (resourceNodeIds != null) {
			// only request updates if the client is subscribed to some resource
			long timestampOfLastRequest = remoteMethodInvocationInfo.getTimestampOfLastRequest();
			long timestamp = new Date().getTime();
			remoteMethodInvocationInfo.getEnrichedReturnValue().put(LAST_UPDATE_TIMESTAMP, timestamp);
			
			Map<String, List<Update>> resourceNodeIdToUpdates = new HashMap<String, List<Update>>();
			for (String resourceNodeId : resourceNodeIds) {
				List<Update> updates = CorePlugin.getInstance().getResourceService()
						.getUpdates(resourceNodeId, timestampOfLastRequest, timestamp);
				resourceNodeIdToUpdates.put(resourceNodeId, updates);
			}
			if (resourceNodeIdToUpdates.size() > 0) {
				remoteMethodInvocationInfo.getEnrichedReturnValue().put(UPDATES, resourceNodeIdToUpdates);
			}
		}
			
		remoteMethodInvocationInfo.setReturnValue(remoteMethodInvocationInfo.getEnrichedReturnValue());
	}
	
}
