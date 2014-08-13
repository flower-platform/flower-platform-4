/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.host.servletbridge.registry;

import javax.servlet.ServletContext;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.http.servlet.HttpServiceServlet;
import org.eclipse.equinox.servletbridge.BridgeServlet;
import org.flowerplatform.util.RunnableWithParam;
import org.flowerplatform.util.servlet.ServletUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class Activator implements BundleActivator {
		
	private HttpServiceServlet httpServiceServlet;
	
	public void start(BundleContext bundleContext) throws Exception {
		httpServiceServlet = new HttpServiceServlet();
		BridgeServlet.registerServletDelegate(httpServiceServlet);
		
		/*
		 * Needed to store the {@link InstanceManager} instance from {@link #httpServiceServlet} context.
		 * 
		 * <p>
		 * Used in {@link CustomJSPServlet} to add this instance as attribute in its context.
		 * Otherwise it will crash when redirecting the "main.jsp" file from client to {@link CustomJSPFactory}.
		 * 
		 * <p>
		 * The context class used by {@link JspServlet} is a <code>ServletContextAdaptor</code> and when 
		 * calling <code>ServletContextAdaptor#getAttribute(name)</code> it looks for it in its <code>httpContext</code>,
		 * not <code>servletContext</code> where this {@link InstanceManager} is set. <br>
		 * So we need to add it there.
		 * 
		 * @see CustomJSPFactory
		 * @see CustomJSPServlet
		 */
		String instanceManagerClass = "org.apache.tomcat.InstanceManager";
		if (Class.forName(instanceManagerClass) == null) {
			throw new RuntimeException("The application is not running in a Tomcat servlet container! "
					+ "Please contact the Flower team if you need support for other servlet containers.");
		}
		ServletUtils.addServletContextAdditionalAttributes(instanceManagerClass, httpServiceServlet.getServletContext().getAttribute(instanceManagerClass));
		
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.host.servletbridge.registry.listeners");
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