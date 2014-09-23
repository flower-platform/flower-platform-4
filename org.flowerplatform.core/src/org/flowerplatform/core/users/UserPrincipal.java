package org.flowerplatform.core.users;

import java.security.Principal;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Valentina-Camelia Bojan
 * @author Mariana Gheorghe
 */
public class UserPrincipal implements Principal {

	private Node user;

	public UserPrincipal() {
		// nothing to do
	}

	public UserPrincipal(Node user) {
		this.user = user;
	}

	public String getName() {
		return user.getNodeUri();
	}
	
	public Node getUser() {
		return user;
	}

}