package org.flowerplatform.js_client.server.jackson;

import java.io.IOException;

import org.flowerplatform.js_client.server.ObjectMapperProvider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * When serializing, in order to be compatible with a javascript client,
 * the type property is not added for primitives or arrays.
 * 
 * So, in these cases, a normal (not typed) deserialization is needed.
 * 
 * <p>
 * @see ObjectMapperProvider
 * 
 * @author Cristina Constantinescu
 */
public class UntypedObjectDeserializer extends com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer {

	private static final long serialVersionUID = -1541902156453338644L;
	

	@Override
	public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
		_listDeserializer = null;
		switch (jp.getCurrentToken()) {
	        case FIELD_NAME:
	        case START_OBJECT:	            
	            return super.deserializeWithType(jp, ctxt, typeDeserializer);	       
	        default:
	        	return super.deserialize(jp, ctxt);	
		 }
	}
	
}
