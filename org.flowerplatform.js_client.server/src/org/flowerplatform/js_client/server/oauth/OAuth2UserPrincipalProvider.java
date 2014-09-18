package org.flowerplatform.js_client.server.oauth;

import java.security.Principal;
import java.util.HashMap;
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
import org.json.JSONObject;

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
				OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
				OAuthClientRequest oauthBearerRequest = new OAuthBearerClientRequest("https://api.github.com/user")
						.setAccessToken(accessToken).buildHeaderMessage();
				OAuthResourceResponse oauthResourceResponse = oauthClient.resource(oauthBearerRequest, null,
						OAuthResourceResponse.class);
				Map<String, Object> user = JSONUtils.parseJSON(oauthResourceResponse.getBody());

				// get user email
				oauthBearerRequest = new OAuthBearerClientRequest("https://api.github.com/user/emails")
						.setAccessToken(accessToken).buildHeaderMessage();
				oauthResourceResponse = oauthClient.resource(oauthBearerRequest, null, OAuthResourceResponse.class);
				// the response is an array; wrap it in a JSON so the parser can recognize it as valid
				Map<String, Object> emails = JSONUtils.parseJSON("{ emails: " + oauthResourceResponse.getBody() + "}");
				
				// create principal
				String login = (String) user.get("login");
				Map<String, Object> info = new HashMap<String, Object>();
				info.put("login", login);
				info.put("email", getEmail(emails.get("emails")));
				info.put("name", user.get("name"));
				info.put("avatar", user.get("avatar_url"));
				info.put("website", user.get("html_url"));
				return new OAuth2UserPrincipal(login, info, new BasicOAuthToken(accessToken));
			} catch (OAuthSystemException | OAuthProblemException e) {
				throw new RuntimeException(e);
			}
		}
		
		private String getEmail(Object emails) {
			// this user has only one email
			for (Object email : ((Object[]) emails)) {
				if (((JSONObject) email).getBoolean("primary")) {
					return ((JSONObject) email).getString("email");
				}
			}
			return null;
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
