package org.flowerplatform.tests.diff_update;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DetailEntity extends AbstractEntity {

	private long id;
	
	private String parentUid;
	
	private String parentChildrenProperty;
	
	private int value;

	public String getParentUid() {
		return parentUid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setParentUid(String parentUid) {
		this.parentUid = parentUid;
	}

	public String getParentChildrenProperty() {
		return parentChildrenProperty;
	}

	public void setParentChildrenProperty(String parentChildrenProperty) {
		this.parentChildrenProperty = parentChildrenProperty;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * 
	 */
	public String toString() {
		return String.format("%s[id:%s value:%s parentUid:%s parentChildrenProperty:%s]", getClass().getSimpleName(), getId(), value, parentUid, parentChildrenProperty, value);
	}
	
}
