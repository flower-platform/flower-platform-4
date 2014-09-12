package org.flowerplatform.js_client.java.jersey_mapping;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class UntypedObjectDeserializer extends com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer {

	private static final long serialVersionUID = -1541902156453338644L;

	@Override
	public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
		 switch (jp.getCurrentToken()) {
	        case FIELD_NAME:
	        case START_OBJECT:	            
	            return super.deserializeWithType(jp, ctxt, typeDeserializer);	       
	        default:
	        	return super.deserialize(jp, ctxt);	
		 }
	}

	@Override
	public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return super.deserialize(jp, ctxt);
	}
	
	
	
}
