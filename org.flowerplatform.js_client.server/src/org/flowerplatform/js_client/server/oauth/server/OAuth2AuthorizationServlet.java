package org.flowerplatform.js_client.server.oauth.server;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.UserService;

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

	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// check if user logged in
		UserService service = ((UserService) CorePlugin.getInstance().getServiceRegistry().getService("userService"));
		Node currentUser = service.getCurrentUser(req);
		if (currentUser == null) {
			resp.sendRedirect("/org.flowerplatform.host.web_app/js_client.core/index.html#/login?return_to=/oauth/authorize?" 
					+ URLEncoder.encode(req.getQueryString())); // encode; otherwise the first parameter is not sent
			return;
		}
		
		try {
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(req);
			Node client = getClient(oauthRequest.getClientId());
			
			// TODO check if already approved
			
			// TODO display authorization form; wait for approval
			
			boolean approved = true;
			if (approved) {
				
				// TODO validate redirect uri
				
				String redirectUri = (String) client.getProperties().get("callback");
				
				// generate authorization code
				String code = getService().createAuthorizationCode(currentUser, oauthRequest.getClientId());
				
				// build response
				OAuthResponse oauthResponse = OAuthASResponse
						.authorizationResponse(req, HttpServletResponse.SC_FOUND)
						.setCode(code)
						.location(redirectUri)
						.buildQueryMessage();
				
				resp.sendRedirect(oauthResponse.getLocationUri());
			}
		} catch (OAuthSystemException | OAuthProblemException e) {
			throw new RuntimeException(e);
		}
	}

	private OAuth2Service getService() {
		return (OAuth2Service) CorePlugin.getInstance().getServiceRegistry().getService("oauthService");
	}
	
	private Node getClient(String clientId) {
		if ("testclientfp".equals(clientId)) {
			Node client = new Node("fpp:|oauth2ClientsFile#123", "oauth2Client");
			client.getProperties().put("clientId", clientId);
			client.getProperties().put("clientSecret", "testclientfpsecret");
			client.getProperties().put("name", "FP Test");
			client.getProperties().put("description", "Some description goes here.");
			client.getProperties().put("homepage", "http://localhost:8080/org.flowerplatform.host.web_app/");
			client.getProperties().put("callback", "http://localhost:8080/org.flowerplatform.host.web_app/oauth/redirect?provider=flower_platform");
			return client;
		}
		throw new RuntimeException("Client not registered");
	}
	
}
