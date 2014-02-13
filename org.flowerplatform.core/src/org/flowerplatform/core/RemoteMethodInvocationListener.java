package org.flowerplatform.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.core.node.update.remote.UpdateService;

/**
 * @author Cristina Constantinescu
 */
public class RemoteMethodInvocationListener implements IRemoteMethodInvocationListener {

	@Override
	public void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
	}

	@Override
	public void postInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		// compute current returnValue with list of updates performed in method's body
		Map<String, Object> returnValue = new HashMap<String, Object>();
		returnValue.put("messageInvocationResult", remoteMethodInvocationInfo.getReturnValue());
		returnValue.put("updates", UpdateService.getCurrentMethodInvocationUpdates().get());
				
		remoteMethodInvocationInfo.setReturnValue(returnValue);
		
		// reset thread local data
		UpdateService.getCurrentMethodInvocationUpdates().set(null);
	}

}
