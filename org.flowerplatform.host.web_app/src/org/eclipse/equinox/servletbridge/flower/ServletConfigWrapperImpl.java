package org.eclipse.equinox.servletbridge.flower;

import javax.servlet.ServletConfig;

/**
 * @author Cristian Spiescu
 */
public class ServletConfigWrapperImpl implements ServletConfigWrapper {

	protected ServletConfig servletConfig;
	
	public ServletConfigWrapperImpl(ServletConfig servletConfig) {
		super();
		this.servletConfig = servletConfig;
	}

	@Override
	public ServletContextWrapper getServletContext() {
		return new ServletContextWrapperImpl(servletConfig.getServletContext());
	}

	@Override
	public String getInitParameter(String name) {
		return servletConfig.getInitParameter(name);
	}

}
