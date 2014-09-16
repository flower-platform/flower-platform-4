package org.flowerplatform.js_client.java;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;


public class MainClientLoginTest {
	
	public static void main(final String[] args) throws Exception {
		try {
			OAuthClientRequest auth = OAuthClientRequest
					   .authorizationLocation("http://localhost:8080/org.flowerplatform.host.web_app/oauth/authorize")
					   .setClientId("clientId")
					   .setResponseType(OAuth.OAUTH_CODE)
					   .setRedirectURI("http://localhost:8080/org.flowerplatform.host.web_app/oauth/redirect")
					   .buildQueryMessage();
			System.out.println(auth.getLocationUri());
			
			Client client = ClientBuilder.newClient();
			client.target(auth.getLocationUri()).request().get();
		} catch (OAuthSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
