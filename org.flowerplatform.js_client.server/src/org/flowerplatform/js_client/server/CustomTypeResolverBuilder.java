package org.flowerplatform.js_client.server;

import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;

public class CustomTypeResolverBuilder extends DefaultTypeResolverBuilder {

	public CustomTypeResolverBuilder(DefaultTyping arg0) {
		super(arg0);		
	}

	private static final long serialVersionUID = 1L;

      @Override
      public TypeSerializer buildTypeSerializer(SerializationConfig config,
              JavaType baseType, Collection<NamedType> subtypes) {
          return !(baseType instanceof SimpleType) ? super.buildTypeSerializer(config, baseType, subtypes) : null;            
      }
    
}
