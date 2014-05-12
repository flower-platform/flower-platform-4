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
package org.flowerplatform.util.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.flowerplatform.util.RunnableWithParam;

/**
 * Stores additional attributes that must be added to a {@link HttpServlet} context.
 * 
 * <p>
 * Additional attributes can be added by:
 * <ul>
 * 	<li> the servletbridge.registry Activator
 *  <li> a {@link RunnableWithParam} instance registered as "org.flowerplatform.host.servletbridge.registry.listeners" extension point
 *  	(used to add properties from other projects; properties that are used by servlet classes from this project)
 * </ul>
 * 
 * @see CustomJSPServlet
 * @see ResourcesServlet
 * 
 * @author Cristina Constantinescu
 */
public class ServletUtils {

	public static final String PROP_USE_FILES_FROM_TEMPORARY_DIRECTORY = "useFilesFromTemporaryDirectory"; 
	public static final String PROP_DEFAULT_USE_FILES_FROM_TEMPORARY_DIRECTORY = "false"; 
	
	private static Map<String, Object> servletContextAdditionalAttributes = new HashMap<String, Object>();
	
	/* package */ static void addAllAdditionalAttributesToServletContext(ServletContext context) {
		for (Map.Entry<String, Object> entry : servletContextAdditionalAttributes.entrySet()) {
			context.setAttribute(entry.getKey(), entry.getValue());
		}
	}
	
	public static void addServletContextAdditionalAttributes(String key, Object value) {
		servletContextAdditionalAttributes.put(key, value);
	}
	
}