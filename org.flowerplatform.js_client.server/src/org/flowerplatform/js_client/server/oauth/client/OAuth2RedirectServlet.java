package org.flowerplatform.js_client.server.oauth.client;

import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_EMBEDDING_CLIENT_ID;
import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_PROVIDER;
import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_STATE;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.js_client.server.JsClientServerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client application redirection endpoint. Trades the authorization code for a
 * token that will be used to obtain protected resources from the resource server.
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2RedirectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2RedirectServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			OAuthAuthzResponse authAuthzResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(req);
			
			// validate state
			validateState(authAuthzResponse);
			
			// get authorization code and provider from the request
			String code = authAuthzResponse.getCode();
			String provider = authAuthzResponse.getParam(OAUTH_PROVIDER);
			
			// get token location and client credentials based on provider
			String tokenLocation = null;
			if ("flower_platform".equals(provider)) {
				tokenLocation = "http://csp41:9090/org.flowerplatform.host.web_app/oauth/token";
			} else {
				tokenLocation = OAuthProviderType.valueOf(provider.toUpperCase()).getTokenEndpoint();
			}
			OAuth2ProviderService providerService = (OAuth2ProviderService) CorePlugin.getInstance().getServiceRegistry()
					.getService("oauthProviderService");
			OAuth2Provider credentials = providerService.getOAuthProvider(provider);
			
			// create token request
			OAuthClientRequest oauthRequest = JsClientServerPlugin.getInstance().buildQueryMessage(
					OAuthClientRequest
					.tokenLocation(tokenLocation)
					.setClientId(credentials.getClientId())
					.setClientSecret(credentials.getClientSecret())
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setCode(code)
					.setRedirectURI(req.getRequestURL().toString()));

			// sync call to get token
			OAuthAccessTokenResponse oauthTokenResponse = getAccessTokenResponse(oauthRequest, provider);
			String accessToken = oauthTokenResponse.getAccessToken();
			// TODO refresh token?
			
			// forward to login servlet
			UriBuilder builder = UriBuilder.fromPath("/oauth/login")
					.queryParam(OAUTH_PROVIDER, provider)
					.queryParam(OAuth.OAUTH_ACCESS_TOKEN, accessToken);
			String embeddingClientId = (String) req.getSession().getAttribute(OAUTH_EMBEDDING_CLIENT_ID);
			if (embeddingClientId != null) {
				builder.queryParam(OAUTH_EMBEDDING_CLIENT_ID, embeddingClientId);
			}
			URI login = builder.build();
			req.getRequestDispatcher(login.toString()).forward(req, resp);
		} catch (OAuthProblemException e) {
			OAuthResponse oauthResponse = JsClientServerPlugin.getInstance().buildJSONMessage(
					OAuthResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.error(e));
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(oauthResponse.getBody());
			}
			JsClientServerPlugin.getInstance().writeHttpResponse(resp, oauthResponse);
		}
	}
	
	private void validateState(OAuthAuthzResponse authAuthzResponse) {
		String actualState = authAuthzResponse.getState();
		Object obj = authAuthzResponse.getRequest().getSession().getAttribute(OAUTH_STATE);
		String expectedState = obj == null ? "" : (String) obj;
		if (!expectedState.equals(actualState)) {
			throw new RuntimeException("Invalid OAuth state");
		}
	}
	
	private OAuthAccessTokenResponse getAccessTokenResponse(OAuthClientRequest oauthRequest, String provider) throws OAuthProblemException {
		OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
		try {
			return oauthClient.accessToken(oauthRequest, 
					provider.equals("github") ? GitHubTokenResponse.class : OAuthJSONAccessTokenResponse.class); // special case for GH
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
}
