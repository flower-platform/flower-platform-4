package org.flowerplatform.flex_client.server.blazeds;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import flex.messaging.MessageBrokerServlet;

public class FlowerMessageBrokerServlet extends MessageBrokerServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(new ServletConfigWrapper(servletConfig));
	}

}
