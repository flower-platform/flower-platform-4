package org.flowerplatform.js_client.java.jackson;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Created to override the default deserialization process when using type property in json format.
 * 
 * In case of an array, don't try to get it from json format because it doesn't exist there; use {@link ArrayList} as type instead.
 * 
 * @see TypeResolverBuilder
 * 
 * @author Cristina Constantinescu
 */
public class AsPropertyTypeDeserializer extends com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer {

	private static final long serialVersionUID = -6909249978026613573L;
	
	public AsPropertyTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
		super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
	}

	@Override
	public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {		
		return _deserialize(jp, ctxt);
	}

	@Override
    public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {     
        if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
            return deserializeTypedFromArray(jp, ctxt);
        }
        return deserializeTypedFromObject(jp, ctxt);
    }    
	
    private final Object _deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // 02-Aug-2013, tatu: May need to use native type ids
        if (jp.canReadTypeId()) {
            return _deserializeWithNativeTypeId(jp, ctxt);
        }
       
        String typeId = null;
        if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
        	typeId = ArrayList.class.getName();
        } else {
        	typeId = _locateTypeId(jp, ctxt);
        }
                		
        JsonDeserializer<Object> deser = _findDeserializer(ctxt, typeId);
        // Minor complication: we may need to merge type id in?
        if (_typeIdVisible && jp.getCurrentToken() == JsonToken.START_OBJECT) {
            // but what if there's nowhere to add it in? Error? Or skip? For now, skip.
            TokenBuffer tb = new TokenBuffer(null, false);
            tb.writeStartObject(); // recreate START_OBJECT
            tb.writeFieldName(_typePropertyName);
            tb.writeString(typeId);
            jp = JsonParserSequence.createFlattened(tb.asParser(jp), jp);
            tb.close();
            jp.nextToken();
        }
        return deser.deserialize(jp, ctxt);
    }    
 	
}
