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
	// key = notificationChannel; value = EntityRegistryEntry
	this.entityRegistryEntries = {};
	// key = update type; value = UpdateProcessor
	this.diffUpdateProcessors = {};
	this.listeners = [];
};

EntityRegistryManager.prototype.createEntityRegistry = function(notificationChannel) {
	var entityRegistry = new EntityRegistry(this);
	entityRegistry.notificationChannel = notificationChannel;
	var entityRegistryEntry = this.entityRegistryEntries[notificationChannel];
	if (!entityRegistryEntry) {
		entityRegistryEntry = new EntityRegistryEntry();
		entityRegistryEntry.notificationChannel = notificationChannel;
		this.entityRegistryEntries[notificationChannel] = entityRegistryEntry;
	}
	entityRegistryEntry.entityRegistries.push(entityRegistry);
	
	return entityRegistry;
};

EntityRegistryManager.prototype.removeEntityRegistry = function(entityRegistry) {
	var entityRegistryEntry = this.entityRegistryEntries[entityRegistry.notificationChannel];
	if (!entityRegistryEntry) {
		throw "notificationChannel not found: " + entityRegistry.notificationChannel;
	}
	var found = false;
	for (var i = 0; i < entityRegistryEntry.entityRegistries.length; i++) {
		if (entityRegistryEntry.entityRegistries[i] == entityRegistry) {
			// found
			found = true;
			entityRegistryEntry.entityRegistries.splice(i, 1);
			break;
		}
	} 
	if (!found) {
		throw "entityRegistry not found: " + entityRegistry;
	}
	if (entityRegistryEntry.entityRegistries.length == 0) {
		delete this.entityRegistryEntries[entityRegistry.notificationChannel];
	}
	// TODO CS/DU: vad ca: 1) ER are handlers; avem deci mem leak aici ca nu dezirengistram? 2) vad ca are si ref catre adapter. nu mai bine tinem ac. ref doar in ERM
};

// TODO CS: de sters la sfarsit
EntityRegistryManager.prototype.getNotificationChannelsData = function() {
	var notificationChannels = [];
	for (var notificationChannel in this.entityRegistryEntries) {
		var channelData = {};
		channelData.notificationChannel = notificationChannel;
		channelData.lastDiffUpdateId = this.entityRegistryEntries[notificationChannel].lastDiffUpdateId;
		notificationChannels.push(channelData);
	}
	return notificationChannels;
};

EntityRegistryManager.prototype.getEntityRegistriesForNotificationChannel = function(notificationChannel) {	
	var entityRegistryEntry = this.entityRegistryEntries[notificationChannel];
	if (!entityRegistryEntry) {
		return null;
	}	
	return entityRegistryEntry.entityRegistries;
};

EntityRegistryManager.prototype.addDiffUpdateProcessor = function(diffUpdateType, diffUpdateProcessor) {
	this.diffUpdateProcessors[diffUpdateType] = diffUpdateProcessor;
};

EntityRegistryManager.prototype.processDiffUpdate = function(notificationChannel, diffUpdate) {
	var diffUpdateProcessor = this.diffUpdateProcessors[diffUpdate.type];
	if (!diffUpdateProcessor) {
		throw "Update processor for type " + diffUpdate.type + " is not registered.";
	}
	var entityRegistryEntry = this.entityRegistryEntries[notificationChannel];
	if (diffUpdate.id <= entityRegistryEntry.lastDiffUpdateId) {
		// ignore if update was already processed (this case may appear when update timer event occurs at the same time with service method call)
		return;
	} else if (entityRegistryEntry.lastDiffUpdateId != -1 && diffUpdate.id > entityRegistryEntry.lastDiffUpdateId + 1) {
		// throw exception if any updates were skipped 
		throw "Update id (" + diffUpdate.id + ") is too high. Last processed update id is " + entityRegistryEntry.lastDiffUpdateId + ".";
	}
	
	for (var i in entityRegistryEntry.entityRegistries) {
		var entityRegistry = entityRegistryEntry.entityRegistries[i];
		diffUpdateProcessor.applyDiffUpdate(entityRegistry, diffUpdate);
	}
	entityRegistryEntry.lastDiffUpdateId = diffUpdate.id;
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

var EntityRegistryEntry = function() {
	this.entityRegistries = [];
	this.lastDiffUpdateId = -1;
}

var Constants = { UPDATED: "updated", ADDED: "added", REMOVED: "removed", EMPTY: "empty", REQUEST_REFRESH: "requestRefresh", INITIAL_INFO: "initialInfo" };
