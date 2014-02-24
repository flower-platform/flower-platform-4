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
