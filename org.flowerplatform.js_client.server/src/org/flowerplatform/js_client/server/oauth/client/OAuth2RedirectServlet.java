package org.flowerplatform.js_client.server.oauth.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
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
			OAuthAuthzResponse authAuthzResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(req);
			
			// validate state
			String actualState = authAuthzResponse.getParam("state");
			Object obj = req.getSession().getAttribute("oauthState");
			String expectedState = obj == null ? "" : (String) obj;
			if (!expectedState.equals(actualState)) {
				throw new RuntimeException("Invalid OAuth state");
			}
			
			// get authorization code and provider from the request
			String code = authAuthzResponse.getCode();
			String provider = authAuthzResponse.getParam("provider");
			
			// get token location and client credentials based on provider
			String tokenLocation = null;
			if ("flower_platform".equals(provider)) {
				tokenLocation = "http://csp41:9090/org.flowerplatform.host.web_app/oauth/token";
			} else {
				tokenLocation = OAuthProviderType.valueOf(provider.toUpperCase()).getTokenEndpoint();
			}
			OAuth2Provider credentials = ((OAuth2ProviderService) CorePlugin.getInstance().getServiceRegistry()
					.getService("oauthProviderService")).getOAuthProvider(provider);
			
			// create token request
			OAuthClientRequest oauthRequest = OAuthClientRequest
					.tokenLocation(tokenLocation)
					.setClientId(credentials.getClientId())
					.setClientSecret(credentials.getClientSecret())
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setCode(code)
					.setRedirectURI(req.getRequestURL().toString())
					.buildQueryMessage();

			// sync call to get token
			OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
			OAuthAccessTokenResponse oauthTokenResponse = oauthClient.accessToken(oauthRequest, 
					provider.equals("github") ? GitHubTokenResponse.class : OAuthJSONAccessTokenResponse.class); // special case for GH
			
			String accessToken = oauthTokenResponse.getAccessToken();
			// TODO refresh token?
			
			// forward to login servlet
			req.getRequestDispatcher("/oauth/login?provider=" + provider + "&accessToken=" + accessToken).forward(req, resp);
		} catch (OAuthSystemException | OAuthProblemException e) {
			throw new RuntimeException(e);
		}
	}

}
