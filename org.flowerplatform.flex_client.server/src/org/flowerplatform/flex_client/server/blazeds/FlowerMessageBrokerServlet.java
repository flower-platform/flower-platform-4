package org.flowerplatform.flex_client.server.blazeds;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.core.CorePlugin;

import flex.messaging.FlexContext;
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

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) {
		try {
			CorePlugin.getInstance().setRequest(req);
			super.service(req, res);
		} finally {
			CorePlugin.getInstance().clearRequest();
		}
	}

}
