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

var EntityRegistry = function(entityRegistryManager) { 
	this.entityRegistryManager = entityRegistryManager; 
	// key = uid; value = entity
	this.registry = { };
	this.entityOperationsAdapter = this.entityRegistryManager.entityOperationsAdapter;
};

/* mai e de facut: 

* relatii unidirectionale *** terminat
* many-to-many *** nu facem momentan
* adaugarea in parinte la index stabilit (indexesInParent) 

*/

EntityRegistry.prototype.mergeEntity = function(entity) {
	var entitiesToRemove = [];
	this.mergeEntityInternal(entity, null, { }, entitiesToRemove);

	for (var i in entitiesToRemove) {
		this.removeInternal(entitiesToRemove[i]);
	}
};

/**
 * If the entity doesn't exist in the registry => it is added. Otherwise, it is merged,
 * i.e. all properties of the entity will be copied into the old entity (i.e. the one that
 * exists in the registry).
 * 
 * The method is recursive. If it finds properties that are relations (one-to-many, many-to-one,
 * many-to-many), the referenced entities are merged as well.
 * 
 * @param entity - The entity that has been received from the server (can exist or not in the registry).
 * @param indexesInParent - Not used
 * @param visitedEntities - Internal map, used to know if an entity has been processed (i.e. to avoid
 * 		infinite recursion for bi-directionnal relations
 * @param entitiesToRemove - list of entities that are candidates for removal after merge
 * 
 * @returns The entity from the registry (same instance, if was not in registry)
 */
EntityRegistry.prototype.mergeEntityInternal = function(entity, indexesInParent, visitedEntities, entitiesToRemove) {
	var uid = this.entityOperationsAdapter.object_getEntityUid(entity);
	if (visitedEntities[uid]) {
		return this.registry[uid]; 
	}
	
	var oldEntity = this.registry[uid];
	var registeredEntity = oldEntity;
	var oldPropertiesSet = null;

	if (!oldEntity) {
		// entity is not in registry
		this.registry[uid] = entity;
		registeredEntity = entity;
	} else {
		// i.e. an old entity exists; it will be reused (and the new properties, merged into it)
		if (this.entityOperationsAdapter.object_hasDynamicProperties(entity)) {
			// if dynamic properties are possible, we need to erase properties that existed in the old
			// entity, but that don't exist in the new entity
			oldPropertiesSet = {};
			this.entityOperationsAdapter.object_iterateProperties(oldEntity, function (property, value) {
				oldPropertiesSet[property] = true;
			});
		}
	}

	visitedEntities[uid] = true;
	
	var _this = this;
	this.entityOperationsAdapter.object_iterateProperties(entity, function(property, value) {
		if (oldPropertiesSet) {
			delete oldPropertiesSet[property];
		}
		var propertyInfo = _this.entityOperationsAdapter.object_getPropertyInfo(entity, property);
		_this.processProperty(property, value, propertyInfo, oldEntity, registeredEntity, visitedEntities, entitiesToRemove);
	});
	
	
	// delete no longer existing properties
	if (oldPropertiesSet) {
		for (var property in oldPropertiesSet) {
			if (oldPropertiesSet[property]) {
				delete registeredEntity[property];
			}
		}
	}

	return registeredEntity;
};

/**
 * When processing relations:
 * 1) if we have bi-directionnal relations, it means that both ends will be visited. In this case, the one-to-many side
 * will also operate on many-to-one.
 * 2) if we have uni-directionnal relations, when the non-navigable end is visited: we don't do anything. When the navigable
 * end is visited, it operates for the other end as well. 
 * 
 * @param oldEntity - may be null (for a newly added entity)
 */
EntityRegistry.prototype.processProperty = function(property, value, propertyInfo, oldEntity, registeredEntity, visitedEntities, entitiesToRemove) {

	if (!propertyInfo) {
		// not a special property  
		if (oldEntity) {
			oldEntity[property] = value;
		}
		return;
	}

	if (propertyInfo.flags & PROPERTY_FLAG_IGNORE) {
		return;
	} else if (propertyInfo.flags & PROPERTY_FLAG_ONE_TO_MANY) { 
		// i.e. a list of references
		
		// property is non-navigable => there is nothing to process (field is null)
		if (!(propertyInfo.flags & PROPERTY_FLAG_NAVIGABLE)) {
			return;
		}
			
		var oldChildrenSet;
		if (oldEntity && oldEntity[property]) {
			oldChildrenSet = { };
			// iterate old list; populate oldChildrenSet with uids of already registered children; will be used to remove the no longer existing children
			var oldChildrenList = oldEntity[property];
			var n = this.entityOperationsAdapter.list_getLength(oldChildrenList);
			for (var i = 0; i < n; i++) {
				var child = this.entityOperationsAdapter.list_getItemAt(oldChildrenList, i);
				var childUid = this.entityOperationsAdapter.object_getEntityUid(child);
				oldChildrenSet[childUid] = true;
			}
		}

		// iterate new list
		var childrenList = value;
		var childrenListModified = false;
		
		var n = childrenList ? this.entityOperationsAdapter.list_getLength(childrenList) : 0;
		for (var i = 0; i < n; i++) {
			var child = this.entityOperationsAdapter.list_getItemAt(childrenList, i);
			var childUid = this.entityOperationsAdapter.object_getEntityUid(child);
			var registeredChild = this.mergeEntityInternal(child, null, visitedEntities, entitiesToRemove);

			if (child != registeredChild) {
				childrenListModified = true;
				this.entityOperationsAdapter.list_setItemAt(childrenList, registeredChild, i);
			}
			
			// If child's parent changed, remove child from former parent's list and try to remove former parent.
			// We compare UIDs because there might be a different instance of the entity, but the same entity uid
			if (!(propertyInfo.flags & PROPERTY_FLAG_DONT_REMOVE_FROM_OPPOSITE) && registeredChild[propertyInfo.oppositeProperty] && this.entityOperationsAdapter.object_getEntityUid(registeredChild[propertyInfo.oppositeProperty]) != this.entityOperationsAdapter.object_getEntityUid(registeredEntity)) {
				this.entityOperationsAdapter.list_removeItem(registeredChild[propertyInfo.oppositeProperty][property], registeredChild);
				entitiesToRemove.push(registeredChild[propertyInfo.oppositeProperty]);
			}
			registeredChild[propertyInfo.oppositeProperty] = registeredEntity;
			
			if (oldEntity && oldChildrenSet) {
				// child exists, so remove it from set of children marked for deletion 
				delete oldChildrenSet[childUid];
			}
		}
		
		if (oldChildrenSet) {
			for (var uid in oldChildrenSet) {
				if (!childrenListModified) {
					childrenListModified = true;
				}
				var oldChild = this.registry[uid];
				if (this.entityOperationsAdapter.object_getEntityUid(oldChild[propertyInfo.oppositeProperty]) == this.entityOperationsAdapter.object_getEntityUid(registeredEntity)) {
					// we need this test for the case when the referenced entity still exists, but it has been "moved"
					// to another entity. E.g. an object has been moved from a flight to another flight. And the other
					// flight has been processed before, i.e. we don't need to break the link.
					// We shouldn't have to compare the uids of the 2 objects, we should only compare the actual instances. 
					// However, this instance equality test doesn't work with Rhino in this scenario, so we need to compare the uids.
					oldChild[propertyInfo.oppositeProperty] = null;
				}
				entitiesToRemove.push(oldChild);
			}
		}
		var oldChildrenListSize = oldEntity && oldEntity[property] ? this.entityOperationsAdapter.list_getLength(oldEntity[property]) : 0; 
		if (childrenListModified || oldChildrenListSize != this.entityOperationsAdapter.list_getLength(value)) {
			registeredEntity[property] = value;
		}
	} else if (propertyInfo.flags & PROPERTY_FLAG_MANY_TO_ONE) {
		// i.e. a "single" reference

		// process references only when this entity is the navigable end of a unidirectional relationship  
		if (!(propertyInfo.flags & PROPERTY_FLAG_NAVIGABLE)) {
			return;
		}
		
		if (!value) { // new entity has no referenced entity
			if (oldEntity && oldEntity[property]) { // but old entity references an entity
				this.entityOperationsAdapter.list_removeItem(oldEntity[property][propertyInfo.oppositeProperty], oldEntity); // remove old entity from formerly referenced entity's list
				entitiesToRemove.push(oldEntity[property]); // try to remove the formerly referenced entity
			}
			registeredEntity[property] = null;
			return;
		}

		var oppositeEntity = this.mergeEntityInternal(value, null, visitedEntities, entitiesToRemove);
		var parentPropertyInfo = this.entityOperationsAdapter.object_getPropertyInfo(oppositeEntity, propertyInfo.oppositeProperty);
		
		if (!(parentPropertyInfo.flags & PROPERTY_FLAG_NAVIGABLE)) {
			// this entity is the navigable end of a unidirectional relationship

			var parentPropertyInfo = this.entityOperationsAdapter.object_getPropertyInfo(oppositeEntity, propertyInfo.oppositeProperty);

			// create list in parent, if it doesn't exist
			if (!oppositeEntity[propertyInfo.oppositeProperty]) {
				this.entityOperationsAdapter.list_create(oppositeEntity, propertyInfo.oppositeProperty);
			}

			if (!oldEntity || (oldEntity[property] && oppositeEntity != oldEntity[property])) { // if there is no old entity or the reference has changed
				// add registered entity to new opposite entity's list
				this.entityOperationsAdapter.list_addItem(oppositeEntity[propertyInfo.oppositeProperty], registeredEntity, -1);
				if (oldEntity) { // if old reference has changed
					// remove entity from former parent's children list
					this.entityOperationsAdapter.list_removeItem(oldEntity[property][propertyInfo.oppositeProperty], oldEntity);
				}
			}

			registeredEntity[property] = oppositeEntity;
		}
		
	} else if (oldEntity) {
		oldEntity[property] = value;
	}
};

/**
 * Removes entity from registry, breaks all its links and attempts to remove all entities referenced by the removed entity
 */
EntityRegistry.prototype.remove = function(entityUid) {
	var entity = this.getEntityByUid(entityUid);
	if (!entity) {
		return;
	}
	var _this = this;
	var entitiesToRemove = [];
	this.entityOperationsAdapter.object_iterateProperties(entity, function(property, value) {
		var propertyInfo = _this.entityOperationsAdapter.object_getPropertyInfo(entity, property);
		if (!propertyInfo || (propertyInfo.flags & PROPERTY_FLAG_IGNORE)) {
			return;
		}
		if (value == null) {
			return;
		}
		if (propertyInfo.flags & PROPERTY_FLAG_ONE_TO_MANY) {
			var n = _this.entityOperationsAdapter.list_getLength(value);
			for (var i = 0; i < n; i++) {
				var oppositeEntity = _this.entityOperationsAdapter.list_getItemAt(value, i);
				oppositeEntity[propertyInfo.oppositeProperty] = null;
				if (propertyInfo.flags & PROPERTY_FLAG_NAVIGABLE) {
					entitiesToRemove.push(oppositeEntity);
				}
			}
		} else if (propertyInfo.flags & PROPERTY_FLAG_MANY_TO_ONE) {
			_this.entityOperationsAdapter.list_removeItem(value[propertyInfo.oppositeProperty], entity);
			if (propertyInfo.flags & PROPERTY_FLAG_NAVIGABLE) {
				entitiesToRemove.push(value);
			}
		}
	});
	delete this.registry[this.entityOperationsAdapter.object_getEntityUid(entity)];
	for (var i in entitiesToRemove) {
		this.removeInternal(entitiesToRemove[i]);
	}
};

/**
 * Attempts to remove the entity and all subsequent entities which are indirectly referenced by root only through it. 
 * 
 */
EntityRegistry.prototype.removeInternal = function(entity) {
	var entityUid = this.entityOperationsAdapter.object_getEntityUid(entity);
	if (!this.registry[entityUid]) {
		return;
	}
	
	var status = { nonRemovableFound: false };
	var nonRemovableEntities = { };
	var linksToRemove;
	var visitedEntities;
	
	do {
		visitedEntities = { };
		linksToRemove = [];
		status.nonRemovableFound = false;
		this.findNonRemovableEntities(entity, nonRemovableEntities, visitedEntities, status, linksToRemove);
	} while (status.nonRemovableFound);
	
	for (var uid in visitedEntities) {
		delete this.registry[uid];
	}

	for (var i in linksToRemove) {
		var link = linksToRemove[i];
		if (!link.unlinkedEntity) { // many to one relationship 
			link.entity[link.property] = null;
		} else { // one to many relationship (i.e. list of linked entities)
			this.entityOperationsAdapter.list_removeItem(link.entity[link.property], link.unlinkedEntity);
		}
	}

};

/**
 * TODO CS: update dupa refactor
 * Discovers non removable entities (i.e. entities which are referenced by other non removable entities), starting from the given entity.
 * It does not guarantee the discovery of all non removable entities (due to possible cyclic model), so it must be called until no new removable entities are found. 
 * 
 * @param initialEntity - The entity that we want to remove
 * @param entity - For the first call, same param as previous
 * @param visitedEntities - The entities visited during the removal session; not the same with the other param
 * 		used during merge.
 * @returns false if this will not be removed. I.e. it is still referenced from a graph starting with the rootObject
 */
EntityRegistry.prototype.findNonRemovableEntities = function(entity, nonRemovableEntities, visitedEntities, status, linksToRemove) {
	var uid = this.entityOperationsAdapter.object_getEntityUid(entity);

	if (this.entityOperationsAdapter.object_isRoot(entity) || nonRemovableEntities[uid]) {
		return false;
	}

	if (visitedEntities[uid]) {
		return true; 
	}
	visitedEntities[uid] = true;

	var _this = this;
	var canRemove = true;
	this.entityOperationsAdapter.object_iterateProperties(entity, function(property, value) {
		if (!canRemove) {
			return;
		}
		var propertyInfo = _this.entityOperationsAdapter.object_getPropertyInfo(entity, property);
		if (!propertyInfo) {
			return;
		}
		
		if (propertyInfo.flags & PROPERTY_FLAG_IGNORE) {
			return;
		} else if (propertyInfo.flags & PROPERTY_FLAG_ONE_TO_MANY) {
			var childrenList = value;
			if (!childrenList) {
				return;
			}
			var n = _this.entityOperationsAdapter.list_getLength(childrenList);
			for (var i = 0; i < n; i++) {
				var child = _this.entityOperationsAdapter.list_getItemAt(childrenList, i);
				var canRemoveOpposite = _this.findNonRemovableEntities(child, nonRemovableEntities, visitedEntities, status, linksToRemove);
				var oppositePropertyInfo = _this.entityOperationsAdapter.object_getPropertyInfo(child, propertyInfo.oppositeProperty);
				if (!canRemoveOpposite) {
					if (oppositePropertyInfo.flags & PROPERTY_FLAG_NAVIGABLE) {
						canRemove = false;
						nonRemovableEntities[uid] = true;
						status.nonRemovableFound = true;
						return;
					} 
					linksToRemove.push({ entity: child, property: propertyInfo.oppositeProperty });
				}
			}
		} else if (propertyInfo.flags & PROPERTY_FLAG_MANY_TO_ONE) {
			var parent = value;
			if (!parent) {
				return;
			}
			var canRemoveOpposite = _this.findNonRemovableEntities(parent, nonRemovableEntities, visitedEntities, status, linksToRemove);
			var oppositePropertyInfo = _this.entityOperationsAdapter.object_getPropertyInfo(parent, propertyInfo.oppositeProperty);
			if (!canRemoveOpposite) {
				if (oppositePropertyInfo.flags & PROPERTY_FLAG_NAVIGABLE) {
					canRemove = false;
					nonRemovableEntities[uid] = true;
					status.nonRemovableFound = true;
					return;
				}
				linksToRemove.push({ entity: parent, property: propertyInfo.oppositeProperty, unlinkedEntity: entity });
			}
		}
	});
	return canRemove;
};

EntityRegistry.prototype.setProperties = function(uid, properties) {
	var _this = this;
	var entity = this.getEntityByUid(uid);

	if (entity == null) {
		return;
	}

	var visitedEntities = { };
	visitedEntities[uid] = true;

	var entitiesToRemove = [];
	this.entityOperationsAdapter.propertiesMap_iterateProperties(properties, function (property, value) {
		if (property == 'data') return;
		
		var propertyInfo = _this.entityOperationsAdapter.object_getPropertyInfo(entity, property);
		
		_this.processProperty(property, value, propertyInfo, entity, entity, visitedEntities, entitiesToRemove);
		
	});

	for (var i in entitiesToRemove) {
		this.remove(entitiesToRemove[i]);
	}

};

EntityRegistry.prototype.getEntityByUid = function(uid) {
	var entity = this.registry[uid];
	if (entity) {
		return entity;
	}
	return null;
};

EntityRegistry.prototype.printDebugInfo = function() {
//	 java.lang.System.out.println("*** registry ***");
//	for (var prop in this.registry) {
//		 java.lang.System.out.println(prop + " : " + this.registry[prop]);
//	}
};			

var PROPERTY_FLAG_ONE_TO_MANY = 0x1;

var PROPERTY_FLAG_MANY_TO_ONE = 0x2;

var PROPERTY_FLAG_MANY_TO_MANY = 0x4;

var PROPERTY_FLAG_IGNORE = 0x8;

var PROPERTY_FLAG_NAVIGABLE = 0x10;

var PROPERTY_FLAG_DONT_REMOVE_FROM_OPPOSITE = 0x20;
