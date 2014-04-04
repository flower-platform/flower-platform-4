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
