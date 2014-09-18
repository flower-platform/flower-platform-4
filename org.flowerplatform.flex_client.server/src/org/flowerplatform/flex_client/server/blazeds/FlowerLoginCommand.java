package org.flowerplatform.flex_client.server.blazeds;

import java.security.Principal;
import java.util.List;

import javax.servlet.ServletConfig;

import org.flowerplatform.core.users.UserValidator;

import flex.messaging.FlexContext;
import flex.messaging.security.LoginCommand;

/**
 * @author Valentina-Camelia Bojan
 * @author Mariana Gheorghe
 */
public class FlowerLoginCommand implements LoginCommand {

	private UserValidator userValidator = new UserValidator();

	@Override
	public Principal doAuthentication(String username, Object credentials) {
		Principal principal = userValidator.getCurrentUserPrincipal(FlexContext.getHttpRequest().getSession());
		if (principal != null) {
			// there is already a user logged in for this session
			// this can happen when the login was done from JS
			// validate same username
			if (!username.equals(principal.getName())) {
				throw new RuntimeException("Trying to log in with a different user");
			}
		} else {
			// no user logged in
			// authenticate the user
			principal = userValidator.validateUser(username, credentials.toString());
			if (principal != null) {
				userValidator.setCurrentUserPrincipal(FlexContext.getHttpRequest().getSession(), principal);
			}
		}
		
		return principal;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean doAuthorization(Principal principal, List roles) {
		return true;
	}

	@Override
	public boolean logout(Principal principal) {
		return true;
	}

	@Override
	public void start(ServletConfig config) {
	}

	@Override
	public void stop() {
	}

}