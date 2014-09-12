package org.flowerplatform.js_client.java.jersey_mapping;

import java.io.IOException;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.js_client.java.ClientNode;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NodeDeserializer extends JsonDeserializer<Node> {

	@Override
	public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
			jp.nextToken();
		};
	    if (jp.getText().equals("@class")) {
	    	jp.nextToken();
	    }
	    
	    return new ClientNode();
	}

	@Override
	public Node deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
			jp.nextToken();
		};
	    if (jp.getText().equals("@class")) {
	    	jp.nextToken();
	    }
	    
	    return new Node();
	}
	
}