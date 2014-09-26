package org.flowerplatform.js_client.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.UserPrincipal;
import org.flowerplatform.js_client.server.oauth.server.OAuth2Service;
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
					JsClientServerPlugin.getInstance().setCookie(request, response, "JSESSIONID", sessionIdFromFlex, true);
				}
			}
			
			// check authorization header in case of an OAuth login
			if (request.getHeader(OAuth.HeaderType.AUTHORIZATION) != null) {
				OAuthAccessResourceRequest oauthResourceRequest = new OAuthAccessResourceRequest(request);
				String token = oauthResourceRequest.getAccessToken();
				OAuth2Service service = (OAuth2Service) CorePlugin.getInstance().getServiceRegistry().getService("oauthService");
				Node user = service.validateAccessToken(token);
				if (user == null) {
					throw OAuthProblemException.error("Invalid access token");
				}
				
				// valid authorization token => set user principal
				request.getSession().setAttribute(CoreConstants.USER_PRINCIPAL, new UserPrincipal(user));
			}
			
			CorePlugin.getInstance().getRequestThreadLocal().set(request);
			super.service(request, response);
		} catch (OAuthSystemException | OAuthProblemException e) {
			throw new RuntimeException(e);
		} finally {
			CorePlugin.getInstance().getRequestThreadLocal().remove();
		}
	}
	
}
