package org.flowerplatform.js_client.server.oauth.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.users.UserService;

/**
 * @author Mariana Gheorghe
 */
public class OAuth2Service {

	private OAuthIssuer issuer = new OAuthIssuerImpl(new MD5Generator());

	private Map<String, String> authorizationCode = new HashMap<String, String>();
	
	private Map<String, String> accessToken = new HashMap<String, String>();
	
	public String createAuthorizationCode(Node user, String clientId) {
		try {
			String code = issuer.authorizationCode();
			user.getProperties().put("authorizationCode", code);
			authorizationCode.put(code, user.getNodeUri());
			return code;
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String createAccessToken(Node user, String clientId) {
		try {
			String token = issuer.accessToken();
			user.getProperties().put("accessToken", token);
			accessToken.put(token, user.getNodeUri());
			return token;
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Node validateAuthorizationCode(String code, String clientId) {
		UserService service = ((UserService) CorePlugin.getInstance().getServiceRegistry().getService("userService"));
		String nodeUri = authorizationCode.get(code);
		if (nodeUri == null) {
			return null;
		}
		return service.getUser(nodeUri);
	}
	
	public Node validateAccessToken(String token) {
		UserService service = ((UserService) CorePlugin.getInstance().getServiceRegistry().getService("userService"));
		String nodeUri = accessToken.get(token);
		if (nodeUri == null) {
			return null;
		}
		return service.getUser(nodeUri);
	}
	
}
