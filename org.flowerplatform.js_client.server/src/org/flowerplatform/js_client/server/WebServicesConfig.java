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

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.js_client.server.jackson.ExceptionMapper;
import org.flowerplatform.js_client.server.remote.JavaClientMethodInvocationService;
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
