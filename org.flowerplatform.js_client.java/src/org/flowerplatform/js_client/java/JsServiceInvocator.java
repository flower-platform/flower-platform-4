package org.flowerplatform.js_client.java;

import java.net.URI;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.flowerplatform.core.node.remote.SubscriptionInfo;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsServiceInvocator {
	
	private static URI getBaseURI() {
	    return UriBuilder.fromUri("http://localhost:8080/org.flowerplatform.host.web_app/").build();
	}
	
	public void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback, Function faultCallback) {
		String[] tokens = serviceIdAndMethodName.split("\\.");
					
		WebTarget target = ClientBuilder.newClient().target(getBaseURI());
		
		target = target.path("/ws-dispatcher").path(tokens[0]).path(tokens[1]);
				
		Response response = target.request().post(Entity.entity(parameters[0], MediaType.TEXT_PLAIN));
		
		JsonNode node = response.readEntity(JsonNode.class);
		Object result = node.fields().next().getValue();
		
		if (response.getStatus() != 200) {
			if (faultCallback != null) {
				Context cx = Context.enter();				
				Scriptable scope = cx.initStandardObjects();				
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							
				try {
					faultCallback.call(cx, scope, scope, new Object[] {mapper.readValue(result.toString(), SubscriptionInfo.class)});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			}
		}
		if (resultCallback != null) {	
			Context cx = Context.enter();
			
			Scriptable scope = cx.initStandardObjects();
			
			 ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						
			try {
				resultCallback.call(cx, scope, scope, new Object[] {mapper.readValue(result.toString(), SubscriptionInfo.class)});
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}

	}
	
}
