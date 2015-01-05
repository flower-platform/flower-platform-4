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
package org.flowerplatform.js_client.java;

import java.net.URI;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.flowerplatform.js_client.java.provider.JsClientJavaObjectMapperProvider;
import org.flowerplatform.js_client.java.provider.JsClientJavaRequestFilter;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.BundleContext;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * @author Cristina Constantinescu
 */
public class JsClientJavaPlugin extends AbstractFlowerJavaPlugin {

	private static JsClientJavaPlugin instance;
	
	private Scriptable nodeRegistryManager;
	
	private WebTarget client;
		
	private Long lastUpdateTimestampOfServer = (long) -1;
	
	public WebTarget getClient() {		
		return client;
	}

	// TODO CC: temp URI (this must be provided by freeplane java client implementation
	private static URI getBaseURI() {
	    return UriBuilder.fromUri("http://localhost:8080/org.flowerplatform.host.web_app/").build();
	}
	
	public static JsClientJavaPlugin getInstance() {
		return instance;
	}
			
	public Scriptable getNodeRegistryManager() {
		return nodeRegistryManager;
	}

	public void setNodeRegistryManager(Scriptable nodeRegistryManager) {
		this.nodeRegistryManager = nodeRegistryManager;
	}
	
	public Long getLastUpdateTimestampOfServer() {
		return lastUpdateTimestampOfServer;
	}

	public void setLastUpdateTimestampOfServer(Long lastUpdateTimestampOfServer) {
		this.lastUpdateTimestampOfServer = lastUpdateTimestampOfServer;
	}

	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		instance = this;	
		
		if (client == null) {
			client = ClientBuilder.newClient()
					.register(JacksonFeature.class)			
					.register(JacksonJaxbJsonProvider.class)
					.register(JsClientJavaObjectMapperProvider.class)
					.register(JsClientJavaRequestFilter.class)
					.target(getBaseURI());						
		}
	}
	
	/**
	 *@author see class
	 **/
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		instance = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// not used
	}		
		
}
