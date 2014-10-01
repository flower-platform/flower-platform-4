package org.flowerplatform.js_client.server.oauth.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.request.AbstractOAuthTokenRequest;
import org.apache.oltu.oauth2.as.request.OAuthUnauthenticatedTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.UserService;
import org.flowerplatform.js_client.server.JsClientServerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Token endpoint. Generates an access token for a valid authorization code (Authorization Code Grant - 
 * {@link #validateAuthorizationCode(OAuthTokenRequest)}) or a valid user credentials 
 * (Resource Owner Password Credentials Grant - {@link #validatePassword(OAuthTokenRequest)}).
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2TokenServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2TokenServlet.class);
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			AbstractOAuthTokenRequest oauthRequest = createOAuthTokenRequest(req); // TODO authenticated clients only? i.e. with secret
			String grant = oauthRequest.getGrantType();
			
			Node user;
			if (GrantType.AUTHORIZATION_CODE.toString().equals(grant)) {
				user = validateAuthorizationCode(oauthRequest);
			} else if (GrantType.PASSWORD.toString().equals(grant)) {
				user = validatePassword(oauthRequest);
			} else {
				throw OAuthProblemException.error(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE);
			}
			
			// generate access token
			String clientId = oauthRequest.getClientId();
			String accessToken = getService().createAccessToken(user, clientId);
			
			// write response
			OAuthResponse oauthResponse = JsClientServerPlugin.getInstance().buildJSONMessage(
					OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken));
			resp.setContentType("application/json;charset=UTF-8");
			resp.setHeader(" Cache-Control", "no-store");
			resp.setHeader("Pragma", "no-cache");
			JsClientServerPlugin.getInstance().writeHttpResponse(resp, oauthResponse);
		} catch (OAuthProblemException e) {
			OAuthResponse oauthResponse = JsClientServerPlugin.getInstance().buildJSONMessage(
					OAuthResponse
					.errorResponse(e.getResponseStatus())
					.error(e));
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(oauthResponse.getBody());
			}
			JsClientServerPlugin.getInstance().writeHttpResponse(resp, oauthResponse);
		}
	}
	
	/**
	 * @see OAuthError.TokenResponse#INVALID_GRANT
	 * 
	 * @param oauthRequest
	 * @return the user that was issued the provided authorization code
	 * @throws OAuthProblemException
	 */
	private Node validateAuthorizationCode(AbstractOAuthTokenRequest oauthRequest) throws OAuthProblemException {
		// get authorization code from request
		String authzCode = oauthRequest.getCode();
		String clientId = oauthRequest.getClientId();

		// validate authorization code
		return validate(getService().validateAuthorizationCode(authzCode, clientId), "Invalid authorization code");
	}

	/**
	 * @see OAuthError.TokenResponse#INVALID_GRANT
	 * 
	 * @param oauthRequest
	 * @return the user identified by the provided credentials
	 * @throws OAuthProblemException
	 */
	private Node validatePassword(AbstractOAuthTokenRequest oauthRequest) throws OAuthProblemException {
		// get user credentials from request
		String username = oauthRequest.getUsername();
		String password = oauthRequest.getPassword();
		
		// validate user credentials
		UserService userService = (UserService) CorePlugin.getInstance().getServiceRegistry().getService("userService");
		return validate(userService.login(username, password), "Invalid username or password");
	}
	
	private Node validate(Node user, String errorDescription) throws OAuthProblemException {
		if (user == null) {
			throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_GRANT, errorDescription);
		}
		return user;
	}
	
	private AbstractOAuthTokenRequest createOAuthTokenRequest(HttpServletRequest req) throws OAuthProblemException {
		try {
			return new OAuthUnauthenticatedTokenRequest(req);
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	private OAuth2Service getService() {
		return (OAuth2Service) CorePlugin.getInstance().getServiceRegistry().getService("oauthService");
	}
	
}
