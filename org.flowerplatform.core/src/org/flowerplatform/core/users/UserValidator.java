package org.flowerplatform.core.users;

import static org.flowerplatform.core.CoreConstants.USER_PRINCIPAL;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.flowerplatform.core.CorePlugin;

/**
 * @author Mariana Gheorghe
 */
public class UserValidator {

	public Principal validateUser(String login, String password) {
		// TODO implement
//		if (login.equals("a") && password.equals("a")) {
			return new UserPrincipal(login);
//		};
//		return null;
	}
	
	public Principal getCurrentUserPrincipal(HttpSession session) {
		return (Principal) session.getAttribute(USER_PRINCIPAL);
	}

	public void setCurrentUserPrincipal(HttpSession session, Principal principal) {
		session.setAttribute(USER_PRINCIPAL, principal);
	}
	
	public void clearCurrentUserPrincipal(HttpSession session) {
		session.setAttribute(USER_PRINCIPAL, null);
	}

}
