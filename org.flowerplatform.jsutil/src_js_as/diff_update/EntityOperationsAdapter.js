/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
 
var EntityOperationsAdapter = function() {
	
};

EntityOperationsAdapter.prototype.getEntityUid = function(entity) {
	return entity.uid;
};

EntityOperationsAdapter.prototype.setChildren = function(entity, childrenProperty, children) {
	entity[childrenProperty] = children;
}

EntityOperationsAdapter.prototype.setParent = function(entity, parentUid, parentChildrenProperty) {
	entity.parentUid = parentUid;
	entity.parentChildrenProperty = parentChildrenProperty;
}

EntityOperationsAdapter.prototype.getChildrenProperties=function(entity) {
	var childrenProperties = [];
	for (var property in entity) {
		if (entity[property].constructor === Array) {
			childrenProperties.push(property);
		}
	}
	return childrenProperties;
}

EntityOperationsAdapter.prototype.getChildrenLists = function(entity) {
	var lists = [];
	for (var property in entity) {
		if (entity[property].constructor === Array) {
			lists.push(entity[property]);
		}
	}
	return lists;
}

EntityOperationsAdapter.prototype.getChildrenList = function(entity, property) {
	return entity[property];
}


EntityOperationsAdapter.prototype.list_addItem = function(list, element, index) {
	if (index == -1) {
		list.push(element);
	} else {
		list.splice(index, 0, element);	
	}
}

EntityOperationsAdapter.prototype.list_setItemAt = function(list, element, index) {
	list[index] = element;
}

EntityOperationsAdapter.prototype.list_removeItemAt(list, index) {
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
