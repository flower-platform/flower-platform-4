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
package org.eclipse.equinox.servletbridge.flower;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * @see FlowerFrameworkLauncher
 * @author Cristian Spiescu
 */
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