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
package org.flowerplatform.flex_client.server.blazeds;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.core.CorePlugin;

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
			CorePlugin.getInstance().getRequestThreadLocal().set(req);
			super.service(req, res);
		} finally {
			CorePlugin.getInstance().getRequestThreadLocal().remove();
		}
	}

}