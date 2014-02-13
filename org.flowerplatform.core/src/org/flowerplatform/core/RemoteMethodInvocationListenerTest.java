package org.flowerplatform.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteMethodInvocationListenerTest implements	IRemoteMethodInvocationListener {
	private final static Logger logger = LoggerFactory
			.getLogger(RemoteMethodInvocationListenerTest.class);

	@Override
	public void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		logger.debug("preInvoke {}.{}", new Object[] { 
					remoteMethodInvocationInfo.getServiceId(),
					remoteMethodInvocationInfo.getMethodName() });
	}

	@Override
	public void postInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		logger.debug("postInvoke {}.{}", new Object[] { 
				remoteMethodInvocationInfo.getServiceId(),
				remoteMethodInvocationInfo.getMethodName() });
	}

}
