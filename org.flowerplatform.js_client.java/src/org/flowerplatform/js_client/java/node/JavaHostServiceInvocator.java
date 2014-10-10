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
package org.flowerplatform.js_client.java.node;

import static org.flowerplatform.js_client.java.JsClientJavaConstants.METHOD_INVOCATION_SERVICE;
import static org.flowerplatform.js_client.java.JsClientJavaConstants.PARAMETERS;
import static org.flowerplatform.js_client.java.JsClientJavaConstants.SERVICE_DOT_METHOD_NAME;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.js_client.java.JsClientJavaPlugin;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * @author Cristina Constantinescu
 */
public class JavaHostServiceInvocator extends AbstractServiceInvocator {
	
	public void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback, Function faultCallback) {			
		Builder request = JsClientJavaPlugin.getInstance().getClient().path(METHOD_INVOCATION_SERVICE).request();
		
		String accessToken = JsClientJavaPlugin.getInstance().getAccessToken();
		if (accessToken != null) {
			// authenticated request
			request.header("Authorization", "Bearer " + accessToken);
		}
		
		// create the map sent as parameter
		Map<String, Object> requestParams = new HashMap<>();	
		requestParams.put(SERVICE_DOT_METHOD_NAME, serviceIdAndMethodName);
		requestParams.put(PARAMETERS, parameters);
	
		// send request
		Response response = request.post(Entity.entity(requestParams, MediaType.APPLICATION_JSON_TYPE));
		
		HashMap<?, ?> entity = response.readEntity(HashMap.class);
		Object result = entity == null ? null : entity.get(CoreConstants.MESSAGE_RESULT);
		
		if (response.getStatus() >= Status.INTERNAL_SERVER_ERROR.getStatusCode()) { 
			// server error (code >= 500)
			if (faultCallback != null) {
				Context cx = Context.enter();				
				Scriptable scope = cx.initStandardObjects();						
				faultCallback.call(cx, scope, scope, new Object[] {new JavaHostException((String) result, response.getStatusInfo())});				
			} else {
				throw new RuntimeException((String) result);
			}
		} else if (resultCallback != null) {
			Long lastUpdateTimestampOfServer = (Long) entity.get(CoreConstants.LAST_UPDATE_TIMESTAMP);
			if (lastUpdateTimestampOfServer != null) {
				JsClientJavaPlugin.getInstance().setLastUpdateTimestampOfServer(lastUpdateTimestampOfServer);
			}
			Context cx = Context.enter();			
			Scriptable scope = cx.initStandardObjects();		
			resultCallback.call(cx, scope, scope, new Object[] {result});			
		}
	}
	
}
