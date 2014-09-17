package org.flowerplatform.js_client.server.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.domain.credentials.BasicCredentialsBuilder;
import org.apache.oltu.oauth2.common.domain.credentials.Credentials;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.flowerplatform.core.CorePlugin;

/**
 * Client application redirection endpoint. Trades the authorization code for a
 * token that will be used to obtain protected resources from the resource server.
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2RedirectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// get authorization code and provider from the request
			OAuthAuthzResponse authAuthzResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(req);
			String code = authAuthzResponse.getCode();
			String provider = authAuthzResponse.getParam("provider").toUpperCase();

			// get token location and client credentials based on provider
			String tokenLocation = OAuthProviderType.valueOf(provider).getTokenEndpoint();
			Credentials credentials = getCredentials(provider);

			// create token request
			OAuthClientRequest oauthRequest = OAuthClientRequest
					.tokenLocation(tokenLocation)
					.setClientId(credentials.getClientId())
					.setClientSecret(credentials.getClientSecret())
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setCode(code)
					.buildQueryMessage();

			// sync call to get token
			OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
			GitHubTokenResponse oauthTokenResponse = oauthClient.accessToken(oauthRequest, GitHubTokenResponse.class);
			
			String accessToken = oauthTokenResponse.getAccessToken();
			
			// forward to login servlet
			req.getRequestDispatcher("/oauth/login?provider=" + provider + "&accessToken=" + accessToken).forward(req, resp);
		} catch (OAuthSystemException | OAuthProblemException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Credentials getCredentials(String provider) {
		String clientId = CorePlugin.getInstance().getFlowerProperties().getProperty("oauth.clientId." + provider);
		String clientSecret = CorePlugin.getInstance().getFlowerProperties().getProperty("oauth.clientSecret." + provider);
		return BasicCredentialsBuilder.credentials()
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.build();
	}

}
