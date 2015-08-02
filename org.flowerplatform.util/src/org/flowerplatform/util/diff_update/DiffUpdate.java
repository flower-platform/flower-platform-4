package org.flowerplatform.util.diff_update;

import java.io.Serializable;
import java.util.Objects;

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
	
	/**
	 * Non-conventional getter/setter so that it's not serialized to client
	 */
	private Object entity;
	
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

	/**
	 * @see #entity
	 */
	public Object entity() {
		return entity;
	}

	/**
	 * @see #entity
	 */
	public void entity(Object entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "DiffUpdate[type=" + type + ", id=" + id + ", entityUid="
				+ entityUid + "]";
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public boolean equals(Object obj) {
		DiffUpdate otherUpdate = (DiffUpdate) obj;
		return Objects.equals(type, otherUpdate.type) && Objects.equals(entityUid, otherUpdate.entityUid);
	}
	
	@Override
	public int hashCode() {
		return type.hashCode() + 3 * entityUid.hashCode(); 
	}
	
}
