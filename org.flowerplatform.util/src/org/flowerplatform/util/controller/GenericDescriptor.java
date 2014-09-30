package org.flowerplatform.util.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cristian Spiescu
 */
public class GenericDescriptor extends AbstractController implements IDescriptor {
	
	private Object value;
	
	private Map<String, Object> extraInfo;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Map<String, Object> getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Map<String, Object> extraInfo) {
		this.extraInfo = extraInfo;
	}

	public GenericDescriptor(Object value) {
		super();
		this.value = value;
	}
	
	/**
	 * 
	 */
	public GenericDescriptor addExtraInfoProperty(String key, String val) {
		if (extraInfo == null) {
			extraInfo = new HashMap<String, Object>();
		}
		extraInfo.put(key, val);
		return this;
	}
	
}
