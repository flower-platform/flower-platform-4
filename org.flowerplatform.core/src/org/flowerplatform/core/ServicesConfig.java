package org.flowerplatform.core;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Exposes registered services as REST API. Passed as an initialization
 * parameter for the {@link ServletContainer} registered via extension point.
 * 
 * @author Mariana Gheorghe
 */
public class ServicesConfig extends ResourceConfig {

	public ServicesConfig() throws ClassNotFoundException {
		for (Object service : CorePlugin.getInstance().getServiceRegistry().getMap().values()) {
			register(service);
		}
				
		register(JacksonFeature.class);		
		register(new ObjectMapperProvider());
		
		register(LoggingFilter.class);
	}
	
}
