/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
	/**
	 * @author see class
	 */
	String getTempDir();
	
	/**
	 * @author see class
	 */
	URL getResource(java.lang.String path) throws MalformedURLException;
	
	/**
	 * @author see class
	 */
	void log(String msg);
	
	/**
	 * @author see class
	 */
	void log(String msg, Throwable throwable);
	
	/**
	 * @author see class
	 */
	Object getAttribute(String name);
	
	/**
	 * @author see class
	 */
	int getMajorVersion();
	
	/**
	 * @author see class
	 */
	int getMinorVersion();
	
	/**
	 * @author see class
	 */
	Set<String> getResourcePaths(String path);
	
	/**
	 * @author see class
	 */
	InputStream getResourceAsStream(String path);
	
	/**
	 * @author see class
	 */
	String getRealPath(String path);
	
	/**
	 * @author see class
	 */
	String getContextPath();
}