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
import org.flowerplatform.util.Pair;

/**
 * @author Mariana Gheorghe
 */
public class OAuth2Service {

	private OAuthIssuer issuer = new OAuthIssuerImpl(new MD5Generator());

	private Map<String, Pair<String, String>> accessToken = new HashMap<String, Pair<String, String>>();
	
	/**
	 * Create an access token for this user and client.
	 */
	public String createAccessToken(Node user, String clientId) {
		try {
			String token = issuer.accessToken();
			// TODO persist it for the user
			// TODO replace the token
			accessToken.put(token, new Pair<String, String>(user.getNodeUri(), clientId));
			return token;
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Find the user who was issued this access token for this client.
	 */
	public Node validateAccessToken(String token) {
		UserService service = ((UserService) CorePlugin.getInstance().getServiceRegistry().getService("userService"));
		Pair<String, String> pair = accessToken.get(token);
		if (pair == null) {
			return null;
		}
		return service.getUser(pair.a);
	}
	
	// TODO refactor
	private Map<String, String> authorizationCode = new HashMap<String, String>();
	
	/**
	 * Create an authorization code for this user and client.
	 */
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
	
	/**
	 * Find the user who was issued this authorization code for this client.
	 */
	public Node validateAuthorizationCode(String code, String clientId) {
		UserService service = ((UserService) CorePlugin.getInstance().getServiceRegistry().getService("userService"));
		String nodeUri = authorizationCode.get(code);
		if (nodeUri == null) {
			return null;
		}
		return service.getUser(nodeUri);
	}
}
