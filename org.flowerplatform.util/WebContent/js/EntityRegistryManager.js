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

var EntityRegistryManager = function(entityOperationsAdapter) {
	this.entityOperationsAdapter = entityOperationsAdapter;
	this.entityRegistryEntries = {};
	this.diffUpdateProcessors = {};
	this.listeners = [];
};

EntityRegistryManager.prototype.createEntityRegistry = function(notificationChannel) {
	var entityRegistry = new EntityRegistry(this);
	var entityRegistryEntry = this.entityRegistryEntries[notificationChannel];
	if (!entityRegistryEntry) {
		entityRegistryEntry = new EntityRegistryEntry;
		this.entityRegistryEntries[notificationChannel] = entityRegistryEntry;
	}
	entityRegistryEntry.entityRegistries.push(entityRegistry);
	
	return entityRegistry;
};

EntityRegistryManager.prototype.getNotificationChannels = function() {
	var notificationChannels = [];
	for (var notificationChannel in this.entityRegistryEntries) {
		notificationChannels.push(notificationChannel);
	}
	return notificationChannels;
};

EntityRegistryManager.prototype.getEntityRegistriesForNotificationChannel = function(notificationChannel) {	
	var entityRegistryEntry = this.entityRegistryEntries[notificationChannel];
	if (!entityRegistriesEntry) {
		return null;
	}	
	return entityRegistryEntry.entityRegistries;
};

EntityRegistryManager.prototype.addDiffUpdateProcessor = function(diffUpdateType, diffUpdateProcessor) {
	this.diffUpdateProcessors[diffUpdateType] = diffUpdateProcessor;
};

EntityRegistryManager.prototype.processDiffUpdate = function(notificationChannel, diffUpdate) {
	var diffUpdateProcessor = this.diffUpdateProcessors[diffUpdate.type];
	var entityRegistryEntry = this.entityRegistryEntries[notificationChannel];
	for (var i in entityRegistryEntry.entityRegistries) {
		var entityRegistry = entityRegistryEntry.entityRegistries[i];
		diffUpdateProcessor.applyDiffUpdate(entityRegistry, diffUpdate);
	}
	if (diffUpdate.id > entityRegistryEntry.lastDiffUpdateId) {
		entityRegistryEntry.lastDiffUpdateId = diffUpdate.id;
	}
	
};

EntityRegistryManager.prototype.addListener = function(listener) {
	this.listeners.push(listener);
};		

EntityRegistryManager.prototype.removeListener = function(listener) {
	var i = this.listeners.indexOf(listener);	
	if (i != -1) {	
		this.listeners.splice(i, 1);		
	}
};


EntityRegistryEntry = function() {
	this.entityRegistries = [];
	this.lastDiffUpdateId = -1;
}

var _entityRegistryManager;

var Constants = { UPDATED: "UPDATED", ADDED: "ADDED", REMOVED: "REMOVED", REQUEST_REFRESH: "REQUEST_REFRESH"};
