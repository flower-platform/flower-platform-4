package org.flowerplatform.js_client.java.jersey_mapping;

import java.io.IOException;

import org.flowerplatform.js_client.java.ClientNode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class ClientNodeInstantiator extends ValueInstantiator {

	@Override
	public String getValueTypeDesc() {		
		return ClientNode.class.getName();
	}
	
	@Override // yes, this creation method is available
    public boolean canCreateUsingDefault() { return true; }

	@Override
	public Object createUsingDefault(DeserializationContext ctxt) throws IOException, JsonProcessingException {		
		return new ClientNode();
	}

	
}
