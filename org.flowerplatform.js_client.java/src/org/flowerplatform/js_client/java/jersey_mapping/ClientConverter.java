package org.flowerplatform.js_client.java.jersey_mapping;

import java.util.HashMap;

import com.fasterxml.jackson.databind.util.StdConverter;

public class ClientConverter extends StdConverter<Object, HashMap<String, Object>> {

	@Override
	public HashMap<String, Object> convert(Object value) {
		return null;
	}

}
