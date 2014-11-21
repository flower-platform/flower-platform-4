package org.flowerplatform.util.diff_update;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class PropertiesDiffUpdate extends DiffUpdate {

	private Map<String, Object> properties = new HashMap<>();

	public Map<String, Object> getProperties() {
		return properties;
	}

	public PropertiesDiffUpdate() {
		setType("UPDATED");
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}
	
}
