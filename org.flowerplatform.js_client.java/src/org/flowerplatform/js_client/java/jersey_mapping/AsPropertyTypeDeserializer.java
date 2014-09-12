package org.flowerplatform.js_client.java.jersey_mapping;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class AsPropertyTypeDeserializer extends com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer {

	public AsPropertyTypeDeserializer(
			com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer src,
			BeanProperty property) {
		super(src, property);
		// TODO Auto-generated constructor stub
	}


	public AsPropertyTypeDeserializer(JavaType bt, TypeIdResolver idRes,
			String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
		super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);		
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -6909249978026613573L;

	@Override
	public Object deserializeTypedFromArray(JsonParser jp,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		return super.deserializeTypedFromArray(jp, ctxt);
	}

	

	
}
