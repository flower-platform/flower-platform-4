package org.flowerplatform.util.diff_update;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class PropertiesDiffUpdate extends DiffUpdate {

	private static final long serialVersionUID = -3256240093556452543L;

	private Map<String, Object> properties = new HashMap<String, Object>();

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
