package org.flowerplatform.js_client.java.jackson;

import java.io.IOException;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.flowerplatform.js_client.java.provider.JsClientJavaObjectMapperProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

/**
 * Used to instantiate a {@link ClientNode} when trying to deserialize a {@link Node}.
 * @see JsClientJavaObjectMapperProvider
 * 
 * @author Cristina Constantinescu
 */
public class ClientNodeInstantiator extends ValueInstantiator {

	@Override
	public String getValueTypeDesc() {		
		return ClientNode.class.getName();
	}
	
	@Override
    public boolean canCreateUsingDefault() { 
		return true; 
	}

	@Override
	public Object createUsingDefault(DeserializationContext ctxt) throws IOException, JsonProcessingException {		
		return new ClientNode();
	}
	
}
