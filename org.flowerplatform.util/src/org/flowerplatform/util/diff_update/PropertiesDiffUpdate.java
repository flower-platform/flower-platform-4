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
		setType("updated");
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	/**
	 * 
	 * @author see class
	 *
	 */
	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	@Override
	public String toString() {
		return super.toString() + "[properties=" + properties + "]";
	}
	
}
