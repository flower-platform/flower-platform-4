package org.flowerplatform.host.servletbridge.registry;

import javax.servlet.ServletContext;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.http.servlet.HttpServiceServlet;
import org.eclipse.equinox.servletbridge.BridgeServlet;
import org.flowerplatform.util.RunnableWithParam;
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
		
		IConfigurationElement[] configurationElements = 
				Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.host.servletbridge.registry.listeners");
		for (IConfigurationElement configurationElement : configurationElements) {
			@SuppressWarnings("unchecked")
			RunnableWithParam<Void, ServletContext> runnable = (RunnableWithParam<Void, ServletContext>) configurationElement.createExecutableExtension("runnable");
			runnable.run(httpServiceServlet.getServletContext());
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		BridgeServlet.unregisterServletDelegate(httpServiceServlet);
		httpServiceServlet = null;
	}
}
