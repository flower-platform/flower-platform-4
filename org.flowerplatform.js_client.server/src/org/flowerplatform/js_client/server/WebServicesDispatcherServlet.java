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

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.UserPrincipal;
import org.flowerplatform.js_client.server.oauth.server.OAuth2Service;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public class WebServicesDispatcherServlet extends ServletContainer {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServicesDispatcherServlet.class);

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
					JsClientServerPlugin.getInstance().setCookie(request, response, "JSESSIONID", sessionIdFromFlex, true);
				}
			}
			
			// check authorization header in case of an OAuth login
			if (request.getHeader(OAuth.HeaderType.AUTHORIZATION) != null) {
				String accessToken = getAccessToken(request);
				OAuth2Service service = (OAuth2Service) CorePlugin.getInstance().getServiceRegistry().getService("oauthService");
				Node user = service.validateAccessToken(accessToken);
				if (user == null) {
					throw OAuthProblemException.error(OAuthError.ResourceResponse.INVALID_TOKEN).responseStatus(SC_UNAUTHORIZED);
				}
				
				// TODO validate scope? or let the service method throw INSUFFICIENT_SCOPE error (in this case wrap the super call)
				
				// valid authorization token => set user principal
				request.getSession().setAttribute(CoreConstants.USER_PRINCIPAL, new UserPrincipal(user));
			}
			
			CorePlugin.getInstance().getRequestThreadLocal().set(request);
			super.service(request, response);
		} catch (OAuthProblemException e) {
			OAuthResponse oauthResponse = JsClientServerPlugin.getInstance().buildJSONMessage(
					OAuthResponse
					.errorResponse(e.getResponseStatus())
					.error(e));
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(oauthResponse.getBody());
			}
			JsClientServerPlugin.getInstance().writeHttpResponse(response, oauthResponse);
		} finally {
			CorePlugin.getInstance().getRequestThreadLocal().remove();
		}
	}
	
	/**
	 * @throws OAuthProblemException {@link OAuthError.ResourceResponse#INVALID_REQUEST}
	 */
	private String getAccessToken(HttpServletRequest request) throws OAuthProblemException {
		try {
			OAuthAccessResourceRequest oauthResourceRequest = new OAuthAccessResourceRequest(request);
			return oauthResourceRequest.getAccessToken();
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
}
