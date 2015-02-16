package org.flowerplatform.tests.diff_update.entity;

import java.io.Serializable;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class AbstractEntity implements Serializable {

	private static final long serialVersionUID = -4157157986919984554L;

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
