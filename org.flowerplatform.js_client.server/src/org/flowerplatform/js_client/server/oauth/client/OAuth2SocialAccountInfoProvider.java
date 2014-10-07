package org.flowerplatform.js_client.server.oauth.client;

import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.USER_AVATAR;
import static org.flowerplatform.core.CoreConstants.USER_EMAIL;
import static org.flowerplatform.core.CoreConstants.USER_LOGIN;
import static org.flowerplatform.core.CoreConstants.USER_WEBSITE;

import java.util.HashMap;
import java.util.Map;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.json.JSONObject;

/**
 * @author Mariana Gheorghe
 */
public enum OAuth2SocialAccountInfoProvider {

	FLOWER_PLATFORM {

		@Override
		protected String userEndpoint() {
			return "http://csp41:9090/org.flowerplatform.host.web_app/ws-dispatcher/users/login";
		}

		@Override
		protected Map<String, Object> getUserInfo(OAuthClient oauthClient, String accessToken, Map<String, Object> properties) {
			Map<String, Object> result = new HashMap<String, Object>();
			JSONObject map = (JSONObject) properties.get("messageResult");
			map = (JSONObject) map.get("properties");
			result.put("login", map.get("login"));
			result.put("name", map.get("name"));
			result.put("email", map.get("email"));
			result.put("avatar", map.get("avatar"));
			return result;
		}
	},

	GITHUB {

		@Override
		protected String userEndpoint() {
			return "https://api.github.com/user";
		}

		@Override
		protected Map<String, Object> getUserInfo(OAuthClient oauthClient, String accessToken, Map<String, Object> properties) throws OAuthProblemException {
			try {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put(USER_LOGIN, properties.get("login"));
				result.put(NAME, properties.get("name"));
				result.put(USER_AVATAR, properties.get("avatar_url"));
				result.put(USER_WEBSITE, properties.get("html_url"));
				
				// get user email
				OAuthClientRequest oauthBearerRequest = new OAuthBearerClientRequest("https://api.github.com/user/emails")
						.setAccessToken(accessToken).buildHeaderMessage();
				OAuthResourceResponse oauthResourceResponse = oauthClient.resource(oauthBearerRequest, null, OAuthResourceResponse.class);
				// the response is an array; wrap it in a JSON so the parser can recognize it as valid
				Map<String, Object> emails = JSONUtils.parseJSON("{ emails: " + oauthResourceResponse.getBody() + "}");
				result.put(USER_EMAIL, getEmail(emails.get("emails")));
				
				return result;
			} catch (OAuthSystemException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * @return the primary email address
		 */
		private String getEmail(Object emails) {
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
		protected String userEndpoint() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected Map<String, Object> getUserInfo(OAuthClient oauthClient, String accessToken,
				Map<String, Object> properties) throws OAuthProblemException {
			// TODO Auto-generated method stub
			return null;
		}

	},

	GOOGLE {

		@Override
		protected String userEndpoint() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected Map<String, Object> getUserInfo(OAuthClient oauthClient, String accessToken,
				Map<String, Object> properties) throws OAuthProblemException {
			// TODO Auto-generated method stub
			return null;
		}

	};

	/**
	 * @param token access token
	 * @return user information from the provider
	 * @throws OAuthProblemException
	 */
	public Map<String, Object> getUserInfo(OAuthToken token) throws OAuthProblemException {
		try {
			// request authenticated user from provider
			OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
			OAuthClientRequest oauthBearerRequest = new OAuthBearerClientRequest(userEndpoint())
					.setAccessToken(token.getAccessToken())
					.buildHeaderMessage();
			OAuthResourceResponse oauthResourceResponse = oauthClient.resource(oauthBearerRequest, null, OAuthResourceResponse.class);
			Map<String, Object> properties = JSONUtils.parseJSON(oauthResourceResponse.getBody());
			
			// filter result
			properties = getUserInfo(oauthClient, token.getAccessToken(), properties);
			return properties;
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the endpoint where the authorized user information can be obtained
	 */
	protected abstract String userEndpoint();

	/**
	 * Filter the properties map obtained from the user endpoint. The {@link OAuthClient} and access token
	 * are also provided in case extra info needs to be requested from the provider (e.g. user email).
	 */
	protected abstract Map<String, Object> getUserInfo(OAuthClient oauthClient, String accessToken, Map<String, Object> properties) throws OAuthProblemException;

}
