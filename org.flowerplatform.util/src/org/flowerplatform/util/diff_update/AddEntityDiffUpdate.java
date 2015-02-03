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
package org.flowerplatform.util.diff_update;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class AddEntityDiffUpdate extends PropertiesDiffUpdate {
	
	private String parentUid;

	private String parentChildrenProperty;
	
	private int index = -1;

	private Object entity;
	
	public AddEntityDiffUpdate() {
		setType("added");
	}
	
	public String getParentUid() {
		return parentUid;
	}

	public void setParentUid(String parentUid) {
		this.parentUid = parentUid;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	/**
	 * @author Claudiu Matei
	 */
	public void getPropertyValue(String property) {
		
	}
	
	/**
	 * @author Claudiu Matei
	 */
	public Map<String, Object> getProperties() {
		// TODO CS/DU: sa facem asta cumva la constructie; caci astfel, la toString se va apela din nou; dar de fapt: daca trimitem tot obiectul, de ce mai e nevoie de asta?
		Map<String, Object> properties = new HashMap<>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
	        	// TODO CS/DU: ce e cu cazurile "details" si "subdetails"?
	        	if (propertyDescriptor.getWriteMethod() != null && !propertyDescriptor.getName().equals("details") && !propertyDescriptor.getName().equals("subdetails")) {
	        		properties.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(entity));
	        	}
	        }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return properties;
	}

	public String getParentChildrenProperty() {
		return parentChildrenProperty;
	}

	public void setParentChildrenProperty(String parentChildrenProperty) {
		this.parentChildrenProperty = parentChildrenProperty;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return super.toString() + "[parentUid=" + parentUid + ", parentChildrenProperty=" + parentChildrenProperty + ", index=" + index + "]";
	}
	
}
