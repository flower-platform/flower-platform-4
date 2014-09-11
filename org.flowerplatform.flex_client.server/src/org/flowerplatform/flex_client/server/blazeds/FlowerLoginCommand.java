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
		// TODO check if same username??
		Principal principal = userValidator.getCurrentUserPrincipal(FlexContext.getHttpRequest().getSession());
		if (principal == null) {
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