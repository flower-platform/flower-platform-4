package org.flowerplatform.core.node.update.remote;

/**
 * @author Cristina Constantinescu
 */
public class PropertyUpdate extends Update {

	private String key;
	
	private Object value;

	/**
	 * @author Claudiu Matei
	 */
	private Object oldValue;

	/**
	 * @author Claudiu Matei
	 */
	private boolean hasOldValue;
	
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

	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * No standard setter was created, in order to avoid serialization of this field 
	 * 
	 * @author Claudiu Matei
	 */
	public PropertyUpdate setOldValueAs(Object oldValue) {
		this.oldValue = oldValue;
		return this;
	}

	public boolean getHasOldValue() {
		return hasOldValue;
	}
	
	/**
	 * No standard setter was created, in order to avoid serialization of this field 
	 * 
	 * @author Claudiu Matei
	 */
	public PropertyUpdate setHasOldValueAs(boolean hasOldValue) {
		this.hasOldValue = hasOldValue;
		return this;
	}
	
	public boolean getIsUnset() {
		return isUnset;
	}

	public void setIsUnset(boolean isUnset) {
		this.isUnset = isUnset;
	}
		
	public PropertyUpdate setUnsetAs(boolean isUnset) {
		this.isUnset = isUnset;
		return this;
	}
	
	@Override
	public String toString() {
		return "PropertyUpdate [key=" + key + " value=" + value + " isUnset=" + isUnset + " node=" + getFullNodeId() + ", timestamp=" + getTimestamp() + "]";
	}
	
}
