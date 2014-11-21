package org.flowerplatform.util.diff_update;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdate {

	private long id;

	private String type;
	
	private String entityUid; 
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEntityUid() {
		return entityUid;
	}

	public void setEntityUid(String entityUid) {
		this.entityUid = entityUid;
	}
	
}
