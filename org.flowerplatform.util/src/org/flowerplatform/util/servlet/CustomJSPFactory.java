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
	
	public Object create() throws CoreException {
		JspServlet jspServlet = (JspServlet) super.create();
		
		try {
			Field jspBundle = jspServlet.getClass().getDeclaredField("bundle");
			jspBundle.setAccessible(true);
			Object bundle = jspBundle.get(jspServlet);
			
			Field jspAlias = jspServlet.getClass().getDeclaredField("alias");
			jspAlias.setAccessible(true);
			Object alias= jspAlias.get(jspServlet);
			
			Field jspBundleResourcePath = jspServlet.getClass().getDeclaredField("bundleResourcePath");
			jspBundleResourcePath.setAccessible(true);
			Object bundleResourcePath = jspBundleResourcePath.get(jspServlet);
			
			return new CustomJSPServlet((Bundle) bundle, (String) bundleResourcePath, (String) alias);
		} catch (Exception e) {	
			throw new RuntimeException(e);
		}
	}
	
}
