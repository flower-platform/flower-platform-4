package org.flowerplatform.flex_client.server.blazeds;

import java.security.Principal;
import java.util.List;

import javax.servlet.ServletConfig;

import org.flowerplatform.core.users.UserPrincipal;

import flex.messaging.FlexContext;
import flex.messaging.security.LoginCommand;

public class FlowerLoginCommand implements LoginCommand{

	@Override
	public void start(ServletConfig config) {}

	@Override
	public void stop() {}

	@Override
	public Principal doAuthentication(String username, Object credentials) {
		
		// TODO VB: Verify the credentials for authentication and set it
		// TODO VB: in the opened session. If success, return the current principal.
		if(username.equals("user") && credentials.equals("password")) {
			FlexContext.getHttpRequest().getSession().setAttribute("userPrincipal", new UserPrincipal(username));
			return new UserPrincipal(username);			
		} else {
			return null;
		}
	}

	@Override
	public boolean doAuthorization(Principal principal, List roles) {
		return true;
	}

	@Override
	public boolean logout(Principal principal) {
		return true;
	}
}