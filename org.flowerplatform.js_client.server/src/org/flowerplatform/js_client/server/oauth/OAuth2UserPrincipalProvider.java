package org.flowerplatform.js_client.server.oauth;

import java.security.Principal;
import java.util.Map;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.token.BasicOAuthToken;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.flowerplatform.core.users.IUserPrincipalProvider;

/**
 * Create a user principal that will contain the user information stored
 * on the resource server (e.g. login, email), and the access token used
 * to obtain protected resources.
 * 
 * @see OAuth2UserPrincipal
 * 
 * @author Mariana Gheorghe
 */
public enum OAuth2UserPrincipalProvider implements IUserPrincipalProvider {

	GITHUB {
		@Override
		public Principal createUserPrincipal(String accessToken) {
			try {
				// request authenticated user
				OAuthClientRequest oauthBearerRequest = new OAuthBearerClientRequest("https://api.github.com/user")
						.setAccessToken(accessToken).buildHeaderMessage();
				OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
				OAuthResourceResponse oauthResourceResponse = oauthClient.resource(oauthBearerRequest, null,
						OAuthResourceResponse.class);
				Map<String, Object> user = JSONUtils.parseJSON(oauthResourceResponse.getBody());

				// create principal
				String login = (String) user.get("login");
				return new OAuth2UserPrincipal(login, new BasicOAuthToken(accessToken));
			} catch (OAuthSystemException | OAuthProblemException e) {
				throw new RuntimeException(e);
			}
		}
	},

	FACEBOOK {
		@Override
		public Principal createUserPrincipal(String accessToken) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	
	GOOGLE {
		@Override
		public Principal createUserPrincipal(String accessToken) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	@Override
	public abstract Principal createUserPrincipal(String accessToken);

}
