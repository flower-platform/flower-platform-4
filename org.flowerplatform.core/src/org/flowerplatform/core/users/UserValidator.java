package org.flowerplatform.core.users;

import static org.flowerplatform.core.CoreConstants.USER_PRINCIPAL;

import java.security.Principal;

import javax.servlet.http.HttpSession;

/**
 * @author Mariana Gheorghe
 */
public class UserValidator {

	/**
	 * @param login
	 * @param password
	 * @return
	 */
	public Principal validateUser(String login, String password) {
		// TODO implement
//		if (login.equals("a") && password.equals("a")) {
			return new UserPrincipal(login, null);
//		};
//		return null;
	}
	
	/**
	 * @param session
	 * @return
	 */
	public Principal getCurrentUserPrincipal(HttpSession session) {
		return (Principal) session.getAttribute(USER_PRINCIPAL);
	}

	/**
	 * @param session
	 * @param principal
	 */
	public void setCurrentUserPrincipal(HttpSession session, Principal principal) {
		session.setAttribute(USER_PRINCIPAL, principal);
	}
	
	/**
	 * @param session
	 */
	public void clearCurrentUserPrincipal(HttpSession session) {
		session.setAttribute(USER_PRINCIPAL, null);
	}

}
