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
package org.flowerplatform.js_client.server;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.flowerplatform.js_client.server.jackson.TypeResolverBuilder;
import org.flowerplatform.js_client.server.jackson.UntypedObjectDeserializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * Set custom properties to {@link ObjectMapper}.
 * 
 * @author Mariana Gheorghe
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private ObjectMapper objectMapper;
	
	public ObjectMapperProvider() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JaxbAnnotationModule());		
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		SimpleModule testModule = new SimpleModule().addDeserializer(Object.class, new UntypedObjectDeserializer());
		objectMapper.registerModule(testModule);
		
		com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder<?> typer = new TypeResolverBuilder(DefaultTyping.NON_FINAL);
	    typer = typer.init(JsonTypeInfo.Id.CLASS, null);
	    typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
	    objectMapper.setDefaultTyping(typer);
	}
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
