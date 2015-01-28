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
	this.entityChangeListeners = [];
	// key = uid; value = entity
	this.registry = {};
	this.entityOperationsAdapter = this.entityRegistryManager.entityOperationsAdapter;
};

EntityRegistry.prototype.registerEntity = function(entity, parentUid, childrenProperty, index) {
	// TODO CS/DU: mie imi pare ca am putea sa comasam intr-o singura functie; idem si unreg?
	// 2) putem face add-ul la sfarsit? de fapt tr sa adaugam entitatea merge-uita; acum adaug
	// entitatea noua, ceea ce nu e bine
	this.registerEntityInternal(entity, parentUid, childrenProperty);
	if (parentUid) {
		var parent = this.registry[parentUid];
		var parentChildren = this.entityOperationsAdapter.getChildrenList(parent, childrenProperty);
		this.entityOperationsAdapter.list_addItem(parentChildren, entity, index);
	}

};

// TODO CS/DU: de ce avem varianta simpla si internal?
EntityRegistry.prototype.registerChildren = function(parentUid, childrenProperty, children) {
	this.registerChildrenInternal(parentUid, childrenProperty, children);
};

// TODO CS: 
/*
registerEntity:
Cand vine o entitate care deja exista: trebuie bagate prop noi, si scoase cele vechi care nu sunt in ent noua.
Eu faceam: faceam un set/map care contine cheile din ob vechi; iteram pe ob nou si bagam in cel vechi (in ac. timp scoteam din set); iteram pe set: astea sunt prop care tr. scoase

//Atentie: adaptorul trebuie sa ne dea obiectul care tine proprietati. By default = this.
oldPropertiesHolder = adapter.object_getPropertiesHolder(entity);

if (adapter.object_hasDynamicProperties(object)) {
	adapter.object_iterateProperties(object, function (key, value) {
		oldPropertiesSet[key] = true;
	});
}

adapter.object_iterateProperties(object, function (key, value) {
	oldPropertiesHolder[key] = value;
});

// iterare pe set

 */
EntityRegistry.prototype.mergeEntity = function(entity, indexesInParent) {
	return mergeEntityInternal(entity, indexesInParent, null, { } );
}

EntityRegistry.prototype.mergeEntityInternal = function(entity, indexesInParent, manyToOneRef, visitedEntities) {
	var _this = this;
	var uid = this.entityOperationsAdapter.getEntityUid(entity);
	if (visitedEntities[uid]) {
		return entity; 
	}
	visitedEntities[uid] = true;
	
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
			oldPropertiesSet = {};
			this.entityOperationsAdapter.object_iterateProperties(oldEntity, function (key, value) {
				oldPropertiesSet[key] = true;
			});
		}
	}
	
	var entityType = this.entityOperationsAdapter.getEntityType(entity);
	var propertiesFlags = this.entityOperationsAdapter.getPropertyFlagsMap(entityType);
	
	this.entityOperationsAdapter.object_iterateProperties(entity, function(property, value) {
		var propertyInfo = propertiesFlags[property];
		if (propertyInfo.flags & PROPERTY_FLAG.IGNORE != 0) {
			return;
		} else if (propertyInfo.flags & PROPERTY_FLAG.ONE_TO_MANY != 0) { 

			var oldChildrenSet;

			if (oldEntity) {
				// populate oldChildrenSet with uids of already registered children; will be used to remove the no longer existing children
				var oldChildrenList = oldEntity[property];
				var n = this.entityOperationsAdapter.list_getLength(oldChildrenList);
				for (var i = 0; i < n; i++) {
					var child = this.entityOperationsAdapter.list_getItemAt(oldChildrenList, i);
					var childUid = this.entityOperationsAdapter.getEntityUid(child);
					oldChildrenSet[childUid] = true;
				}
			}

			var childrenList = entity[property];
			var n = this.entityOperationsAdapter.list_getLength(childrenList);
			for (var i = 0; i < n; i++) {
				var child = this.entityOperationsAdapter.list_getItemAt(childrenList, i);
				var registeredChild = this.mergeEntityInternal(child, null, null, visitedEntities);
				if (oldChildrenSet) {
					// child exists, so remove it from set of children marked for deletion 
					var childUid = this.entityOperationsAdapter.getEntityUid(entity);
					delete oldChildrenSet[childUid];
				}
			}
			
			if (registeredEntity) {
				for (var uid in oldChildrenSet) {
					// TODO CM: review this
					_this.remove(uid, {});
				}
			}
		} else if (propertyInfo.flags & PROPERTY_FLAG.MANY_TO_ONE != 0) {
			var parentEntity = _this.mergeEntityInternal(registeredEntity[property], null, null, visitedEntities);
			registeredEntity[property] = parentEntity;
			
			// create children list in parent, if it doesn't exist
			if (!parentEntity[propertyInfo.oppositeProperty]) {
				parentEntity[propertyInfo.oppositeProperty] = _this.entityOperationsAdapter.list_create(parentEntity, propertyInfo.oppositeProperty);
			} 
			// add child to parent's children list
			_this.entityOperationsAdapter.addItem(parentEntity[propertyInfo.oppositeProperty], registeredEntity, -1);
		} 
	}

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

EntityRegistry.prototype.remove = function(initialEntity, entity, removeParent, visitedEntities) {
	var _this = this;

	if (entity.isRootEntity) {
		return false;
	}

	var uid = this.entityOperationsAdapter.getEntityUid(entity);
	if (visitedEntities[uid]) {
		return true; 
	}
	visitedEntities[uid] = true;

	var canRemove = true;
	this.entityOperationsAdapter.object_iterateProperties(entity, function(property, value) {
		if (!canRemove) {
			return;
		}
		var propertyInfo = propertiesFlags[property];
		
		if (propertyInfo.flags & PROPERTY_FLAG.IGNORE != 0) {
			return;
		} else if (propertyInfo.flags & PROPERTY_FLAG_ONE_TO_MANY != 0) {
			var childrenList = entity[property];
			var n = this.entityOperationsAdapter.list_getLength(childrenList);
			for (var i = 0; i < n; i++) {
				var child = this.entityOperationsAdapter.list_getItemAt(childrenList, i);
				canRemove = canRemove && _this.remove(initialEntity, child, entity, visitedEntities);
				if (!canRemove) {
					return;
				}
			}
		} else if (propertyInfo.flags & PROPERTY_MANY_TO_ONE != 0) {
			var parent = entity[property];
			if (parent == removeParent) {
				return;
			}
			var parentChildrenList = parent[propertyInfo.oppositeProperty];
			canRemove = canRemove && _this.remove(initialEntity, child, null, visitedEntities);
			if (!canRemove) {
				return;
			}
		}
	});
	
	// when bottom of call stack is reached, remove all visited entities if ok to remove  
	if ((entity == initialEntity) && canRemove) {
		for (var uid in visitedEntities) {
			delete this.registry[uid];
		}
	}
	
	return canRemove;
};

EntityRegistry.prototype.registerEntityInternal = function(entity, parentUid, childrenProperty) {
	var uid = this.entityOperationsAdapter.getEntityUid(entity);
	var oldEntity = this.registry[uid];
	var manyToOneProperties = this.entityOperationsAdapter.getManyToOneProperties(entity);

	if (!oldEntity) {
		// i.e. did not exist in the registry => newly added
		this.registry[uid] = entity;
		oldEntity = entity;
		var propertiesHolder = this.entityOperationsAdapter.object_getPropertiesHolder(entity);
		
		// manyToOneProps: replace with a "parent" instance from the registry
		if (manyToOneProperties) {
			for (var i in manyToOneProperties) {
				var property = manyToOneProperties[i];
				var refUid = this.entityOperationsAdapter.getEntityUid(propertiesHolder[property]);
				var ref = this.registry[refUid]; // if the object is not found => null; otherwise risk of infinite loop, and other nasty things
				// TODO CS/DU: de fapt cred ca trebuie sa avem un registru per procesare: daca am vizitat un element, sa nu-l mai vizitez inca o data; astfel
				// nu avem risc de infinit, si putem mereu sa facem register, chiar si la manyToOne
				if ((!ref || ref == null) && this.entityOperationsAdapter.shouldMergeManyToOneProperty(entity, property)) {
					ref = this.registerEntityInternal(propertiesHolder[property]);
				}
				propertiesHolder[property] = ref;
			}
		}
	} else { 
		// i.e. an old entity exists; it will be reused (and the new one, merged into it)
		// CS/DU: vad in functia asta ca folosim de cateva ori this.registry[uid] in log de oldEntity
		var oldPropertiesHolder = this.entityOperationsAdapter.object_getPropertiesHolder(this.registry[uid]);
		
		var oldPropertiesSet = null;
		// TODO CS/DU: ce face acest hasDynamic? vad ca ret false
		if (this.entityOperationsAdapter.object_hasDynamicProperties(entity)) {
			oldPropertiesSet = {};
			this.entityOperationsAdapter.object_iterateProperties(oldPropertiesHolder, function (key, value) {
				oldPropertiesSet[key] = true;
			});
		}
		
		var propertiesHolder = this.entityOperationsAdapter.object_getPropertiesHolder(entity);
		var this_ = this; // TODO CS/DU: mai avem apel this din callback?
		this.entityOperationsAdapter.object_iterateProperties(propertiesHolder, function (key, value) {
			if (manyToOneProperties && manyToOneProperties[0] == key) { // CS/DU ..[0] is temp
				// i.e. a many-to-one property
				var refUid = this_.entityOperationsAdapter.getEntityUid(propertiesHolder[key]);
				var ref = this_.registry[refUid]; // see many-to-one case above
				if ((!ref || ref==null) && this_.entityOperationsAdapter.shouldMergeManyToOneProperty(entity, property)) {
					ref = this_.registerEntityInternal(propertiesHolder[property]);
				}
				propertiesHolder[key] = ref;
			} else {
				// i.e. "normal" property
				oldPropertiesHolder[key] = value;
			}
			if (oldPropertiesSet) {
				oldPropertiesSet[key] = false;
			}
		});

		if (oldPropertiesSet) {
			for (var property in oldPropertiesSet) {
				if (oldPropertiesSet[property]) {
					delete oldPropertiesHolder[property];
				}
			}
		}
		
	} // end merge
	
	if (parentUid) {
		var parent = this.registry[parentUid];
		this.entityOperationsAdapter.setParent(this.registry[uid], parentUid, childrenProperty);
	}
	
	// TODO CS/DU ca treaba asta sa mearga, deocamdata adaptorul tr sa NU dea si propr
	// copii in apelul: object_iterateProperties (putin mai sus). Trebuie reorganizat, caci nu imi place, ca in
	// aceasta clipa, aici, obiectul vechi e inconsistent: are propr simple noi, si listele vechi. As vrea ca metoda
	// de mai jos sa opereze direct pe o lista, nu prin UID
	var childrenProperties = this.entityOperationsAdapter.getChildrenProperties(entity);
	if (childrenProperties) {
		for (var i in childrenProperties) {
			var childrenProperty = childrenProperties[i];
			var children = this.entityOperationsAdapter.getChildrenList(entity, childrenProperty); 
			this.registerChildrenInternal(uid, childrenProperty, children);
		}
	}
	for (var i = 0; i < this.entityChangeListeners.length; i++) {
		this.entityChangeListeners[i].entityRegistered(entity);
	}
	
	return oldEntity;
};

/*
pentru copii:
parcurg lista de copii si o bag in map (ca mai sus)
lista copii = lista noua de copii
parcurg lista copii, si scot din map
parcurg ce a mai ramas in map: ca sa scot din registru
 */
EntityRegistry.prototype.registerChildrenInternal = function(parentUid, childrenProperty, children) {
	var parent = this.getEntityByUid(parentUid);
	var oldChildrenList = this.entityOperationsAdapter.getChildrenList(parent, childrenProperty);
	var oldChildrenSet;
	if (oldChildrenList != children) {
		// the lists are equal during new object add; i.e. we recurse on an object and we want to register all
		// children
		oldChildrenSet = { };
		var n = this.entityOperationsAdapter.list_getLength(oldChildrenList);
		for (var i = 0; i < n; i++) {
			var child = this.entityOperationsAdapter.list_getItemAt(oldChildrenList, i);
			var childUid = this.entityOperationsAdapter.getEntityUid(child);
			oldChildrenSet[childUid] = true;
		}
	}
	
	var n = this.entityOperationsAdapter.list_getLength(children);
	for (var i = 0; i < n; i++) {
		var child = this.entityOperationsAdapter.list_getItemAt(children, i);
		var mergedEntity = this.registerEntityInternal(child, parentUid, childrenProperty);
		if (oldChildrenSet) {
			var childUid = this.entityOperationsAdapter.getEntityUid(child);
			oldChildrenSet[childUid] = false;
		}
		// we replace it in both cases: new object/recursive or childrenUpdate
		this.entityOperationsAdapter.list_setItemAt(children, mergedEntity, i);
	}

	// TODO CS/DU: chestia asta (if-ul) genereaza memory leak
	if (oldChildrenSet && this.entityOperationsAdapter.shouldUnregisterChildrenFromRegistry(parent, childrenProperty)) {
		for (var uid in oldChildrenSet) {
			if (oldChildrenSet[uid]) {
				delete this.registry[uid];
			}
		}
	}
	this.entityOperationsAdapter.setChildren(parent, childrenProperty, children);
};

// TODO CS: la fel ca mai sus: in anumite cazuri nu am nevoie sa sterg din lista de parinte
EntityRegistry.prototype.unregisterEntity = function(uid) {
	var entity = this.getEntityByUid(uid);
	if (!entity) {
		return;
	} 
	
	if (entity.parentUid) {
		// remove from parent
		var parent = this.getEntityByUid(entity.parentUid);
		var parentChildrenList = this.entityOperationsAdapter.getChildrenList(parent, entity.parentChildrenProperty);
		this.entityOperationsAdapter.list_removeItem(parentChildrenList, entity);
	}
	
	this.unregisterEntityInternal(uid);
	
};

EntityRegistry.prototype.unregisterEntityInternal = function(uid) {
	var entity = this.getEntityByUid(uid);
	
	var childrenProperties = this.entityOperationsAdapter.getChildrenProperties(entity);
	for (var i in childrenProperties) {
		var childrenProperty = childrenProperties[i];
		this.unregisterChildren(uid, childrenProperty);
	}
	if (entity.parentUid) {
		// remove from parent
		var parent = this.getEntityByUid(entity.parentUid);
		var parentChildrenList = this.entityOperationsAdapter.getChildrenList(parent, entity.parentChildrenProperty);
		this.entityOperationsAdapter.list_removeItem(parentChildrenList, entity);
	}
	delete this.registry[uid];
	for (var i = 0; i < this.entityChangeListeners.length; i++) {
		this.entityChangeListeners[i].entityUnregistered(entity);
	}
};

EntityRegistry.prototype.unregisterChildren = function(parentUid, childrenProperty) {
	var entity = this.getEntityByUid(parentUid);
	var children = this.entityOperationsAdapter.getChildrenList(entity, childrenProperty); 
	for (var i = 0; i < this.entityOperationsAdapter.list_getLength(children); i++) {
		var child = this.entityOperationsAdapter.list_getItemAt(children, i);
		var childUid = this.entityOperationsAdapter.getEntityUid(child);
		this.unregisterEntity(childUid);
	}
};

/*
similar ca mai sus: adapter.propertiesMap_iterateProperties(propertiesMap, handler)
 */
EntityRegistry.prototype.setProperties = function(uid, properties) {
	var entity = this.getEntityByUid(uid);

	if (entity == null) {
		return;
	}
	
	var propertiesHolder = this.entityOperationsAdapter.object_getPropertiesHolder(entity);
	var this_ = this;
	this.entityOperationsAdapter.propertiesMap_iterateProperties(properties, function (key, value, isChildrenProperty) {
		if (key == 'data') return;
		if (isChildrenProperty) {
			var children = this_.entityOperationsAdapter.getChildrenList(properties, key); 
			this_.registerChildrenInternal(uid, key, children);
		} else {
			propertiesHolder[key] = value;
		}
	});
	
	for (var i = 0; i < this.entityChangeListeners.length; i++) {
		this.entityChangeListeners[i].entityUpdated(entity);
	}

};

EntityRegistry.prototype.getEntityByUid = function(uid) {
	var entity = this.registry[uid];
	if (entity) {
		return entity;
	}
	return null;
};

// TODO CS: de invocat listenerii la operatiuni
EntityRegistry.prototype.addEntityChangeListener = function(listener) {
	this.entityChangeListeners.push(listener);
};
		
EntityRegistry.prototype.removeEntityChangeListener = function(listener) {
	var i = this.entityChangeListeners.indexOf(listener);	
	if(i != -1) {		
		this.entityChangeListeners.splice(i, 1);		
	}
};			

EntityRegistry.prototype.printDebugInfo = function() {
	java.lang.System.out.println("*** registry ***");
	for (var prop in this.registry) {
		java.lang.System.out.println(prop + " : " + this.registry[prop]);
	}
};			

/*
De intrebat Cristina

EntityRegistry.prototype.resetEntityProperties = function(entity, newProperties) {
	for (var property in newProperties) {
		this.setPropertyValue(entity, property, newProperties[property]);
	}
	// remove anything that is not in newProperties
	for(var property in entity.properties){
		if(!(property in newProperties)){
			delete entity.properties[property];
		}
	}
};

*/
var PROPERTY_FLAG = { ONE_TO_MANY = 0x1, MANY_TO_ONE = 0x2, MANY_TO_MANY = 0x4, IGNORE = 0x8 };
