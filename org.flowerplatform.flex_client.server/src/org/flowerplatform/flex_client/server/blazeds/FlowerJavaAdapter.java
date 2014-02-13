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
 */
public class FlowerJavaAdapter extends JavaAdapter {

	public Object invoke(Message message) {
		RemotingMessage remoteMessage = (RemotingMessage) message;
		RemoteMethodInvocationInfo remoteMethodInvocationInfo = new RemoteMethodInvocationInfo();
		remoteMethodInvocationInfo.setServiceId(remoteMessage.getDestination());
		remoteMethodInvocationInfo.setMethodName(remoteMessage.getOperation());
		remoteMethodInvocationInfo.setParameters(remoteMessage.getParameters());
		
		CorePlugin.getInstance().getRemoteMethodInvocationListener().preInvoke(remoteMethodInvocationInfo);
		
		Object originalReturnValue = super.invoke(message);
		
		remoteMethodInvocationInfo.setReturnValue(originalReturnValue);
		CorePlugin.getInstance().getRemoteMethodInvocationListener().postInvoke(remoteMethodInvocationInfo);
		
		return remoteMethodInvocationInfo.getReturnValue();
	}
	
}
