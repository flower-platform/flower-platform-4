package org.flowerplatform.js_client.server.oauth.client;

import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_STATE;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.flowerplatform.core.CorePlugin;

/**
 * Reads the available OAuth2 providers from the user's properties file.
 * 
 * @author Mariana Gheorghe
 */
@Path("/oauthProviders")
public class OAuth2ProviderService {

	private Map<String, OAuth2Provider> providers = new HashMap<String, OAuth2Provider>();

	public OAuth2ProviderService() {
		// read providers from properties
		String names = CorePlugin.getInstance().getFlowerProperties().getProperty("oauth.providers");
		if (names != null) {
			for (String name : names.split(",")) {
				providers.put(name, new OAuth2Provider(name));
			}
		}
	}
	
	/**
	 * @return the {@link OAuth2Provider} with this name
	 */
	public OAuth2Provider getOAuthProvider(String name) {
		return providers.get(name);
	}
	
	/**
	 * @return the providers map and a random state
	 */
	@GET
	public Map<String, Object> getOAuthProviders() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("providers", providers);
		response.put("state", generateState());
		return response;
	}
	
	/**
	 * Generate a state string and keep it in the session attributes.
	 */
	private String generateState() {
		try {
			String state = new MD5Generator().generateValue();
			HttpSession session = CorePlugin.getInstance().getRequestThreadLocal()
					.get().getSession();
			session.setAttribute(OAUTH_STATE, state);
			return state;
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
}
