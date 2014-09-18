package org.flowerplatform.js_client.server.oauth;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.core.users.UserValidator;

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
		String provider = request.getParameter("provider").toUpperCase();
		String accessToken = request.getParameter("accessToken");
		Principal userPrincipal = userValidator.getCurrentUserPrincipal(request.getSession());

		if (userPrincipal != null) {
			// TODO do we reject the login request?
		}
		
		userPrincipal = OAuth2UserPrincipalProvider.valueOf(provider).createUserPrincipal(accessToken);
		
		if (userPrincipal == null) {
			response.sendRedirect("/org.flowerplatform.host.web_app/authenticate/loginError.html");
			return;
		}
		
		userValidator.setCurrentUserPrincipal(request.getSession(), userPrincipal);
		
		// go to login page, let the browser handle it
		response.sendRedirect("/org.flowerplatform.host.web_app/js_client.core/index.html#/login");
	}

}