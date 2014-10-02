package org.flowerplatform.js_client.server.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

/**
 * Created to override the default serialization process when using type property in json format.
 * It doesn't add type property for primitives or arrays.
 *
 * <p>
 * Note: A similar class exists on client side too.
 * 
 * @author Cristina Constantinescu
 */
public class AsPropertyTypeSerializer extends com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer {

	public AsPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName) {
		super(idRes, property, propName);		
	}

	@Override
	public void writeTypePrefixForArray(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
		 jgen.writeStartArray();
	}

	@Override
	public void writeTypePrefixForArray(Object value, JsonGenerator jgen, Class<?> type) throws IOException, JsonProcessingException {
		 jgen.writeStartArray();
	}

	@Override
	public void writeTypeSuffixForArray(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
		 jgen.writeEndArray();
	}

	@Override
	public void writeCustomTypePrefixForArray(Object value, JsonGenerator jgen, String typeId) throws IOException, JsonProcessingException {
		jgen.writeStartArray();
	}

	@Override
	public void writeCustomTypeSuffixForArray(Object value, JsonGenerator jgen,	String typeId) throws IOException, JsonProcessingException {		
	}

	@Override
	public void writeTypePrefixForScalar(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {		
	}

	@Override
	public void writeTypePrefixForScalar(Object value, JsonGenerator jgen, Class<?> type) throws IOException, JsonProcessingException {		
	}

	@Override
	public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {		
	}

	@Override
	public void writeCustomTypePrefixForScalar(Object value, JsonGenerator jgen, String typeId) throws IOException, JsonProcessingException {		
	}

	@Override
	public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator jgen, String typeId) throws IOException,	JsonProcessingException {		
	}
	
}
