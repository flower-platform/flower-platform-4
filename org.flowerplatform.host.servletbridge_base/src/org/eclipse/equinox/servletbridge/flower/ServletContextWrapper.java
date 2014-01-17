package org.eclipse.equinox.servletbridge.flower;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public interface ServletContextWrapper {
	String getTempDir();
	
	URL getResource(java.lang.String path) throws MalformedURLException;
	void log(String msg);
	void log(String msg, Throwable throwable);
	Object getAttribute(String name);
	int getMajorVersion();
	int getMinorVersion();
	Set<String> getResourcePaths(String path);
	InputStream getResourceAsStream(String path);
	String getRealPath(String path);
	String getContextPath();
}
