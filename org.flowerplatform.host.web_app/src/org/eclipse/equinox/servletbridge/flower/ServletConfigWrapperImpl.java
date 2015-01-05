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

import javax.servlet.ServletConfig;

/**
 * @author Cristian Spiescu
 */
public class ServletConfigWrapperImpl implements ServletConfigWrapper {

	protected ServletConfig servletConfig;
	
	/**
	 *@author see class
	 **/
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