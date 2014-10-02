package org.flowerplatform.js_client.server.oauth.client;

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
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.IUserPrincipalProvider;
import org.flowerplatform.core.users.UserService;
import org.flowerplatform.util.Utils;
import org.json.JSONObject;

/**
 * Create a user principal that will contain the user information stored on the
 * resource server (e.g. login, email), and the access token used to obtain
 * protected resources.
 * 
 * @see OAuth2UserPrincipal
 * 
 * @author Mariana Gheorghe
 */
public enum OAuth2UserPrincipalProvider implements IUserPrincipalProvider {

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
		protected Map<String, Object> getUserInfo(OAuthClient oauthClient, String accessToken, Map<String, Object> properties) {
			try {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("login", properties.get("login"));
				result.put("name", properties.get("name"));
				result.put("avatar", properties.get("avatar_url"));
				result.put("website", properties.get("html_url"));
				
				// get user email
				OAuthClientRequest oauthBearerRequest = new OAuthBearerClientRequest("https://api.github.com/user/emails")
						.setAccessToken(accessToken).buildHeaderMessage();
				OAuthResourceResponse oauthResourceResponse = oauthClient.resource(oauthBearerRequest, null, OAuthResourceResponse.class);
				// the response is an array; wrap it in a JSON so the parser can recognize it as valid
				Map<String, Object> emails = JSONUtils.parseJSON("{ emails: " + oauthResourceResponse.getBody() + "}");
				result.put("email", getEmail(emails.get("emails")));
				
				return result;
			} catch (OAuthSystemException | OAuthProblemException e) {
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
				Map<String, Object> properties) {
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
				Map<String, Object> properties) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	@Override
	public Principal createUserPrincipal(String accessToken) {
		try {
			// request authenticated user
			OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
			OAuthClientRequest oauthBearerRequest = new OAuthBearerClientRequest(userEndpoint())
					.setAccessToken(accessToken)
					.buildHeaderMessage();
			OAuthResourceResponse oauthResourceResponse = oauthClient.resource(oauthBearerRequest, null, OAuthResourceResponse.class);
			Map<String, Object> properties = JSONUtils.parseJSON(oauthResourceResponse.getBody());
			
			// filter result
			properties = getUserInfo(oauthClient, accessToken, properties);
			
			// create principal
			String login = (String) properties.get("login");
			login = this.name() + "@" + login;
			String nodeUri = Utils.getUri("fpp", "|.users", login);
			
			UserService userService = (UserService) CorePlugin.getInstance().getServiceRegistry().getService("userService");
			Node user = userService.getUser(nodeUri);
			if (user == null) {
				user = new Node(null, "user");
				user.setProperties(properties);
				user.getProperties().put("login", login);
				user = userService.saveUser(user);
			}
			
			return new OAuth2UserPrincipal(user, new BasicOAuthToken(accessToken));
		} catch (OAuthSystemException | OAuthProblemException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the endpoint where the authorized user information can be obtained
	 */
	protected abstract String userEndpoint();

	/**
	 * Filter the properties map obtained from the user endpoint. The {@link OAuthClient} and access token
	 * are also provided in case extra info is needed from the resource server.
	 */
	protected abstract Map<String, Object> getUserInfo(OAuthClient oauthClient, String accessToken, Map<String, Object> properties);

}
