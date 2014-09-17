package org.flowerplatform.js_client.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class JsClientServerPlugin extends AbstractFlowerJavaPlugin {

	protected static JsClientServerPlugin INSTANCE;
		
	public static JsClientServerPlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
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
