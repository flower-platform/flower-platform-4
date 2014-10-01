package org.flowerplatform.js_client.server;

/**
 * @author Mariana Gheorghe
 */
public final class JsClientServerConstants {

	/**
	 * This query parameter is expected for the redirect endpoint to identify
	 * the OAuth2 provider.
	 */
	public static final String OAUTH_PROVIDER = "provider";
	
	//////////////////////////////////
	// Session attributes
	//////////////////////////////////
	
	public static final String OAUTH_STATE = "oauthState";
	public static final String OAUTH_EMBEDDING_CLIENT_ID = "oauthEmbeddingClientId";
	
	private JsClientServerConstants() {
		// utility class needs private constructor
	}
}
