package org.flowerplatform.flex_client.server.blazeds;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationInfo;

import flex.messaging.messages.Message;
import flex.messaging.messages.RemotingMessage;
import flex.messaging.services.remoting.adapters.JavaAdapter;

/**
 * Gives control before and after the client invokes a service method.
 * 
 * @author Sebastian Solomon
 * @author Cristina Constantinescu
 * @author Claudiu Matei
 */
public class FlowerJavaAdapter extends JavaAdapter {

	public Object invoke(Message message) {
		RemotingMessage remoteMessage = (RemotingMessage) message;
		RemoteMethodInvocationInfo remoteMethodInvocationInfo = new RemoteMethodInvocationInfo();
		remoteMethodInvocationInfo.setServiceId(remoteMessage.getDestination());
		remoteMethodInvocationInfo.setMethodName(remoteMessage.getOperation());
		remoteMethodInvocationInfo.setParameters(remoteMessage.getParameters());
		remoteMethodInvocationInfo.setHeaders(remoteMessage.getHeaders());
		
		CorePlugin.getInstance().getRemoteMethodInvocationListener().preInvoke(remoteMethodInvocationInfo);

		try {
			Object originalReturnValue = super.invoke(message);
			remoteMethodInvocationInfo.setReturnValue(originalReturnValue);
		} finally {
			CorePlugin.getInstance().getRemoteMethodInvocationListener().postInvoke(remoteMethodInvocationInfo);
		}
		
		return remoteMethodInvocationInfo.getReturnValue();
	}
	
}
