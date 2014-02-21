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
	public static final String FULL_ROOT_NODE_ID = "fullRootNodeId";
	
	public static final String MESSAGE_RESULT = "messageResult";
	public static final String UPDATES = "updates";
	
	public void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		remoteMethodInvocationInfo.setStartTimestamp(new Date().getTime());
	}

	/**
	 * Changes {@link RemoteMethodInvocationInfo#getReturnValue()} to a map containing:
	 * <ul>
	 * 	<li> {@link #MESSAGE_RESULT} -> message invocation result
	 *  <li> {@link #UPDATES} -> list of recently updates = all updates registered after {@link #LAST_UPDATE_TIMESTAMP}
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
		Map<String, Object> returnValue = new HashMap<String, Object>();
		returnValue.put(MESSAGE_RESULT, remoteMethodInvocationInfo.getReturnValue());
				
		Object timestampOfLastRequest = remoteMethodInvocationInfo.getHeaders().get(LAST_UPDATE_TIMESTAMP);
		Object fullRootNodeId = remoteMethodInvocationInfo.getHeaders().get(FULL_ROOT_NODE_ID);
		
		if (fullRootNodeId != null) { // client requested the latest updates
			List<Update> updates = CorePlugin.getInstance().getUpdateService().getUpdates(
							new Node((String) fullRootNodeId), 
							// this instance verification is only temporary (correct timestamp will be seen as Double)
							// TODO CC: remove verification when properly timestamp
							((timestampOfLastRequest instanceof Double) ? ((Double) timestampOfLastRequest).longValue() : ((Integer) timestampOfLastRequest).longValue()));
			if (updates != null) {
				returnValue.put(UPDATES, updates);	
			}
		}
		
		remoteMethodInvocationInfo.setReturnValue(returnValue);	
	}

}
