package org.flowerplatform.js_client.server.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

/**
 * Authorization endpoint. Should request resource owner credentials
 * and authorize the client application if credentials are valid.
 * 
 * <p>
 * Redirect to the client endpoint if authorization is completed.
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2AuthorizationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private OAuthIssuer oauthIssuer = new OAuthIssuerImpl(new MD5Generator());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// dynamically recognize an OAuth profile based on request characteristic (params,
			// method, content type etc.), perform validation
			OAuthAuthzRequest oauthRequest = null;
			try {
				oauthRequest = new OAuthAuthzRequest(req);
			} catch (OAuthSystemException | OAuthProblemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// validateRedirectionURI(oauthRequest);

			// build OAuth response
			OAuthResponse oauthResponse = null;
			try {
				oauthResponse = OAuthASResponse
						.authorizationResponse(req, HttpServletResponse.SC_FOUND)
						.setCode(oauthIssuer.authorizationCode())
						.location(oauthRequest.getRedirectURI())
						.buildQueryMessage();
			} catch (OAuthSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("REDIRECT TO " + oauthResponse.getLocationUri());
			resp.sendRedirect(oauthResponse.getLocationUri());
	}

}
