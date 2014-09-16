package org.flowerplatform.js_client.server.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * Client application redirection endpoint. Trades the authorization
 * code for a token that will be used to obtain protected resources.
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2RedirectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = null;
		try {
			OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(req);
			code = oar.getCode();
		} catch (OAuthProblemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("REDIRECTED " + code);

		try {
			OAuthClientRequest oauthRequest = OAuthClientRequest
					.tokenLocation("http://localhost:8080/org.flowerplatform.host.web_app/oauth/token")
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId("clientId")
					.setClientSecret("clientSecret")
					.setRedirectURI("http://localhost:8080/org.flowerplatform.host.web_app/")
					.setCode(code).buildQueryMessage();

			// create OAuth client that uses custom http client under the hood
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

			OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(oauthRequest, OAuthJSONAccessTokenResponse.class);

			String accessToken = oAuthResponse.getAccessToken();
			Long expiresIn = oAuthResponse.getExpiresIn();
			System.out.println("GET TOKEN " + accessToken);

		} catch (OAuthSystemException | OAuthProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
