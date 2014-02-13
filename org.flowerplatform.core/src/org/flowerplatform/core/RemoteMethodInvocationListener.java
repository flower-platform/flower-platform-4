package org.flowerplatform.core;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoked by the remote method invocation backend (e.g. Flex/BlazeDS, Rest/JSON), when a call from the client arrives.
 * 
 * @author Sebastian Solomon
 */
public class RemoteMethodInvocationListener {

	private final static Logger logger = LoggerFactory
			.getLogger(RemoteMethodInvocationListener.class);

	public void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		remoteMethodInvocationInfo.setStartTimestamp(new Date().getTime());
	}

	public void postInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		if (logger.isDebugEnabled()) {
			long endTime = new Date().getTime();
			long difference = endTime - remoteMethodInvocationInfo.getStartTimestamp();
			String serviceId = remoteMethodInvocationInfo.getServiceId();
			String methodName = remoteMethodInvocationInfo.getMethodName();
			logger.debug("[{}ms] {}.{}() invoked", new Object[] { difference,
					serviceId, methodName });
		}
	}

}
