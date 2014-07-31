package org.flowerplatform.js_client.java;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.flowerplatform.core.node.remote.SubscriptionInfo;
import org.flowerplatform.util.RunnableWithParam;

public class JsServiceInvocator {
	
	private static URI getBaseURI() {
	    return UriBuilder.fromUri("http://localhost:8080/org.flowerplatform.host.web_app/").build();
	}
	
	public void invoke(String serviceIdAndMethodName, Object[] parameters, final RunnableWithParam<Object, Object> resultCallback, final RunnableWithParam<Object, Object> faultCallback) {
		String[] tokens = serviceIdAndMethodName.split("\\.");
				 		
		WebTarget target = ClientBuilder.newClient().target(getBaseURI());
		
		target = target.path("/servlet/rest").path(tokens[0]).path(tokens[1]);
		
		Entity<Object> entity = Entity.json(parameters[0]);
		
		resultCallback.run(target.request().post(entity, SubscriptionInfo.class));
//		async().get(new InvocationCallback<Object>() {
//			@Override
//			public void completed(Object response) {
//				resultCallback.run(response);
//			}
//			
//			@Override
//			public void failed(Throwable throwable) {
//				faultCallback.run(throwable);
//			}
//		});
	}
	
}
