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

import org.flowerplatform.tests.diff_update.entity.AbstractEntity;
import org.flowerplatform.tests.diff_update.entity.DetailEntity;
import org.flowerplatform.tests.diff_update.entity.HumanResource;
import org.flowerplatform.tests.diff_update.entity.HumanResourceSchedule;
import org.flowerplatform.tests.diff_update.entity.MasterEntity;
import org.flowerplatform.tests.diff_update.entity.Mission;
import org.flowerplatform.tests.diff_update.entity.ObjectAction;
import org.flowerplatform.tests.diff_update.entity.ObjectActionGroup;
import org.flowerplatform.tests.diff_update.entity.SubdetailEntity;
import org.flowerplatform.tests.diff_update.entity.Task;
import org.flowerplatform.tests.diff_update.entity.unnamed.E1;
import org.flowerplatform.tests.diff_update.entity.unnamed.E2;
import org.flowerplatform.tests.diff_update.entity.unnamed.E3;
import org.flowerplatform.tests.diff_update.entity.unnamed.E4;
import org.flowerplatform.tests.diff_update.entity.unnamed.E5;
import org.flowerplatform.tests.diff_update.entity.unnamed.E6;
import org.flowerplatform.tests.diff_update.entity.unnamed.E7;
import org.flowerplatform.tests.diff_update.entity.unnamed.E8;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.diff_update.DiffUpdate;
import org.mozilla.javascript.NativeObject;

/**
 * @author Claudiu Matei
 */
public class EntityOperationsAdapter {
	
	public static final int PROPERTY_FLAG_ONE_TO_MANY = 0x1;

	public static final int PROPERTY_FLAG_MANY_TO_ONE = 0x2;

	public static final int PROPERTY_FLAG_MANY_TO_MANY = 0x4;

	public static final int PROPERTY_FLAG_IGNORE = 0x8;
	
	public static final int PROPERTY_FLAG_NAVIGABLE = 0x10;
	
	private Map<Class<?>, Map<String, NativeObject>> propertyFlagsMap;
	
	
	public EntityOperationsAdapter() {
		
		propertyFlagsMap = new HashMap<Class<?>, Map<String, NativeObject>>();

		addOneToManyRelation(MasterEntity.class, "details", true, DetailEntity.class, "masterEntity", false);
		addOneToManyRelation(DetailEntity.class, "subdetails", true, SubdetailEntity.class, "detailEntity", false);
		
		/****************************************************************************************************************/
		
		addOneToManyRelation(Mission.class, "objectActionGroups", true, ObjectActionGroup.class, "mission", false);
		addOneToManyRelation(Mission.class, "resources", true, HumanResource.class, "mission", false);
		addOneToManyRelation(Task.class, "objectActionGroups", true, ObjectActionGroup.class, "task", false);
		addOneToManyRelation(ObjectActionGroup.class, "objectActions", true, ObjectAction.class, "objectActionGroup", false);
		addOneToManyRelation(HumanResourceSchedule.class, "missions", true, Mission.class, "humanResourceSchedule", false);
		addOneToManyRelation(HumanResource.class, "humanResourceSchedules", false, HumanResourceSchedule.class, "humanResource", true);
		
		ignoreProperty(Mission.class, "instanceId");
		ignoreProperty(Task.class, "instanceId");
		ignoreProperty(ObjectActionGroup.class, "instanceId");
		ignoreProperty(ObjectAction.class, "instanceId");
		ignoreProperty(HumanResourceSchedule.class, "instanceId");
		ignoreProperty(HumanResource.class, "instanceId");

		/****************************************************************************************************************/
		
		addOneToManyRelation(E1.class, "e2List", true, E2.class, "e1Ref", false);
		addOneToManyRelation(E7.class, "e1List", false, E1.class, "e7Ref", true);
		addOneToManyRelation(E2.class, "e3List", true, E3.class, "e2Ref", false);
		addOneToManyRelation(E5.class, "e2List", true, E2.class, "e5Ref", false);
		addOneToManyRelation(E4.class, "e3List", true, E3.class, "e4Ref", true);
		addOneToManyRelation(E4.class, "e5List", true, E5.class, "e4Ref", false);
		addOneToManyRelation(E5.class, "e6List", false, E6.class, "e5Ref", true);
		addOneToManyRelation(E6.class, "e7List", false, E7.class, "e6Ref", true);
		
		addOneToManyRelation(E8.class, "e3List", true, E3.class, "e8Ref", true);
		addOneToManyRelation(E8.class, "e5List", false, E5.class, "e8Ref", true);
		
	}

	private void addOneToManyRelation(Class<?> parentType, String parentChildrenProperty, boolean navigableFromParent, Class<?> childType, String childParentProperty, boolean navigableFromChild) {
		NativeObject relationInfo;
		
		Map<String, NativeObject> parentProperties = propertyFlagsMap.get(parentType);
		if (parentProperties == null) {
			parentProperties = new HashMap<String, NativeObject>();
			propertyFlagsMap.put(parentType, parentProperties);
		}
		relationInfo = new NativeObject();
		relationInfo.defineProperty("oppositeType", childType, NativeObject.READONLY);
		relationInfo.defineProperty("oppositeProperty", childParentProperty, NativeObject.READONLY);
		relationInfo.defineProperty("flags", new Integer(PROPERTY_FLAG_ONE_TO_MANY | (navigableFromParent ? PROPERTY_FLAG_NAVIGABLE : 0)), NativeObject.READONLY);
		parentProperties.put(parentChildrenProperty, relationInfo);

		Map<String, NativeObject> childProperties = propertyFlagsMap.get(childType);
		if (childProperties == null) {
			childProperties = new HashMap<String, NativeObject>();
			propertyFlagsMap.put(childType, childProperties);
		}
		relationInfo = new NativeObject();
		relationInfo.defineProperty("oppositeType", parentType, NativeObject.READONLY);
		relationInfo.defineProperty("oppositeProperty", parentChildrenProperty, NativeObject.READONLY);
		relationInfo.defineProperty("flags", new Integer(PROPERTY_FLAG_MANY_TO_ONE | (navigableFromChild ? PROPERTY_FLAG_NAVIGABLE : 0)), NativeObject.READONLY);
		childProperties.put(childParentProperty, relationInfo);
		
	}
	
	private void ignoreProperty(Class<?> type, String property) {
		Map<String, NativeObject> properties = propertyFlagsMap.get(type);
		if (properties == null) {
			properties = new HashMap<String, NativeObject>();
			propertyFlagsMap.put(type, properties);
		}

		NativeObject relationInfo = new NativeObject();
		relationInfo.defineProperty("property", property, NativeObject.READONLY);
		relationInfo.defineProperty("flags", PROPERTY_FLAG_IGNORE, NativeObject.READONLY);
		properties.put("property", relationInfo);
	}
	
	public void postProcessUpdate(NativeObject entityRegistry, DiffUpdate update) {
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Operations on list
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @author See class.
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void list_create(Object entity, String property) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		new PropertyDescriptor(property, entity.getClass()).getWriteMethod().invoke(entity, new ArrayList<AbstractEntity>());
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

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Operations on object
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean object_isRoot(AbstractEntity rootOrEntity) {
		return rootOrEntity.isRoot();
	}

	/**
	 * @author See class.
	 */
	public String object_getEntityUid(Object entity) {
		String uid = entity.getClass().getSimpleName() + ":" + ((AbstractEntity) entity).getId();
		return uid;
	}

	public boolean object_equals(Object obj1, Object obj2) {
		return Utils.safeEquals(obj1, obj2);
	}

	public NativeObject object_getPropertyInfo(Object entity, String property) {
		Map<String, NativeObject> properties = propertyFlagsMap.get(entity.getClass());
		if (properties == null) {
			return null;
		}
		return properties.get(property);
	}
	
	public boolean object_hasDynamicProperties(Object entity) {
		return false;
	}
	
	public void object_iterateProperties(Object entity, IteratePropertiesCallback ipc) throws ReflectiveOperationException, IllegalArgumentException, IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        	if (propertyDescriptor.getWriteMethod() != null) {
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

