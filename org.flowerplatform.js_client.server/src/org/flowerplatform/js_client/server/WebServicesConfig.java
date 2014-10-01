package org.flowerplatform.js_client.server;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.js_client.server.jackson.ExceptionMapper;
import org.flowerplatform.js_client.server.remote.JavaClientMethodInvocationService;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Exposes registered services as REST API. Passed as an initialization
 * parameter for the {@link ServletContainer} registered via extension point.
 * 
 * @author Mariana Gheorghe
 */
public class WebServicesConfig extends ResourceConfig {

	public WebServicesConfig() throws ClassNotFoundException {	
		for (Object service : CorePlugin.getInstance().getServiceRegistry().getMap().values()) {
			register(service);
		}
		register(new JavaClientMethodInvocationService());
		
		register(new RemoteMethodInvocationFilter());
		register(new RemoteMethodInvocationWriterInterceptor());
		
		register(JacksonFeature.class);
		register(JsClientServerPlugin.getInstance().getObjectMapperProvider());
		register(ExceptionMapper.class);
		
		register(LoggingFilter.class);
	}
	
}
