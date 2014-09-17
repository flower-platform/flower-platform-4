package org.flowerplatform.core.users;

import java.security.Principal;

/**
 * @author Mariana Gheorghe
 */
public interface IUserPrincipalProvider {

	/**
	 * Create a user principal that will store the access token.
	 */
	Principal createUserPrincipal(String accessToken);

}