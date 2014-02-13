package org.flowerplatform.core;

/**
 * @author Sebastian Solomon
 */
public interface IRemoteMethodInvocationListener {

	void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo);

	void postInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo);
}
