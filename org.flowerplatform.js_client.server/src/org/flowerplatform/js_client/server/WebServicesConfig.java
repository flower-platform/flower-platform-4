package org.flowerplatform.js_client.server;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.groups.Default;
import javax.ws.rs.core.MultivaluedHashMap;

import org.flowerplatform.core.CorePlugin;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.oauth1.DefaultOAuth1Provider;
import org.glassfish.jersey.server.oauth1.OAuth1Consumer;
import org.glassfish.jersey.server.oauth1.OAuth1Provider;
import org.glassfish.jersey.server.oauth1.OAuth1ServerFeature;
import org.glassfish.jersey.server.oauth1.OAuth1Token;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Exposes registered services as REST API. Passed as an initialization
 * parameter for the {@link ServletContainer} registered via extension point.
 * 
 * @author Mariana Gheorghe
 */
public class WebServicesConfig extends ResourceConfig {

	public WebServicesConfig() throws ClassNotFoundException {
		for (Object service : CorePlugin.getInstance().getServiceRegistry().getMap().values()) {
			register(service);
		}

//		register(new RemoteMethodInvocationFilter());
//		register(new RemoteMethodInvocationWriterInterceptor());
		
		register(JacksonFeature.class);
		register(new ObjectMapperProvider());
		
		register(LoggingFilter.class);
		
		DefaultOAuth1Provider p = new DefaultOAuth1Provider();
		p.registerConsumer("test", "714528398641253", "4ce1637f1617d716a8347eae3524c8b3", new MultivaluedHashMap<String, String>());
		p.addAccessToken("ceva", "4ce1637f1617d716a8347eae3524c8b3", "714528398641253", 
				"", null, new HashSet<String>(), new MultivaluedHashMap<String, String>());
		//p.newAccessToken(requestToken, verifier)
		register(new OAuth1ServerFeature(p, "authorize1", "authorize2"));
	}
	
}
