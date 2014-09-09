package org.flowerplatform.js_client.java;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

public class CustomMessageBodyWriter extends DefaultTypeResolverBuilder {

	public CustomMessageBodyWriter(DefaultTyping t) {
		super(t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean useForType(JavaType t) {
		if (_appliesFor == DefaultTyping.NON_FINAL) {
            return !t.isArrayType();
		}
		return super.useForType(t);
	}


}
