package org.flowerplatform.flex_client.server.blazeds;

import javax.servlet.ServletConfig;

import flex.messaging.config.FlexConfigurationManager;
import flex.messaging.config.ServletResourceResolver;

/**
 * @see FlowerMessageBrokerServlet
 * 
 * @author Cristian Spiescu
 */
public class FlowerFlexConfigurationManager extends FlexConfigurationManager {

	@Override
	protected void setupConfigurationPathAndResolver(ServletConfig servletConfig) {
        configurationPath = "/services-config.xml";
        configurationResolver = new ServletResourceResolver(new DummyServletContext());
	}

}
