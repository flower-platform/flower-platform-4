package org.flowerplatform.js_client.server.oauth.client;

import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_EMBEDDING_CLIENT_ID;
import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_PROVIDER;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.OAuth;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.UserService;
import org.flowerplatform.core.users.UserValidator;
import org.flowerplatform.js_client.server.oauth.server.OAuth2Service;

/**
 * Use the access token to obtain the authenticated user's
 * information from the resource server (e.g. GitHub, Facebook).
 * 
 * @author Valentina-Camelia Bojan
 * @author Mariana Gheorghe
 */
public class OAuth2LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserValidator userValidator = new UserValidator();

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String provider = request.getParameter(OAUTH_PROVIDER).toUpperCase();
		String accessToken = request.getParameter(OAuth.OAUTH_ACCESS_TOKEN);
		Principal userPrincipal = userValidator.getCurrentUserPrincipal(request.getSession());

//		if (userPrincipal != null) {
//			// TODO do we reject the login request?
//		}
		
		userPrincipal = OAuth2UserPrincipalProvider.valueOf(provider).createUserPrincipal(accessToken);
		
		if (userPrincipal == null) {
			response.sendRedirect("/org.flowerplatform.host.web_app/authenticate/loginError.html");
			return;
		}
		
		userValidator.setCurrentUserPrincipal(request.getSession(), userPrincipal);
		
		// embedding client: generate an access token for our server
		String embeddingClientId = request.getParameter(OAUTH_EMBEDDING_CLIENT_ID);
		if (embeddingClientId != null) {
			OAuth2Service oauthService = (OAuth2Service) CorePlugin.getInstance().getServiceRegistry().getService("oauthService");
			Node user = ((UserService) CorePlugin.getInstance().getServiceRegistry().getService("userService")).getCurrentUser(request);
			String fpAccessToken = oauthService.createAccessToken(user, embeddingClientId);
			response.sendRedirect("/org.flowerplatform.host.web_app/js_client.users/authAccess.html#access_token=" + fpAccessToken);
		} else {
			// go to login page, let the browser handle it
			response.sendRedirect("/org.flowerplatform.host.web_app/js_client.core/index.html#/auth");
		}
	}

}