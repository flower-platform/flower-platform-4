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
	this.registry = {};
	this.entityOperationsAdapter = this.entityRegistryManager.entityOperationsAdapter;
};

EntityRegistry.prototype.registerEntity = function(entity) {
	if (!this.registry[entity]) {
		var uid = this.entityOperationsAdapter.getEntityUid(entity);
		this.registry[uid] = entity;

		// add children to registry
		var childrenProperties = this.entityOperationsAdapter.getChildrenProperties(entity);
		if (childrenProperties) {
			for (var i in childrenProperties) {
				var childrenProperty = childrenProperties[i];
				var children = this.entityOperationsAdapter.getChildren(entity, childrenProperty); 
				for (var j in children) {
					var child = children[j];
					var childUid = this.entityOperationsAdapter.getEntityUid(child);
					this.registry[childUid] = child;
					this.entityOperationsAdapter.setParent(child, uid, childrenProperty);
				}
			}
		}
	} else {
		// merge
	}
};

EntityRegistry.prototype.registerChildEntities = function(parentUid, childrenProperty, children) {
	var parent = this.registry[parentUid];
	this.entityOperationsAdapter.setChildren(parent, childrenProperty, children);
	
	var childEntities = this.entityOperationsAdapter.getChildren(parent, childrenProperty); 
	for (var i in childEntities) {
		var child = childEntities[i];
		var childUid = this.entityOperationsAdapter.getEntityUid(child);
		this.registry[childUid] = child;
		this.entityOperationsAdapter.setParent(child, parentUid, childrenProperty);
	}
};

EntityRegistry.prototype.registerChildEntity = function(parentUid, childrenProperty, child, index) {
	var parent = this.registry[parentUid];
	this.entityOperationsAdapter.addChild(parent, childrenProperty, child, index);

	var childUid = this.entityOperationsAdapter.getEntityUid(child);
	this.registry[childUid] = child;
	this.entityOperationsAdapter.setParent(child, parentUid, childrenProperty);
};


EntityRegistry.prototype.unregisterEntity = function(uid) {
	this.unregister(uid);
};

EntityRegistry.prototype.unregisterChildren = function(parentUid, childrenProperty) {
	var entity = this.getEntityByUid(parentUid);
	var children = this.entityOperationsAdapter.getChildren(entity, childrenProperty); 
	for (var j in children) {
		var child = children[j];
		var childUid = this.entityOperationsAdapter.getEntityUid(child);
		this.unregister(childUid);
	}
};

EntityRegistry.prototype.unregisterChild = function(parentUid, childUid) {
	var parent = this.getEntityByUid(parentUid);
	var child = this.getEntityByUid(childUid);
	this.entityOperationsAdapter.removeChild(parent, child.parentChildrenProperty, child);
	this.unregister(childUid);
};

EntityRegistry.prototype.unregister = function(uid) {
	var entity = this.getEntityByUid(uid);
	var childrenProperties = this.entityOperationsAdapter.getChildrenProperties(entity);
	if (childrenProperties) {
		for (var i in childrenProperties) {
			var childrenProperty = childrenProperties[i];
			var children = this.entityOperationsAdapter.getChildren(entity, childrenProperty); 
			for (var j in children) {
				var child = children[j];
				var childUid = this.entityOperationsAdapter.getEntityUid(child);
				this.unregister(childUid);
			}
		}
	}
	delete this.registry[uid];
};


EntityRegistry.prototype.getEntityByUid = function(uid) {
	if (uid in this.registry) {
		return this.registry[uid];
	}
	return null;
};

EntityRegistry.prototype.addChangeListener = function(listener) {
	this.entityChangeListeners.push(listener);
};
		
EntityRegistry.prototype.removeChangeListener = function(listener) {
	var i = this.entityChangeListeners.indexOf(listener);	
	if(i != -1) {		
		this.entityChangeListeners.splice(i, 1);		
	}
};			

EntityRegistry.prototype.setPropertyValue = function(node, property, newValue) {
	var oldValue = (property in node.properties) ? node.properties[property] : null;	
	node.properties[property] = newValue;
		
	for (var i = 0; i < this.nodeChangeListeners.length; i++){		
		this.nodeChangeListeners[i].nodeUpdated(node, property, oldValue, newValue);
	}	
};
				
EntityRegistry.prototype.setEntityProperties = function(node, newProperties) {
	for (var property in newProperties) {
		this.setPropertyValue(node, property, newProperties[property]);
	}
	// remove anything that is not in newProperties
	for(var property in node.properties){
		if(!(property in newProperties)){
			delete node.properties[property];
		}
	}
};

