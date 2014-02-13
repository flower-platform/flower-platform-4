package org.flowerplatform.core.node.update.remote;

/**
 * @author Cristina Constantinescu
 */
public class PropertyUpdate extends Update {

	private String key;
	
	private Object value;
	
	private boolean isUnset;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public PropertyUpdate setKeyAs(String key) {
		this.key = key;
		return this;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public PropertyUpdate setValueAs(Object value) {
		this.value = value;
		return this;
	}
	
	public boolean getIsUnset() {
		return isUnset;
	}

	public void setUnset(boolean isUnset) {
		this.isUnset = isUnset;
	}
		
	public PropertyUpdate setUnsetAs(boolean isUnset) {
		this.isUnset = isUnset;
		return this;
	}
	
	@Override
	public String toString() {
		return "PropertyUpdate [key=" + key + " value=" + value + " isUnset=" + isUnset + " node=" + getNode() + ", timestamp=" + getTimestamp() + "]";
	}
	
}
