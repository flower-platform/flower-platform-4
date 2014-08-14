/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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

		Object originalReturnValue = super.invoke(message);
		
		remoteMethodInvocationInfo.setReturnValue(originalReturnValue);
		CorePlugin.getInstance().getRemoteMethodInvocationListener().postInvoke(remoteMethodInvocationInfo);
		
		return remoteMethodInvocationInfo.getReturnValue();
	}
	
}