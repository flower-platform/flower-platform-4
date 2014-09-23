package org.flowerplatform.js_client.server.oauth.client;

import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.UserPrincipal;

/**
 * Keeps the access token for the authenticated user. Any protected resource
 * request will use the token.
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2UserPrincipal extends UserPrincipal {

	private OAuthToken token;

	public OAuth2UserPrincipal(Node user, OAuthToken token) {
		super(user);
		this.token = token;
	}

	public OAuthToken getToken() {
		return token;
	}

	public void setToken(OAuthToken token) {
		this.token = token;
	}

}
