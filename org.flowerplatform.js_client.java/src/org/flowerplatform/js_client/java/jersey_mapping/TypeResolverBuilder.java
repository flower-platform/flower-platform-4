package org.flowerplatform.js_client.java.jersey_mapping;

import java.util.Collection;

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
	
	@Override
	public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {		
		if (useForType(baseType)) {
			if (_idType == JsonTypeInfo.Id.NONE) {
				return null;
			}	
			TypeIdResolver idRes = idResolver(config, baseType, subtypes, false, true);
			
			// First, method for converting type info to type id:
			switch (_includeAs) {
				case WRAPPER_ARRAY:
					return new AsArrayTypeDeserializer(baseType, idRes, _typeProperty, _typeIdVisible, _defaultImpl);
				case PROPERTY:
					return new AsPropertyTypeDeserializer(baseType, idRes, _typeProperty, _typeIdVisible, _defaultImpl);
				case WRAPPER_OBJECT:
					return new AsWrapperTypeDeserializer(baseType, idRes, _typeProperty, _typeIdVisible, _defaultImpl);
				case EXTERNAL_PROPERTY:
					return new AsExternalTypeDeserializer(baseType, idRes, _typeProperty, _typeIdVisible, _defaultImpl);
			default:
				break;
			}
		}
		return null;
	}

}
