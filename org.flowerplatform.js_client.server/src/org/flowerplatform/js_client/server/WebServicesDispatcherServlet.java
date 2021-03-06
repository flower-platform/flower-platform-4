/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.js_client.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.core.CorePlugin;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author Mariana Gheorghe
 */
public class WebServicesDispatcherServlet extends ServletContainer {

	private static final long serialVersionUID = 1L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// on Android, the cookies are not shared between the Flex app and the the embedded HTML pages
			// so after a login from Flex, we do a login from JS as well, with the session id used by the Flex request in the request headers
			// we send a set-cookie response header with this session id => and all subsequent requests from JS will use this session id
			String sessionIdFromFlex = request.getHeader("JSESSIONID");
			if (sessionIdFromFlex != null) {
				String sessionId = request.getSession().getId();
				if (!sessionIdFromFlex.equals(sessionId)) {
					String contextPath = request.getContextPath();
					boolean secure = request.isSecure();
					String setCookie = String.format("JSESSIONID=%s; Path=%s/; HttpOnly" + (secure ? "; Secure" : ""), sessionIdFromFlex, contextPath);
					response.addHeader("Set-Cookie", setCookie);
				}
			}

			CorePlugin.getInstance().getRequestThreadLocal().set(request);
			super.service(request, response);
		} finally {
			CorePlugin.getInstance().getRequestThreadLocal().remove();
		}
	}
	
}
