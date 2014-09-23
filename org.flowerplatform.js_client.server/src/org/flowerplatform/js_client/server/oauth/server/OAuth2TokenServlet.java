package org.flowerplatform.js_client.server.oauth.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * Token endpoint. Generates an access token for a valid authorization code.
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2TokenServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// get authorization code from request
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(req);
			String authzCode = oauthRequest.getCode();
			String clientId = oauthRequest.getClientId();

			// validate authorization code
			Node user = getService().validateAuthorizationCode(authzCode, clientId);
			if (user == null) {
				throw OAuthProblemException.error("Invalid authorization code");
			}
			
			// generate access token
			String accessToken = getService().createAccessToken(user, clientId);
			
			// write response
			OAuthResponse oauthResponse = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken)
					.buildJSONMessage();
			resp.setStatus(oauthResponse.getResponseStatus());
			PrintWriter pw = resp.getWriter();
			pw.print(oauthResponse.getBody());
			pw.flush();
		} catch (OAuthSystemException | OAuthProblemException e) {
			throw new RuntimeException(e);
		}
	}

	private OAuth2Service getService() {
		return (OAuth2Service) CorePlugin.getInstance().getServiceRegistry().getService("oauthService");
	}
	
}
