package org.flowerplatform.js_client.server;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationInfo;
import org.flowerplatform.core.RemoteMethodInvocationListener;

/**
 * Notifies the registered {@link RemoteMethodInvocationListener} before and 
 * after a service method is invoked.
 * 
 * @author Mariana Gheorghe
 */
public class RemoteMethodInvocationFilter implements ContainerRequestFilter, ContainerResponseFilter {
	
	private ThreadLocal<RemoteMethodInvocationInfo> infoThreadLocal = new ThreadLocal<RemoteMethodInvocationInfo>();
	
	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		// create RemoteMethodInvocationInfo
		UriInfo uriInfo = context.getUriInfo();
		RemoteMethodInvocationInfo remoteMethodInvocationInfo =	new RemoteMethodInvocationInfo();
		remoteMethodInvocationInfo.setServiceId(uriInfo.getPathSegments().get(0).getPath());
		remoteMethodInvocationInfo.setMethodName(uriInfo.getPathSegments().get(1).getPath());
		remoteMethodInvocationInfo.setHeaders(context.getHeaders());
		infoThreadLocal.set(remoteMethodInvocationInfo);
		
		// pre-invoke
		CorePlugin.getInstance().getRemoteMethodInvocationListener().preInvoke(remoteMethodInvocationInfo);
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		try {
			// get RemoteMethodInvocationInfo
			RemoteMethodInvocationInfo remoteMethodInvocationInfo = infoThreadLocal.get();
			if (remoteMethodInvocationInfo == null) {
				return;
			}
			
			// get original response and post-invoke
			Object originalResponse = responseContext.getEntity();
			remoteMethodInvocationInfo.setReturnValue(originalResponse);
			CorePlugin.getInstance().getRemoteMethodInvocationListener().postInvoke(remoteMethodInvocationInfo);
			responseContext.setEntity(remoteMethodInvocationInfo.getReturnValue());
		} finally {
			RemoteMethodInvocationFilter.this.infoThreadLocal.remove();
		}
	}
	
}
