package org.eclipse.equinox.servletbridge.flower;

/**
 * @see FlowerFrameworkLauncher
 * @author Cristian Spiescu
 */
public interface ServletConfigWrapper {
	ServletContextWrapper getServletContext();
	String 	getInitParameter(String name);
}
