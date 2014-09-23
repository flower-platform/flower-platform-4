package org.flowerplatform.js_client.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.js_client.server.oauth.client.OAuth2ProviderService;
import org.flowerplatform.js_client.server.oauth.server.OAuth2Service;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Mariana Gheorghe
 */
public class JsClientServerPlugin extends AbstractFlowerJavaPlugin {

	private ObjectMapperProvider objectMapperProvider = new ObjectMapperProvider();

	protected static JsClientServerPlugin INSTANCE;
	
	public ObjectMapperProvider getObjectMapperProvider() {
		return objectMapperProvider;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapperProvider.getContext(null);
	}
	
	public static JsClientServerPlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		// client
		CorePlugin.getInstance().getServiceRegistry().registerService("oauthProviderService", new OAuth2ProviderService());
		
		// server
		CorePlugin.getInstance().getServiceRegistry().registerService("oauthService", new OAuth2Service());
	}

	public void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, boolean httpOnly) {
		String contextPath = request.getContextPath();
		boolean secure = request.isSecure();
		String setCookie = String.format("%s=%s; Path=%s/" + (httpOnly ? "; HttpOnly" : "") + (secure ? "; Secure" : ""), name, value, contextPath);
		response.addHeader("Set-Cookie", setCookie);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}
