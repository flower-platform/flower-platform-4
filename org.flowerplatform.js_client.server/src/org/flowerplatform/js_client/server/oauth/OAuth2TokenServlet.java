package org.flowerplatform.js_client.server.oauth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

/**
 * Token endpoint. Generates a token for a valid
 * authorization code.
 * 
 * @author Mariana Gheorghe
 */
public class OAuth2TokenServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		System.out.println("GENERATE TOKEN");

		OAuthTokenRequest oauthRequest = null;
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

		try {

			oauthRequest = new OAuthTokenRequest(req);

			// validateClient(oauthRequest);

			String authzCode = oauthRequest.getCode();

			// some code

			String accessToken = oauthIssuerImpl.accessToken();
			String refreshToken = oauthIssuerImpl.refreshToken();
			System.out.println("TOKEN " + accessToken);

			// some code

			OAuthResponse oauthResponse;

			oauthResponse = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).setAccessToken(accessToken)
					.setExpiresIn("3600").setRefreshToken(refreshToken).buildJSONMessage();

			resp.setStatus(oauthResponse.getResponseStatus());
			PrintWriter pw = resp.getWriter();
			pw.print(oauthResponse.getBody());
			pw.flush();
			pw.close();
		} catch (OAuthSystemException | OAuthProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
