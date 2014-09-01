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
 
var ResourceOperationsManager = function(nodeRegistryManager, resourceOperationsHandler) {
	this.nodeRegistryManager = nodeRegistryManager;
	this.resourceOperationsHandler = resourceOperationsHandler;
	this.resourceOperationsHandler.nodeRegistryManager = nodeRegistryManager;
};

ResourceOperationsManager.prototype.showSaveDialog = function(nodeRegistries, dirtyResourceNodeIds, handler) {
	if (nodeRegistries == null) {
		nodeRegistries = this.nodeRegistryManager.getNodeRegistries();
	}
	
	var dirtyResourceNodes = this.nodeRegistryManager.externalInvocator.getNewListInstance();
	if (dirtyResourceNodeIds == null) {
		this.nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries(nodeRegistries, function(dirtyResourceNodeId) {
			dirtyResourceNodes.addItem({resourceNodeId: dirtyResourceNodeId, selected: true});
		});				
	} else {
		for (var i = 0; i < dirtyResourceNodeIds.length; i++) {
			dirtyResourceNodes.addItem({resourceNodeId: dirtyResourceNodeIds[i], selected: true});
		}
	}	
	if (dirtyResourceNodes.length == 0) {
		if (handler != null) {
			handler();
		} else {			
			this.nodeRegistryManager.removeNodeRegistries(nodeRegistries);
		}
		return;
	}
	this.resourceOperationsHandler.showSaveDialog(nodeRegistries, dirtyResourceNodes, handler);
};

ResourceOperationsManager.prototype.showReloadDialog = function(nodeRegistries, resourceSets) {
	if (nodeRegistries == null) {
		nodeRegistries = this.nodeRegistryManager.getNodeRegistries();
	}
	if (resourceSets == null) {
		resourceSets = this.nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries(nodeRegistries);
	}
	this.resourceOperationsHandler.showReloadDialog(nodeRegistries, resourceSets);
};

ResourceOperationsManager.prototype.save = function(nodeRegistry) {
	var dirtyResourceNodeIds = this.nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries([nodeRegistry]);
	if (dirtyResourceNodeIds.length == 1) { 
		// single resourceNode to save -> save without asking
		this.nodeRegistryManager.serviceInvocator.invoke("resourceService.save", [dirtyResourceNodeIds[0]]);
	} else { 
		// multiple resourceNodes to save -> show dialog
		this.showSaveDialog([nodeRegistry], dirtyResourceNodeIds, function() {});
	}
};

ResourceOperationsManager.prototype.saveAll = function() {
	var self = this;
	this.nodeRegistryManager.getAllDirtyResourceSets(false, function(dirtyResourceNodeId) {
		// for each dirty resourceNode found -> save it
		self.nodeRegistryManager.serviceInvocator.invoke("resourceService.save", [dirtyResourceNodeId]);
	});
};

ResourceOperationsManager.prototype.reload = function(nodeRegistry) {
	var resourceSets = this.nodeRegistryManager.getDirtyResourceSetsFromNodeRegistries([nodeRegistry]);
	if (resourceSets.length == 1) {
		// single resourceNode to reload -> reload without asking
		this.nodeRegistryManager.serviceInvocator.invoke("resourceService.reload", [resourceSets[0]]);
	} else {
		// multiple resourceNodes -> show dialog
		this.showReloadDialog([nodeRegistry], resourceSets);
	}
};