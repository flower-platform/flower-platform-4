package org.flowerplatform.host.servletbridge.registry;

import org.eclipse.equinox.http.servlet.HttpServiceServlet;
import org.eclipse.equinox.servletbridge.BridgeServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Cristian Spiescu
 */
public class Activator implements BundleActivator {

	private HttpServiceServlet httpServiceServlet;
	
	public void start(BundleContext bundleContext) throws Exception {
		httpServiceServlet = new HttpServiceServlet();
		BridgeServlet.registerServletDelegate(httpServiceServlet);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		BridgeServlet.unregisterServletDelegate(httpServiceServlet);
		httpServiceServlet = null;
	}

}
