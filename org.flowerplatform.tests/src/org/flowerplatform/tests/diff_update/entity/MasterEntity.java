package org.flowerplatform.tests.diff_update.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class MasterEntity extends AbstractEntity {
	
	private String name;

	private List<DetailEntity> details = new ArrayList<>();
	
	public MasterEntity(int id) {
		super(id);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DetailEntity> getDetails() {
		return details;
	}

	public void setDetails(List<DetailEntity> children) {
		this.details = children;
	}

	public boolean isRoot() {
		return true;
	}
	
	/**
	 * 
	 * @author see class
	 *
	 */
	public String toString() {
		return String.format("MasterEntity@%s[id:%s name:%s details:%s]", System.identityHashCode(this), super.getId(), name, details);
	}
	
}
