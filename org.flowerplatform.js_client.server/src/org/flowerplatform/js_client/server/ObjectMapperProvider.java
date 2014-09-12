package org.flowerplatform.js_client.server;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.flowerplatform.js_client.server.jersey_mapper.TypeResolverBuilder;
import org.flowerplatform.js_client.server.jersey_mapper.UntypedObjectDeserializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
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
		
//		SimpleModule testModule = new SimpleModule("MyModule", new Version(1, 0, 0, null, null, null))
//			.addDeserializer(Object.class, new UntypedObjectDeserializer());
//		objectMapper.registerModule(testModule);
//		
//		com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder<?> typer = new TypeResolverBuilder(DefaultTyping.NON_FINAL);
//	    typer = typer.init(JsonTypeInfo.Id.CLASS, null);
//	    typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
//	    objectMapper.setDefaultTyping(typer);
//		 
		objectMapper.enableDefaultTyping(DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY);
//		objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
//
//			@Override
//			public Boolean isTypeId(AnnotatedMember member) {
//				// TODO Auto-generated method stub
//				return super.isTypeId(member);
//			}
//
//			@Override
//			public ObjectIdInfo findObjectIdInfo(Annotated ann) {
//				// TODO Auto-generated method stub
//				return super.findObjectIdInfo(ann);
//			}
//
//			@Override
//			public String findTypeName(AnnotatedClass ac) {
//				// TODO Auto-generated method stub
//				return super.findTypeName(ac);
//			}
//			
//		});
	}
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
