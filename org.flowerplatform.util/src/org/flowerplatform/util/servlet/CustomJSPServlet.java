package org.flowerplatform.util.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.tomcat.InstanceManager;
import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.flowerplatform.util.servlet.ServletUtils;
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
	
	public CustomJSPServlet(Bundle bundle, String bundleResourcePath, String alias) {
		super(bundle, bundleResourcePath, alias);		
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {		
		super.init(config);
		ServletUtils.addAllAdditionalAttributesToServletContext(getServletContext());		
	}
	
}
