package org.flowerplatform.flex_client.server.blazeds;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

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

	@Override
	public ServletContext getServletContext() {
		return delegate.getServletContext();
	}

	@Override
	public String getServletName() {
		return delegate.getServletName();
	}

}
