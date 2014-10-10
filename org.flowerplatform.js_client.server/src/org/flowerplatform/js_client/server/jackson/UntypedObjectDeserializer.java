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
package org.flowerplatform.js_client.server.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
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
	public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
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
