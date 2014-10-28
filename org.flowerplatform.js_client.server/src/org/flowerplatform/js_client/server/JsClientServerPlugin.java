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

import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Cristina Constantinescu
 */
public class JsClientServerPlugin extends AbstractFlowerJavaPlugin {

	private static JsClientServerPlugin instance;
	
	private ObjectMapperProvider objectMapperProvider = new ObjectMapperProvider();
	
	public static JsClientServerPlugin getInstance() {
		return instance;
	}
			
	public ObjectMapperProvider getObjectMapperProvider() {
		return objectMapperProvider;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapperProvider.getContext(null);
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		instance = this;			
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
