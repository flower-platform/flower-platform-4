/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.js_client.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationInfo;
import org.flowerplatform.core.RemoteMethodInvocationListener;

import com.fasterxml.jackson.databind.type.CollectionType;

/**
 * Notifies the registered {@link RemoteMethodInvocationListener} before and 
 * after a service method is invoked.
 * 
 * <p>
 * Equivalent of the Blaze DS server class: <code>FlowerJavaAdapter</code>.
 * 
 * @author Mariana Gheorghe
 */
public class RemoteMethodInvocationFilter implements ContainerRequestFilter, ContainerResponseFilter {
	
	private static final String RESOURCES_SEPARATOR = ";";
	
	private ThreadLocal<RemoteMethodInvocationInfo> infoThreadLocal = new ThreadLocal<RemoteMethodInvocationInfo>();
	
	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		// create RemoteMethodInvocationInfo
		UriInfo uriInfo = context.getUriInfo();
		RemoteMethodInvocationInfo remoteMethodInvocationInfo =	new RemoteMethodInvocationInfo();
		remoteMethodInvocationInfo.setServiceMethodOrUrl(context.getMethod() + " " + uriInfo.getPath());
			
		Map<String, Object> headers = new HashMap<String, Object>();
		List<String> header;
		
		header = context.getHeaders().get(CoreConstants.LAST_UPDATE_TIMESTAMP);
		if (header != null) {
			headers.put(CoreConstants.LAST_UPDATE_TIMESTAMP, Long.parseLong(header.get(0)));
		}
		
		header = context.getHeaders().get(CoreConstants.RESOURCE_SETS);
		if (header != null) {
			headers.put(CoreConstants.RESOURCE_SETS, Arrays.asList(StringUtils.split(header.get(0), RESOURCES_SEPARATOR)));
		}
		
		header = context.getHeaders().get(CoreConstants.RESOURCE_URIS);
		if (header != null) {
			headers.put(CoreConstants.RESOURCE_URIS, Arrays.asList(StringUtils.split(header.get(0), RESOURCES_SEPARATOR)));
		}
		
		remoteMethodInvocationInfo.setHeaders(headers);
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
