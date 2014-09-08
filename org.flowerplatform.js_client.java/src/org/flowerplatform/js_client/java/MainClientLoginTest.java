package org.flowerplatform.js_client.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.glassfish.jersey.client.oauth1.AccessToken;
import org.glassfish.jersey.client.oauth1.ConsumerCredentials;
import org.glassfish.jersey.client.oauth1.OAuth1AuthorizationFlow;
import org.glassfish.jersey.client.oauth1.OAuth1ClientSupport;


public class MainClientLoginTest {
	
		 private static final BufferedReader IN = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
//		 private static final String FRIENDS_TIMELINE_URI = "https://api.twitter.com/1.1/statuses/home_timeline.json";
//		 private static final Properties PROPERTIES = new Properties();
//		 private static final String PROPERTIES_FILE_NAME = "twitterclient.properties";
		 private static final String PROPERTY_CONSUMER_KEY = "714528398641253";
		 private static final String PROPERTY_CONSUMER_SECRET = "4ce1637f1617d716a8347eae3524c8b3";
//		 private static final String PROPERTY_TOKEN = "token";
//		 private static final String PROPERTY_TOKEN_SECRET = "tokenSecret";
		 
	public static void main(final String[] args) throws Exception {
		ConsumerCredentials consumerCredentials = 
					 	new ConsumerCredentials(PROPERTY_CONSUMER_KEY,PROPERTY_CONSUMER_SECRET);
		OAuth1AuthorizationFlow authFlow = 
					OAuth1ClientSupport.builder(consumerCredentials)
										 .authorizationFlow(
												 "http://localhost:8080/org.flowerplatform.host.web_app/ws-dispatcher/authorize1",
												 "http://localhost:8080/org.flowerplatform.host.web_app/ws-dispatcher/authorize2",
												 "http://localhost:8080/org.flowerplatform.host.web_app/ws-dispatcher/authorize1")
										 .build();
			 
		String authorizationUri = authFlow.start();
		// here we must direct the user to authorization uri to approve
		// our application. The result will be verifier code (String).
		System.out.println("Enter the following URI into a web browser and authorize me:");
		System.out.println(authorizationUri);
		System.out.print("Enter the authorization code: ");
//		final String verifier;
//		try {
//			verifier = IN.readLine();
//		} catch (final IOException ex) {
//			throw new RuntimeException(ex);
//		}
		final AccessToken accessToken = authFlow.finish("ceva");
		System.out.println(accessToken.getToken());
//		 // store access token for next application execution
//		 PROPERTIES.setProperty(PROPERTY_TOKEN, accessToken.getToken());
//		 PROPERTIES.setProperty(PROPERTY_TOKEN_SECRET, accessToken.getAccessTokenSecret());
//		 // get the feature that will configure the client with consumer credentials and
//		 // received access token
//		 filterFeature = authFlow.getOAuth1Feature();
//		 } else {
//		 final AccessToken storedToken = new AccessToken(PROPERTIES.getProperty(PROPERTY_TOKEN),
//		 PROPERTIES.getProperty(PROPERTY_TOKEN_SECRET));
//		 // build a new feature from the stored consumer credentials and access token
//		 filterFeature = OAuth1ClientSupport.builder(consumerCredentials).feature()
//		 .accessToken(storedToken).build();
//		 }
//		 // create a new Jersey client and register filter feature that will add OAuth signatures and
//		 // JacksonFeature that will process returned JSON data.
//		 final Client client = ClientBuilder.newBuilder()
//		 .register(filterFeature)
//		 .register(JacksonFeature.class)
//		 .build();
//		 // make requests to protected resources
//		 // (no need to care about the OAuth signatures)
//		 final Response response = client.target(FRIENDS_TIMELINE_URI).request().get();
//		 if (response.getStatus() != 200) {
//		 String errorEntity = null;
//		 if (response.hasEntity()) {
//		 errorEntity = response.readEntity(String.class);
//		 }
//		 throw new RuntimeException("Request to Twitter was not successful. Response code: "
//		 + response.getStatus() + ", reason: " + response.getStatusInfo().getReasonPhrase()
//		 + ", entity: " + errorEntity);
//		 }
//		 // persist the current consumer key/secret and token/secret for future use
//		 storeSettings();
//		 final List<Status> statuses = response.readEntity(new GenericType<List<Status>>() {
//		 });
//		 System.out.println("Tweets:\n");
//		 for (final Status s : statuses) {
//		 System.out.println(s.getText());
//		 System.out.println("[posted by " + s.getUser().getName() + " at " + s.getCreatedAt() + "]");
//		 }
//		 }
//		 private static void loadSettings() {
//		 FileInputStream st = null;
//		 try {
//		 st = new FileInputStream(PROPERTIES_FILE_NAME);
//		 PROPERTIES.load(st);
//		 } catch (final IOException e) {
//		 // ignore
//		 } finally {
//		 if (st != null) {
//		 try {
//		 st.close();
//		 } catch (final IOException ex) {
//		 // ignore
//		 }
//		 }
//		 }
//		 for (final String name : new String[]{PROPERTY_CONSUMER_KEY, PROPERTY_CONSUMER_SECRET,
//		 PROPERTY_TOKEN, PROPERTY_TOKEN_SECRET}) {
//		 final String value = System.getProperty(name);
//		 if (value != null) {
//		 PROPERTIES.setProperty(name, value);
//		 }
//		 }
//		 if (PROPERTIES.getProperty(PROPERTY_CONSUMER_KEY) == null
//		 || PROPERTIES.getProperty(PROPERTY_CONSUMER_SECRET) == null) {
//		 System.out.println("No consumerKey and/or consumerSecret found in twitterclient.properties file. "
//		 + "You have to provide these as system properties. See README.html for more information.");
//		 System.exit(1);
//		 }
//		 }
//		 private static void storeSettings() {
//		 FileOutputStream st = null;
//		 try {
//		 st = new FileOutputStream(PROPERTIES_FILE_NAME);
//		 PROPERTIES.store(st, null);
//		 } catch (final IOException e) {
//		 // ignore
//		 } finally {
//		 try {
//		 if (st != null) {
//		 st.close();
//		 }
//		 } catch (final IOException ex) {
//		 // ignore
//		 }
//		 }
//		 }
	}
}
