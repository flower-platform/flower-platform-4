package org.flowerplatform.util.diff_update;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PropertiesDiffUpdate)) {
			return false;
		}
		PropertiesDiffUpdate otherUpdate = (PropertiesDiffUpdate) obj;
		return Objects.equals(getType(), otherUpdate.getType()) && Objects.equals(getEntityUid(), otherUpdate.getEntityUid())
				&& Objects.equals(getProperties(), otherUpdate.getProperties());
	}
	
	@Override
	public int hashCode() {
		return getType().hashCode() + 3 * getEntityUid().hashCode() + 7 * getProperties().hashCode(); 
	}
	
}
