package org.flowerplatform.js_client.server;

import static org.flowerplatform.core.CoreConstants.LOGIN_METHOD;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.core.users.UserPrincipal;
import org.flowerplatform.core.users.UserValidator;

/**
 * @author Valentina-Camelia Bojan
 * @author Mariana Gheorghe
 */
public class UserValidatorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserValidator userValidator = new UserValidator();

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginMethod = request.getParameter(LOGIN_METHOD);
		Principal userPrincipal = userValidator.getCurrentUserPrincipal(request.getSession());

		switch (loginMethod) {
		// TODO VB: Login with Facebook
		case "facebook": {
			String email = request.getParameter("email");
			userPrincipal = new UserPrincipal("facebookUser", email);
			break;
		}
		// TODO VB: Login with Google+
		case "google": {
			String email = request.getParameter("email");
			userPrincipal = new UserPrincipal("googleUser", email);
			break;
		}
		// TODO VB: Usual login (username & password)
		default:
			// TODO VB: Get the user's info (username & password)
			String name = request.getParameter("name");
			String password = request.getParameter("password");

			// TODO VB: Validate the user who tries to login in.
			userPrincipal = userValidator.validateUser(name, password);
			break;
		}

		userValidator.setCurrentUserPrincipal(request.getSession(), userPrincipal);

		// TODO VB: If valid, redirect the client to the desired page,
		// TODO VB: else redirect to error page.
		if (userPrincipal == null) {
			response.sendRedirect("/org.flowerplatform.host.web_app/authenticate/loginError.html");
		} else {
			// TODO VB: If the user has accessed directly the login page
			// TODO VB: the redirect him to a main page.
//			response.sendRedirect("");
		}

	}

}