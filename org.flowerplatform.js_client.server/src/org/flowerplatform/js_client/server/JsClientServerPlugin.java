package org.flowerplatform.js_client.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.OAuthRequestBuilder;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.OAuthResponse.OAuthResponseBuilder;
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

	protected static JsClientServerPlugin instance;
	
	public ObjectMapperProvider getObjectMapperProvider() {
		return objectMapperProvider;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapperProvider.getContext(null);
	}
	
	public static JsClientServerPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
		
		// client
		CorePlugin.getInstance().getServiceRegistry().registerService("oauthProviderService", new OAuth2ProviderService());
		
		// server
		CorePlugin.getInstance().getServiceRegistry().registerService("oauthService", new OAuth2Service());
	}

	/**
	 * Add a Set-Cookie header to the response.
	 */
	public void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, boolean httpOnly) {
		String contextPath = request.getContextPath();
		boolean secure = request.isSecure();
		String setCookie = String.format("%s=%s; Path=%s/" + (httpOnly ? "; HttpOnly" : "") + (secure ? "; Secure" : ""), name, value, contextPath);
		response.addHeader("Set-Cookie", setCookie);
	}
	
	/**
	 * Build a JSON representation of the body of the builder.
	 * Utility method to avoid try/catch blocks for {@link OAuthSystemException}s.
	 */
	public OAuthResponse buildJSONMessage(OAuthResponseBuilder builder) {
		try {
			return builder.buildJSONMessage();
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Build a query string representation of the body of the builder.
	 * Utility method to avoid try/catch blocks for {@link OAuthSystemException}s.
	 */
	public OAuthClientRequest buildQueryMessage(OAuthRequestBuilder builder) {
		try {
			return builder.buildQueryMessage();
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Write out the {@link OAuthResponse}.
	 */
	public void writeHttpResponse(HttpServletResponse resp, OAuthResponse oauthResponse) throws IOException {
		resp.setStatus(oauthResponse.getResponseStatus());
		PrintWriter pw = resp.getWriter();
		pw.print(oauthResponse.getBody());
		pw.flush();
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}
