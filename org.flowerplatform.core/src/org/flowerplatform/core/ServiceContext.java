package org.flowerplatform.core;

import java.util.HashMap;

/**
 * @author Cristina Constantinescu
 */
public class ServiceContext extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public boolean getValue(String key) {
		return containsKey(key) && (boolean) get(key);
	}

	public ServiceContext add(String key, Object value) {
		put(key, value);
		return this;
	}	
	
}
