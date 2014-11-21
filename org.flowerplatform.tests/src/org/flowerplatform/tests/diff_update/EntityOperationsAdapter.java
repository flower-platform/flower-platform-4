package org.flowerplatform.tests.diff_update;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



/**
 * 
 * @author Claudiu Matei
 *
 */
public class EntityOperationsAdapter {
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public String getEntityUid(Object entity) {
		String uid = entity.getClass().getSimpleName() + ":" + ((AbstractEntity) entity).getId();
		return uid;
	}
	
	/**
	 * 
	 * @param entity
	 * @param childrenProperty
	 * @param children
	 */
	public void setChildren(Object entity, String childrenProperty, List<Object> children) {
		((MasterEntity) entity).setDetails(children);
	}

	/**
	 * 
	 * @param entity
	 * @param childrenProperty
	 * @param children
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void addChild(Object parent, String childrenProperty, Object child, int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
			IntrospectionException {
		@SuppressWarnings("unchecked")
		List<Object> children = (List<Object>) new PropertyDescriptor(childrenProperty, parent.getClass()).getReadMethod().invoke(parent);
		if (index == -1) {
			children.add(child);
		} else {
			children.add(index, child);
		}
		
	}
	
	/**
	 * 
	 * @param entity
	 * @param parentUid
	 * @param parentChildrenProperty
	 */
	public void setParent(Object entity, String parentUid, String parentChildrenProperty) {
		DetailEntity childEntity = (DetailEntity) entity;
		childEntity.setParentUid(parentUid);
		childEntity.setParentChildrenProperty(parentChildrenProperty);
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public String[] getChildrenProperties(Object entity) {
		if (entity instanceof MasterEntity) {
			return new String[] { "details" };
		}
		return null;
	}
	
	/**
	 * 
	 * @param entity
	 * @param childrenProperty
	 * @return
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public Object[] getChildren(Object entity, String childrenProperty) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		@SuppressWarnings("unchecked")
		List<Object> children = (List<Object>) new PropertyDescriptor(childrenProperty, entity.getClass()).getReadMethod().invoke(entity);
		return children.toArray(); 
	}

	/**
	 * 
	 * @param parent
	 * @param childrenProperty
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public void removeChildren(Object parent, String childrenProperty) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		@SuppressWarnings("unchecked")
		List<Object> children = (List<Object>) new PropertyDescriptor(childrenProperty, parent.getClass()).getReadMethod().invoke(parent);
		children.clear(); 
	}

	/**
	 * 
	 * @param parent
	 * @param childrenProperty
	 * @param child
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public void removeChild(Object parent, String childrenProperty, Object child) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
			IntrospectionException {
		@SuppressWarnings("unchecked")
		List<Object> children = (List<Object>) new PropertyDescriptor(childrenProperty, parent.getClass()).getReadMethod().invoke(parent);
		children.remove(child); 
	}

	/**
	 * 
	 * @param entityType
	 * @param properties
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public Object createEntity(String entityType, Map<String, Object> properties) throws InstantiationException, IllegalAccessException, 
			ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Object entity = Class.forName(entityType).newInstance();
		for (Entry<String, Object> entry : properties.entrySet()) {
			new PropertyDescriptor(entry.getKey(), entity.getClass()).getWriteMethod().invoke(entity, entry.getValue());
		}
		return entity;
	}
	
}
