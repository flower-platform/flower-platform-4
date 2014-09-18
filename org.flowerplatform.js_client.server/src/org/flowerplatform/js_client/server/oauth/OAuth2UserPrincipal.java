package org.flowerplatform.js_client.server.oauth;

import java.util.Map;

import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.flowerplatform.core.users.UserPrincipal;

/**
 * Keeps the access token for the authenticated user.
 * Any protected resource request will use the token.
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2UserPrincipal extends UserPrincipal {

	private OAuthToken token;

	public OAuth2UserPrincipal(String username, Map<String, Object> info, OAuthToken token) {
		super(username, info);
		this.token = token;
	}

	public OAuthToken getToken() {
		return token;
	}

	public void setToken(OAuthToken token) {
		this.token = token;
	}
	
}
