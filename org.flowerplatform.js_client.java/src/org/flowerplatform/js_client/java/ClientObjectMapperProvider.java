package org.flowerplatform.js_client.java;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.SubscriptionInfo;
import org.flowerplatform.js_client.java.jersey_mapping.ClientConverter;
import org.flowerplatform.js_client.java.jersey_mapping.ClientNodeInstantiator;
import org.flowerplatform.js_client.java.jersey_mapping.NodeDeserializer;
import org.flowerplatform.js_client.java.jersey_mapping.TypeResolverBuilder;
import org.flowerplatform.js_client.java.jersey_mapping.UntypedObjectDeserializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * @see JsServiceInvocator
 * @author Cristina Constantinescu
 */
@Provider
public class ClientObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private ObjectMapper objectMapper;
	
	public ClientObjectMapperProvider() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JaxbAnnotationModule());
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
//		objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
//
//			private static final long serialVersionUID = 331692366476982678L;
//
//			@Override
//			public Object findDeserializationConverter(Annotated a) {
////				if (a.getRawType() == SubscriptionInfo.class) {
////					return new ClientConverter<SubscriptionInfo, ClientSubscriptionInfo>();
////				}
//				if (a.getRawType() == Node.class) {
//					return new ClientConverter<Node, ClientNode>();
//				}
//				return super.findDeserializationConverter(a);
//			}
//			
//		});
//		SimpleModule testModule = new SimpleModule();			
//		testModule.addDeserializer(Node.class, new NodeDeserializer());	
//		testModule.addDeserializer(Object.class, new UntypedObjectDeserializer());		
//		testModule.addValueInstantiator(Node.class, new ClientNodeInstantiator());	
//		testModule.addValueInstantiator(SubscriptionInfo.class, new ValueInstantiator() {
//			
//			@Override
//			public String getValueTypeDesc() {		
//				return ClientSubscriptionInfo.class.getName();
//			}
//			
//			@Override // yes, this creation method is available
//		    public boolean canCreateUsingDefault() { return true; }
//
//			@Override
//			public Object createUsingDefault(DeserializationContext ctxt)
//					throws IOException, JsonProcessingException {		
//				return new ClientSubscriptionInfo();
//			}			
//		});	
		
//		testModule.setDeserializerModifier(new BeanDeserializerModifier() {
//
//			@Override
//			public List<BeanPropertyDefinition> updateProperties(
//					DeserializationConfig config, BeanDescription beanDesc,
//					List<BeanPropertyDefinition> propDefs) {				
//				if (beanDesc.getBeanClass() == Node.class) {
//					return config.introspect(config.introspectClassAnnotations(ClientNode.class).getType()).findProperties();
//				}
//				if (beanDesc.getBeanClass() == SubscriptionInfo.class) {
//					return config.introspect(config.introspectClassAnnotations(ClientSubscriptionInfo.class).getType()).findProperties();
//				}
//				return super.updateProperties(config, beanDesc, propDefs);
//			}
//			
//			
//		});	
//		objectMapper.registerModule(testModule);
		objectMapper.enableDefaultTyping(DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY);
//		com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder<?> typer = new TypeResolverBuilder(DefaultTyping.NON_FINAL);
//	    typer = typer.init(JsonTypeInfo.Id.CLASS, null);
//	    typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
//	    objectMapper.setDefaultTyping(typer);
	}
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
