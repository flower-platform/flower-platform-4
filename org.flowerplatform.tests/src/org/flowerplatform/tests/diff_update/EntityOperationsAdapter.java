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
//CHECKSTYLE:OFF
package org.flowerplatform.tests.diff_update;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Claudiu Matei
 */
public class EntityOperationsAdapter {
	
	/**
	 * @author See class.
	 */
	public String getEntityUid(Object entity) {
		String uid = entity.getClass().getSimpleName() + ":" + ((AbstractEntity) entity).getId();
		return uid;
	}
	
	/**
	 * @author See class.
	 */
	public void setChildren(Object entity, String childrenProperty, List<Object> children) {
		if (entity instanceof MasterEntity) {
			((MasterEntity) entity).setDetails(children);
		} else if (entity instanceof DetailEntity) {
			((DetailEntity) entity).setSubdetails(children);
		}

		
	}
	
	/**
	 * @author See class.
	 */
	public void setParent(Object entity, String parentUid, String parentChildrenProperty) {
		if (entity instanceof DetailEntity) {
			DetailEntity detailEntity = (DetailEntity) entity;
			detailEntity.setParentUid(parentUid);
			detailEntity.setParentChildrenProperty(parentChildrenProperty);
		} else if (entity instanceof SubdetailEntity) {
			SubdetailEntity subdetailEntity = (SubdetailEntity) entity;
			subdetailEntity.setParentUid(parentUid);
			subdetailEntity.setParentChildrenProperty(parentChildrenProperty);
		}
	}

	/**
	 * @author See class.
	 */
	public String[] getChildrenProperties(Object entity) {
		if (entity instanceof MasterEntity) {
			return new String[] { "details" };
		} else if (entity instanceof DetailEntity) {
			return new String[] { "subdetails" };
		}
		return null;
	}
	
	/**
	 * @author See class.
	 */
	public Object createEntity(String entityType, Map<String, Object> properties) throws InstantiationException, IllegalAccessException, 
			ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Object entity = Class.forName(entityType).newInstance();
		for (Entry<String, Object> entry : properties.entrySet()) {
			new PropertyDescriptor(entry.getKey(), entity.getClass()).getWriteMethod().invoke(entity, entry.getValue());
		}
		return entity;
	}
	
	/**
	 * @author See class.
	 */
	public List<?>[] getChildrenLists(Object entity) {
		List<?>[] lists;
		if (entity instanceof MasterEntity) {
			lists = new List<?>[1];
			lists[0] = ((MasterEntity) entity).getDetails();
		}else if (entity instanceof DetailEntity) {
			lists = new List<?>[1];
			lists[0] = ((DetailEntity) entity).getSubdetails();
		}

		return new List<?>[0];
	}

	/**
	 * @author See class.
	 */
	public List<?> getChildrenList(Object entity, String property) {
		if (entity instanceof MasterEntity) {
			return ((MasterEntity) entity).getDetails();
		} else if (entity instanceof DetailEntity) {
			return ((DetailEntity) entity).getSubdetails();
		}
		return null;
	}

	
	/**
	 * @author See class.
	 */
	public void list_addItem(List<Object> list, Object element, int index) {
		if (index == -1) {
			list.add(element);
		} else {
			list.add(index, element);
		}
	}

	/**
	 * @author See class.
	 */
	public void list_setItemAt(List<Object> list, Object element, int index) {
		list.set(index, element);
	}

	/**
	 * @author See class.
	 */
	public Object list_removeItemAt(List<Object> list, int index) {
		return list.remove(index);
	}
	
	/**
	 * @author See class.
	 */
	public void list_removeItem(List<Object> list, Object element) {
		list.remove(element);
	}

	/**
	 * @author See class.
	 */
	public int list_getLength(List<Object> list) {
		return list.size();
	}

	/**
	 * @author See class.
	 */
	public Object list_getItemAt(List<Object> list, int index) {
		return list.get(index);
	}

	
	public Object object_getPropertiesHolder(Object entity) {
		return entity;
	}

	public boolean object_hasDynamicProperties(Object entity) {
		return false;
	}
	
	public void object_iterateProperties(Object propertiesHolder, IteratePropertiesCallback ipc) throws ReflectiveOperationException, IllegalArgumentException, IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(propertiesHolder.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        	if (propertyDescriptor.getWriteMethod() != null && !propertyDescriptor.getName().equals("details") && !propertyDescriptor.getName().equals("subdetails")) {
            	ipc.callback(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(propertiesHolder));
        	}
        }
	}

	
	public void propertiesMap_iterateProperties(Map<String, Object> properties, IteratePropertiesCallback ipc) throws ReflectiveOperationException, IllegalArgumentException, IntrospectionException {
		for (Entry<String, Object> entry : properties.entrySet()) {
        	ipc.callback(entry.getKey(), entry.getValue());
		}
	}

}

interface IteratePropertiesCallback {
    public void callback(String key, Object value);
}