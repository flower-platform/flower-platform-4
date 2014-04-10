package org.flowerplatform.js_client.server;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

/**
 * Set custom properties to {@link ObjectMapper}.
 * 
 * @author Mariana Gheorghe
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private ObjectMapper objectMapper;
	
	public ObjectMapperProvider() {
		objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.INDENT_OUTPUT, true);
	}
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
