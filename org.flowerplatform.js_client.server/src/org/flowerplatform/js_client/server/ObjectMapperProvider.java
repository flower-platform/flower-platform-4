package org.flowerplatform.js_client.server;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.flowerplatform.js_client.server.jackson.TypeResolverBuilder;
import org.flowerplatform.js_client.server.jackson.UntypedObjectDeserializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

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
		objectMapper.registerModule(new JaxbAnnotationModule());
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		SimpleModule testModule = new SimpleModule().addDeserializer(Object.class, new UntypedObjectDeserializer());
		objectMapper.registerModule(testModule);
		
		com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder<?> typer = new TypeResolverBuilder(DefaultTyping.NON_FINAL);
		typer = typer.init(JsonTypeInfo.Id.CLASS, null);
		typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
		objectMapper.setDefaultTyping(typer);
	}
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
