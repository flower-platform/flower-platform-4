package org.flowerplatform.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoked by the remote method invocation backend (e.g. Flex/BlazeDS, Rest/JSON), when a call from the client arrives.
 * 
 * @author Sebastian Solomon
 * @author Cristina Constantinescu
 */
public class RemoteMethodInvocationListener {

	private final static Logger logger = LoggerFactory.getLogger(RemoteMethodInvocationListener.class);

	public static final String LAST_UPDATE_TIMESTAMP = "timestampOfLastUpdate";
	public static final String ROOT_NODE_IDS = "rootNodeIds";
	
	public static final String MESSAGE_RESULT = "messageResult";
	public static final String UPDATES = "updates";
	
	public void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		remoteMethodInvocationInfo.setStartTimestamp(new Date().getTime());
	}

	/**
	 * Changes {@link RemoteMethodInvocationInfo#getReturnValue()} to a map containing:
	 * <ul>
	 * 	<li> {@link #MESSAGE_RESULT} -> message invocation result
	 *  <li> {@link #UPDATES} -> map from fullRootNodeId to a list of recent updates = all updates registered after {@link #LAST_UPDATE_TIMESTAMP}
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
		
		// get info from header
		long timestampOfLastRequest = ((Number) remoteMethodInvocationInfo.getHeaders().get(LAST_UPDATE_TIMESTAMP)).longValue();
		@SuppressWarnings("unchecked")
		List<String> rootNodeIds = (List<String>) remoteMethodInvocationInfo.getHeaders().get(ROOT_NODE_IDS);
		
		// prepare result
		Map<String, Object> returnValue = new HashMap<String, Object>();
		returnValue.put(MESSAGE_RESULT, remoteMethodInvocationInfo.getReturnValue());
		returnValue.put(LAST_UPDATE_TIMESTAMP, new Date().getTime());
		
		Map<String, List<Update>> rootNodeIdToUpdates = new HashMap<String, List<Update>>();
		for (String rootNodeId : rootNodeIds) {
			Node rootNode = new Node(rootNodeId);
			List<Update> updates = CorePlugin.getInstance().getUpdateService().getUpdates(rootNode, timestampOfLastRequest);
			rootNodeIdToUpdates.put(rootNode.getFullNodeId(), updates);
		}
		if (rootNodeIdToUpdates.size() > 0) {
			returnValue.put(UPDATES, rootNodeIdToUpdates);
		}
		
		remoteMethodInvocationInfo.setReturnValue(returnValue);	
	}

}
