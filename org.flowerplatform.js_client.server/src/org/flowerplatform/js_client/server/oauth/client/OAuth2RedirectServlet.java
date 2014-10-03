package org.flowerplatform.js_client.server.oauth.client;

import static org.flowerplatform.core.CoreConstants.SOCIAL_ACCOUNTS;
import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_EMBEDDING_CLIENT_ID;
import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_PROVIDER;
import static org.flowerplatform.js_client.server.JsClientServerConstants.OAUTH_STATE;

import java.io.IOException;
import java.security.Principal;

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
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.UserPrincipal;
import org.flowerplatform.core.users.UserValidator;
import org.flowerplatform.js_client.server.JsClientServerPlugin;
import org.flowerplatform.js_client.server.oauth.server.OAuth2Service;
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
	
	private UserValidator userValidator = new UserValidator();
	
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
			
			// login
			Principal userPrincipal = userValidator.getCurrentUserPrincipal(req.getSession());

//			if (userPrincipal != null) {
//				// TODO do we reject the login request?
//			}
			
			// get token from provider
			OAuthToken token = getOAuthToken(provider, code);
			userPrincipal = OAuth2UserPrincipalProvider.valueOf(provider.toUpperCase()).createUserPrincipal(token);
			
			if (userPrincipal == null) {
				resp.sendRedirect("/org.flowerplatform.host.web_app/authenticate/loginError.html");
				return;
			}
			
			userValidator.setCurrentUserPrincipal(req.getSession(), userPrincipal);
			
			Node user = ((UserPrincipal) userPrincipal).getUser();
			if (user.getProperties().get(SOCIAL_ACCOUNTS) == null) {
				resp.sendRedirect("/org.flowerplatform.host.web_app/js_client.core/index.html#/link");
				user.getProperties().put("socialAccounts", user.getProperties().get("login") + "@" + provider);
				return;
			}
			
			// embedding client: generate an access token for our server
			String embeddingClientId = (String) req.getSession().getAttribute(OAUTH_EMBEDDING_CLIENT_ID);
			if (embeddingClientId != null) {
				OAuth2Service oauthService = (OAuth2Service) CorePlugin.getInstance().getServiceRegistry().getService("oauthService");
				String fpAccessToken = oauthService.createAccessToken(user, embeddingClientId);
				resp.sendRedirect("/org.flowerplatform.host.web_app/js_client.users/authAccess.html#access_token=" + fpAccessToken);
			} else {
				// go to login page, let the browser handle it
				resp.sendRedirect("/org.flowerplatform.host.web_app/js_client.core/index.html#/auth");
			}
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
	
	private OAuthToken getOAuthToken(String provider, String code) throws OAuthProblemException {
		// get token location and client credentials based on provider
		String tokenLocation = null;
		if ("flower_platform".equals(provider)) {
			tokenLocation = "http://csp41:9090/org.flowerplatform.host.web_app/oauth/token"; // TODO remove
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
				.setCode(code));

		// sync call to get token
		OAuthAccessTokenResponse oauthTokenResponse = null;
		try {
			OAuthClient oauthClient = new OAuthClient(new URLConnectionClient());
			oauthTokenResponse = oauthClient.accessToken(oauthRequest, 
					provider.equals("github") ? GitHubTokenResponse.class : OAuthJSONAccessTokenResponse.class); // special case for GH
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
		
		return oauthTokenResponse.getOAuthToken();
	}
}
