package org.flowerplatform.util.diff_update;

import java.io.Serializable;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdate implements Serializable {

	private static final long serialVersionUID = -7517448831349466230L;

	private long id;

	private long timestamp = System.currentTimeMillis();
	
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

	@Override
	public String toString() {
		return "DiffUpdate[type=" + type + ", id=" + id + ", entityUid="
				+ entityUid + "]";
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
}
