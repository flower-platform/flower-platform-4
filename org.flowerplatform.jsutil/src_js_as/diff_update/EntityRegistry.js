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
	if (parentUid) {
		var parent = this.registry[parentUid];
		var parentChildren = this.entityOperationsAdapter.getChildrenList(parent, childrenProperty);
		this.entityOperationsAdapter.list_addItem(parentChildren, entity, index);
	}
	this.registerEntityInternal(entity, parentUid, childrenProperty);
};

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

EntityRegistry.prototype.registerEntityInternal = function(entity, parentUid, childrenProperty) {
	var uid = this.entityOperationsAdapter.getEntityUid(entity);

	var oldEntity = this.registry[uid];
	if (!oldEntity) {
		this.registry[uid] = entity;
	} else { // merge entity
		
		var oldPropertiesHolder = this.entityOperationsAdapter.object_getPropertiesHolder(this.registry[uid]);
		
		var oldPropertiesSet = null;
		if (this.entityOperationsAdapter.object_hasDynamicProperties(entity)) {
			oldPropertiesSet = {};
			this.entityOperationsAdapter.object_iterateProperties(oldPropertiesHolder, function (key, value) {
				oldPropertiesSet[key] = true;
			});
		}
		
		var propertiesHolder = this.entityOperationsAdapter.object_getPropertiesHolder(entity);
		this.entityOperationsAdapter.object_iterateProperties(propertiesHolder, function (key, value) {
			oldPropertiesHolder[key] = value;
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
		this.registerEntityInternal(child, parentUid, childrenProperty);
		if (oldChildrenSet) {
			var childUid = this.entityOperationsAdapter.getEntityUid(child);
			oldChildrenSet[childUid] = false;
			this.entityOperationsAdapter.list_setItemAt(children, this.getEntityByUid(childUid), i);
		}
	}
	if (oldChildrenSet) {
		for (var uid in oldChildrenSet) {
			if (oldChildrenSet[uid]) {
				delete this.registry[uid];
			}
		}
		this.entityOperationsAdapter.setChildren(parent, childrenProperty, children);
	}
};

// TODO CS: la fel ca mai sus: in anumite cazuri nu am nevoie sa sterg din lista de parinte
EntityRegistry.prototype.unregisterEntity = function(uid) {
	var entity = this.getEntityByUid(uid);

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
	this.entityOperationsAdapter.propertiesMap_iterateProperties(properties, function (key, value) {
		trace("*** EntityRegistry:213 - "+ key + " " + value);
		if (key == 'data') return;
		propertiesHolder[key] = value;
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
	//java.lang.System.out.println("*** registry ***");
	for (var prop in this.registry) {
//		java.lang.System.out.println(prop + " : " + this.registry[prop]);
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