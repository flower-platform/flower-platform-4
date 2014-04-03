package org.flowerplatform.host.servletbridge.registry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.tomcat.InstanceManager;
import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.osgi.framework.Bundle;

/**
 * Besides the super functionality, adds {@link InstanceManager} as attribute to its context.
 * 
 * @see Activator#instanceManager
 * 
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
		getServletContext().setAttribute(InstanceManager.class.getName(), Activator.instanceManager);		
	}
	
}
