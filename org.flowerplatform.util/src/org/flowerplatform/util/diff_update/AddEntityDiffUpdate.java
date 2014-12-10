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
		Map<String, Object> properties = new HashMap<>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
	        	if (propertyDescriptor.getWriteMethod() != null && !propertyDescriptor.getName().equals("details") && !propertyDescriptor.getName().equals("subdetails")) {
	        		properties.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(entity));
	        	}
	        }
		} catch (Exception e) {
			//TODO CM: ce ar trebui sa facem?
			e.printStackTrace();
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
	
}
