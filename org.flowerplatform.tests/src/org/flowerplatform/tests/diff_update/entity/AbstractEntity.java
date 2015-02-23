package org.flowerplatform.tests.diff_update.entity;


/**
 * 
 * @author Claudiu Matei
 *
 */
public class AbstractEntity {

	private int id;

	public AbstractEntity(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isRoot() {
		return false;
	}
	
}
