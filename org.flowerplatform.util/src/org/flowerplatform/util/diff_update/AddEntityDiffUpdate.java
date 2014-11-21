package org.flowerplatform.util.diff_update;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class AddEntityDiffUpdate extends PropertiesDiffUpdate {
	
	private String entityType;
	
	private String parentUid;

	public AddEntityDiffUpdate() {
		setType("ADDED");
	}
	
	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getParentUid() {
		return parentUid;
	}

	public void setParentUid(String parentUid) {
		this.parentUid = parentUid;
	}
	
}
