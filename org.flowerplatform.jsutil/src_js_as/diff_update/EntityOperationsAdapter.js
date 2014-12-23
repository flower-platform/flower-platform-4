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
 
var EntityOperationsAdapter = function() { };

EntityOperationsAdapter.prototype.getEntityUid = function(entity) {
	return entity.entityUid;
};

EntityOperationsAdapter.prototype.setChildren = function(entity, childrenProperty, children) {
	entity[childrenProperty] = children;
};

EntityOperationsAdapter.prototype.setParent = function(entity, parentUid, parentChildrenProperty) {
	entity.parentUid = parentUid;
	entity.parentChildrenProperty = parentChildrenProperty;
};

EntityOperationsAdapter.prototype.getChildrenProperties=function(entity) {
	var childrenProperties = [];
	for (var property in entity) {
		if (entity[property].constructor === Array) {
			childrenProperties.push(property);
		}
	}
	return childrenProperties;
};

EntityOperationsAdapter.prototype.getChildrenLists = function(entity) {
	var lists = [];
	for (var property in entity) {
		if (entity[property].constructor === Array) {
			lists.push(entity[property]);
		}
	}
	return lists;
};

EntityOperationsAdapter.prototype.getChildrenList = function(entity, property) {
	return entity[property];
};


EntityOperationsAdapter.prototype.list_addItem = function(list, element, index) {
	if (index == -1) {
		list.push(element);
	} else {
		list.splice(index, 0, element);	
	}
};

EntityOperationsAdapter.prototype.list_setItemAt = function(list, element, index) {
	list[index] = element;
};

EntityOperationsAdapter.prototype.list_removeItemAt = function(list, index) {
	return list.remove(index);
};

EntityOperationsAdapter.prototype.list_removeItem = function(list, element) {
	var index = list.indexOf(element);
	list.splice(index, 1);
};

EntityOperationsAdapter.prototype.list_getLength = function(list) {
	return list.length;
};

EntityOperationsAdapter.prototype.list_getItemAt = function(list, index) {
	return list[index];
};


EntityOperationsAdapter.prototype.object_getPropertiesHolder = function(entity) {
	return entity;
};

EntityOperationsAdapter.prototype.object_hasDynamicProperties = function(entity) {
	return false;
};

EntityOperationsAdapter.prototype.object_iterateProperties = function(propertiesHolder, callback) {
	for (var property in propertiesHolder) {
		callback(property, propertiesHolder[property]);
	}
};

EntityOperationsAdapter.prototype.propertiesMap_iterateProperties = function(properties, callback) {
	for (var property in properties) {
		callback(property, properties[property]);
	}
};
