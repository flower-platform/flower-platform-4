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
package org.flowerplatform.js_client.java.provider;

import static org.flowerplatform.js_client.java.JsClientJavaConstants.METHOD_INVOCATION_SERVICE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.apache.commons.lang3.StringUtils;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.js_client.java.JsClientJavaConstants;
import org.flowerplatform.js_client.java.JsClientJavaPlugin;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

/**
 * Adds additional headers to all client requests like list of available resourceSets, resourceUris.
 * @author Cristina Constantinescu
 */
public class JsClientJavaRequestFilter implements ClientRequestFilter {

	private String prepareHeaderValueForResources(NativeArray resources) {		
		List<String> list = new ArrayList<>();
		
		for (int i = 0; i < resources.getLength(); ++i) {
			list.add((String) resources.get(i, null));			
	    }
		if (list.size() > 0) {
			Collections.sort(list);
			return StringUtils.join(list, METHOD_INVOCATION_SERVICE);
		}
		return null;
	}
		
	@Override
	public void filter(ClientRequestContext ctx) throws IOException {
		Scriptable nodeRegistryManager = JsClientJavaPlugin.getInstance().getNodeRegistryManager();
		// add resourceSets in header
		NativeArray resourceSets = (NativeArray) JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceSets");		
		String value = prepareHeaderValueForResources(resourceSets);
		if (value != null) {
			ctx.getHeaders().add(CoreConstants.RESOURCE_SETS, value);
			
			// add resourceUris in header
			NativeArray resourceUris = (NativeArray) JsClientJavaUtils.invokeJsFunction(nodeRegistryManager, "getResourceUris");	
			ctx.getHeaders().add(CoreConstants.RESOURCE_URIS, prepareHeaderValueForResources(resourceUris));
		}		
		// add last update timestamp in header
		ctx.getHeaders().add(CoreConstants.LAST_UPDATE_TIMESTAMP, JsClientJavaPlugin.getInstance().getLastUpdateTimestampOfServer());		
	}

}
