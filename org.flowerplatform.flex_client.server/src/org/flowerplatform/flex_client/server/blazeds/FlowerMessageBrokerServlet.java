package org.flowerplatform.flex_client.server.blazeds;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import flex.messaging.MessageBrokerServlet;

/**
 * The classes from this package, override classes from BlazeDS so that we can
 * replace the mechanism that reads the configuration files (i.e. {@link DummyServletContext#getResourceAsStream(String)}).
 * 
 * @see DummyServletContext#getResourceAsStream(String)
 * @author Cristian Spiescu
 */
public class FlowerMessageBrokerServlet extends MessageBrokerServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(new ServletConfigWrapper(servletConfig));
	}

}
