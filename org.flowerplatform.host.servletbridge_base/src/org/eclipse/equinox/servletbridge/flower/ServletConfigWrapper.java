package org.eclipse.equinox.servletbridge.flower;

public interface ServletConfigWrapper {
	ServletContextWrapper getServletContext();
	String 	getInitParameter(String name);
}
