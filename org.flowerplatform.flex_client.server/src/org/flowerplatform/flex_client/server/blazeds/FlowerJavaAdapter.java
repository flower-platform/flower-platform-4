package org.flowerplatform.flex_client.server.blazeds;

import java.util.Date;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.messages.Message;
import flex.messaging.messages.RemotingMessage;
import flex.messaging.services.remoting.adapters.JavaAdapter;

/**
 * log method call execution time and gives control before and after the client
 * invokes a service method
 * 
 * @author Sebastian Solomon
 */
public class FlowerJavaAdapter extends JavaAdapter {

	private final static Logger logger = LoggerFactory
			.getLogger(FlowerJavaAdapter.class);

	public Object invoke(Message message) {
		long startTime= new Date().getTime();
		RemotingMessage remoteMessage = (RemotingMessage) message;
		RemoteMethodInvocationInfo remoteMethodInvocationInfo = new RemoteMethodInvocationInfo(
				message.getDestination(),
				remoteMessage.getOperation(),
				remoteMessage.getParameters(),
				remoteMessage.getTimestamp());
		
		CorePlugin.getInstance().getRemoteMethodInvocationListener().preInvoke(remoteMethodInvocationInfo);
		
		Object object = super.invoke(message);
		
		remoteMethodInvocationInfo.setReturnValue(object);
		
		if (logger.isDebugEnabled()) {
			long endTime, difference;
			
			endTime = new Date().getTime();
			difference = endTime - startTime;

			String destination = message.getDestination();
			String operation = "";
			if (message instanceof RemotingMessage) {
				operation = ((RemotingMessage) message).getOperation();
			}
			logger.debug("[{}ms] {}.{}() invoked", new Object[] { difference,
					destination, operation });
		}
		
		CorePlugin.getInstance().getRemoteMethodInvocationListener().postInvoke(remoteMethodInvocationInfo);
		
		return object;
	}
	
}
