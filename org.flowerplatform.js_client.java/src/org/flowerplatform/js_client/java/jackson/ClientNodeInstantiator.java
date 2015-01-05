/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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
