/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.tests.diff_update;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class SubdetailEntity extends AbstractEntity {

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
