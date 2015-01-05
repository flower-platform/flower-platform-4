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
package org.flowerplatform.util.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.osgi.framework.Bundle;

/**
 * Besides the super functionality, adds additional attributes from  {@link ServletUtils} to its context; 
 * the most important is {@link InstanceManager}.
 * 
 * @see servletbridge.registry Activator
 * @author Cristina Constantinescu
 */
public class CustomJSPServlet extends JspServlet  {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 *@author see class
	 **/
	public CustomJSPServlet(Bundle bundle, String bundleResourcePath, String alias) {
		super(bundle, bundleResourcePath, alias);		
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {		
		super.init(config);
		ServletUtils.addAllAdditionalAttributesToServletContext(getServletContext());		
	}
	
}