package org.flowerplatform.core.users;

import java.security.Principal;

/**
 * @author Mariana Gheorghe
 */
public interface IUserPrincipalProvider {

	Principal createUserPrincipal(String accessToken);

}