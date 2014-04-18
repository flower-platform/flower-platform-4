package org.flowerplatform.core.node.remote;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cristina Constantinescu
 */
public class ServiceContext<T> {

	private T service;
	
	private Map<String, Object> context;
	
	public T getService() {
		return service;
	}
	
	public void setService(T service) {
		this.service = service;
	}

	public Map<String, Object> getContext() {
		if (context == null) {
			context = new HashMap<>();
		}
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
	public ServiceContext() {	
	}
	
	public ServiceContext(T service) {
		this.service = service;
	}
	
	public boolean getBooleanValue(String key) {
		return getContext().containsKey(key) && (boolean) getContext().get(key);
	}

	public Object get(String key) {
		return getContext().get(key);
	}
		
	public ServiceContext<T> add(String key, Object value) {		
		getContext().put(key, value);
		return this;
	}

}
