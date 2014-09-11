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
package org.flowerplatform.flex_client.server.blazeds;

import java.lang.reflect.Proxy;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Delegates all operations to {@link #delegate}. Except {@link #getInitParameter(String)} and {@link #getServletContext()}.
 * 
 * @see FlowerMessageBrokerServlet
 * @see #getInitParameter(String)
 * 
 * @author Cristian Spiescu
 */
public class ServletConfigWrapper implements ServletConfig {

	protected ServletConfig delegate;
	
	/**
	 *@author see class
	 **/
	public ServletConfigWrapper(ServletConfig delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public String getInitParameter(String arg0) {
		if ("services.configuration.manager".equals(arg0)) {
			return FlowerFlexConfigurationManager.class.getCanonicalName();
		}
		return delegate.getInitParameter(arg0);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return delegate.getInitParameterNames();
	}

	/**
	 * Wraps a proxy around the servlet context.
	 * 
	 * @see ServletContextAdaptorInvocationHandler
	 * 
	 * @author Mariana Gheorghe
	 */
	@Override
	public ServletContext getServletContext() {
		Class clazz = getClass();
		ClassLoader classLoader = clazz.getClassLoader();
		Class[] interfaces = new Class[] {ServletContext.class};
		return (ServletContext) Proxy.newProxyInstance(classLoader, interfaces, 
				new ServletContextAdaptorInvocationHandler(delegate.getServletContext()));
	}
	
	@Override
	public String getServletName() {
		return delegate.getServletName();
	}

}