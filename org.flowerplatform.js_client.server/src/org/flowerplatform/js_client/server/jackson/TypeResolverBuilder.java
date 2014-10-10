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

import java.util.Collection;

import org.flowerplatform.js_client.server.ObjectMapperProvider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer;

/**
 * Created to provide our {@link AsPropertyTypeSerializer}.
 * 
 * @see ObjectMapperProvider
 * @see UntypedObjectDeserializer
 * 
 * @author Cristina Constantinescu
 */
public class TypeResolverBuilder extends DefaultTypeResolverBuilder {

	private static final long serialVersionUID = 1018958085327198395L;
	
	public TypeResolverBuilder(DefaultTyping t) {
		super(t);	
	}

	@Override
	public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
		if (useForType(baseType)) {
			if (_idType == JsonTypeInfo.Id.NONE) {
				return null;
			}
			TypeIdResolver idRes = idResolver(config, baseType, subtypes, true, false);
			switch (_includeAs) {
				case WRAPPER_ARRAY:
					return new AsArrayTypeSerializer(idRes, null);
				case PROPERTY:
					return new AsPropertyTypeSerializer(idRes, null, _typeProperty);
				case WRAPPER_OBJECT:
					return new AsWrapperTypeSerializer(idRes, null);
				case EXTERNAL_PROPERTY:
					return new AsExternalTypeSerializer(idRes, null, _typeProperty);
				default:
					break;
			}
		}
		return null;
	}
	
}
