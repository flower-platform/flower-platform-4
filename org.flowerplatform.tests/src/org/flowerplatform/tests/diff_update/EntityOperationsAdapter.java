//CHECKSTYLE:OFF
package org.flowerplatform.tests.diff_update;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.flowerplatform.tests.diff_update.entity.HumanResource;
import org.flowerplatform.tests.diff_update.entity.HumanResourceSchedule;
import org.flowerplatform.tests.diff_update.entity.Mission;
import org.flowerplatform.tests.diff_update.entity.ObjectAction;
import org.flowerplatform.tests.diff_update.entity.ObjectActionGroup;
import org.flowerplatform.tests.diff_update.entity.Task;
import org.mozilla.javascript.NativeObject;

/**
 * @author Claudiu Matei
 */
public class EntityOperationsAdapter {
	
	public static final int PROPERTY_FLAG_ONE_TO_MANY = 0x1;

	public static final int PROPERTY_FLAG_MANY_TO_ONE = 0x2;

	public static final int PROPERTY_FLAG_MANY_TO_MANY = 0x2;

	public static final int PROPERTY_FLAG_IGNORE = 0x8;
	
	private Map<Class<?>, Map<String, NativeObject>> propertyFlagsMap;
	
	public Class<?> getEntityType(Object entity) {
		return entity.getClass();
	}
	
	private void addOneToManyRelation(Class<?> parentType, String parentChildrenProperty, Class<?> childType, String childParentProperty) {
		Map<String, NativeObject> parentProperties = propertyFlagsMap.get(parentType);
		NativeObject relationInfo;
		
		relationInfo = new NativeObject();
		relationInfo.put("oppositeProperty", childParentProperty);
		relationInfo.put("flags", new Integer(PROPERTY_FLAG_ONE_TO_MANY));
		parentProperties.put(parentChildrenProperty, relationInfo);

		Map<String, NativeObject> childProperties = propertyFlagsMap.get(childType);
		relationInfo = new NativeObject();
		relationInfo.put("oppositeProperty", parentChildrenProperty);
		relationInfo.put("flags", new Integer(PROPERTY_FLAG_MANY_TO_ONE));
		childProperties.put(parentChildrenProperty, relationInfo);
		
	}
	
	public Map<Class<?>, Map<String, NativeObject>> getPropertyFlagsMap() {
		if (propertyFlagsMap != null) {
			return propertyFlagsMap;
		}
		
		propertyFlagsMap = new HashMap<Class<?>, Map<String, NativeObject>>();
		propertyFlagsMap.put(Mission.class, new HashMap<String, NativeObject>());
		propertyFlagsMap.put(Task.class, new HashMap<String, NativeObject>());
		propertyFlagsMap.put(ObjectActionGroup.class, new HashMap<String, NativeObject>());
		propertyFlagsMap.put(HumanResourceSchedule.class, new HashMap<String, NativeObject>());
		propertyFlagsMap.put(ObjectAction.class, new HashMap<String, NativeObject>());

		addOneToManyRelation(Mission.class, "objectActionGroups", ObjectActionGroup.class, "mission");
		addOneToManyRelation(Mission.class, "resources", HumanResource.class, "mission");
		addOneToManyRelation(Task.class, "objectActionGroups", ObjectActionGroup.class, "task");
		addOneToManyRelation(ObjectActionGroup.class, "objectActions", ObjectAction.class, "objectActionGroup");
		addOneToManyRelation(HumanResourceSchedule.class, "missions", Mission.class, "humanResourceSchedule");
		addOneToManyRelation(HumanResource.class, "humanResourceSchedules", HumanResourceSchedule.class, "humanResource");
		
		return propertyFlagsMap;
		
	}
	
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
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void list_create(Object entity, String property) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		new PropertyDescriptor(property, entity.getClass()).getWriteMethod().invoke(entity, new ArrayList<Object>());
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

	
	public boolean object_hasDynamicProperties(Object entity) {
		return false;
	}
	
	public void object_iterateProperties(Object entity, IteratePropertiesCallback ipc) throws ReflectiveOperationException, IllegalArgumentException, IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        	if (propertyDescriptor.getWriteMethod() != null && !propertyDescriptor.getName().equals("details") && !propertyDescriptor.getName().equals("subdetails")) {
            	ipc.callback(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(entity));
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
