package org.flowerplatform.tests.diff_update.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DetailEntity extends AbstractEntity {
	
	private List<Object> subdetails = new ArrayList<>();

	private int value;

	private MasterEntity masterEntity;
	
	public DetailEntity(int id) {
		super(id);
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<Object> getSubdetails() {
		return subdetails;
	}

	public void setSubdetails(List<Object> subdetails) {
		this.subdetails = subdetails;
	}

	public MasterEntity getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(MasterEntity masterEntity) {
		this.masterEntity = masterEntity;
	}

	/**
	 * @author see class
	 */
	public String toString() {
		return String.format("%s@%s[id:%s value:%s masterEntity:%s@%s subdetails:%s ]", 
				getClass().getSimpleName(), System.identityHashCode(this), getId(), value, 
				(masterEntity != null ? masterEntity.getId() : "null"), System.identityHashCode(masterEntity), subdetails);
	}
	
}
