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

import java.lang.reflect.Field;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.eclipse.equinox.jsp.jasper.registry.JSPFactory;
import org.osgi.framework.Bundle;

/**
 * Returns {@link CustomJSPServlet} as extension class.
 * 
 * @see servletbridge.registry Activator
 * 
 * @author Cristina Constantinescu
 */
public class CustomJSPFactory extends JSPFactory {
	/**
	 * @author see class
	 */
	public Object create() throws CoreException {
		JspServlet jspServlet = (JspServlet) super.create();
		
		try {
			Field jspBundle = jspServlet.getClass().getDeclaredField("bundle");
			jspBundle.setAccessible(true);
			Object bundle = jspBundle.get(jspServlet);
			
			Field jspAlias = jspServlet.getClass().getDeclaredField("alias");
			jspAlias.setAccessible(true);
			Object alias = jspAlias.get(jspServlet);
			
			Field jspBundleResourcePath = jspServlet.getClass().getDeclaredField("bundleResourcePath");
			jspBundleResourcePath.setAccessible(true);
			Object bundleResourcePath = jspBundleResourcePath.get(jspServlet);
			
			return new CustomJSPServlet((Bundle) bundle, (String) bundleResourcePath, (String) alias);
		} catch (Exception e) {	
			throw new RuntimeException(e);
		}
	}
	
}