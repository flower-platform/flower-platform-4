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
