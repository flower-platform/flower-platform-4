package org.flowerplatform.js_client.java;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class JsServiceInvocator {
	
	private static final String METHOD_INVOCATION_SERVICE = "/ws-dispatcher/javaClientMethodInvocationService";
	private static final String SERVICE_DOT_METHOD_NAME = "serviceDotMethodName";
	private static final String PARAMETERS = "parameters";

	private WebTarget client;
		
	public WebTarget getClient() {
		if (client == null) {
			client = ClientBuilder.newClient()
					.register(JacksonFeature.class)			
					.register(JacksonJaxbJsonProvider.class)
					.register(ClientObjectMapperProvider.class)
					.target(getBaseURI());			
			client = client.path(METHOD_INVOCATION_SERVICE);			
		}
		return client;
	}

	// TODO CC: temp URI
	private static URI getBaseURI() {
	    return UriBuilder.fromUri("http://localhost:8080/org.flowerplatform.host.web_app/").build();
	}
	
	public void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback, Function faultCallback) {		
		Map<String, Object> requestParams = new HashMap<>();	
		requestParams.put(SERVICE_DOT_METHOD_NAME, serviceIdAndMethodName);
		requestParams.put(PARAMETERS, parameters);
		Response response = getClient().request().post(Entity.entity(requestParams, MediaType.APPLICATION_JSON_TYPE));
				
		if (response.getStatus() != 200) {
			if (faultCallback != null) {
				Context cx = Context.enter();				
				Scriptable scope = cx.initStandardObjects();						
				try {
					faultCallback.call(cx, scope, scope, new Object[] {new Fault(response.getStatusInfo())});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			}
		} else if (resultCallback != null) {	
			HashMap<?, ?> node = response.readEntity(HashMap.class);
			Object result = node.get("messageResult");
			
			Context cx = Context.enter();			
			Scriptable scope = cx.initStandardObjects();		
			try {
				resultCallback.call(cx, scope, scope, new Object[] {result});
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
