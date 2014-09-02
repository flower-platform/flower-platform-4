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
 
var NodeRegistryManager = function(resourceOperationsHandler, serviceInvocator, externalInvocator) {
	this.resourceOperationsManager = new ResourceOperationsManager(this, resourceOperationsHandler);
	this.serviceInvocator = serviceInvocator;
	this.externalInvocator = externalInvocator;
	
	this.resourceSetToNodeRegistries = {};		

	this.resourceSetToResourceUris = {};
	this.resourceUriToResourceSet = {};

	this.listeners = [];
};

NodeRegistryManager.prototype.createNodeRegistry = function() {
	return new NodeRegistry(this);
};

NodeRegistryManager.prototype.addListener = function(listener) {
	this.listeners.push(listener);
};		

NodeRegistryManager.prototype.removeListener = function(listener) {
	var i = this.listeners.indexOf(listener);	
	if (i != -1) {	
		this.listeners.splice(i, 1);		
	}
};

NodeRegistryManager.prototype.getResourceSets = function() {
	var resourceSets = [];
	for (var resourceSet in this.resourceSetToNodeRegistries) {
		resourceSets.push(resourceSet);
	}
	return resourceSets;
};

NodeRegistryManager.prototype.getNodeRegistriesForResourceSet = function(resourceSet) {	
	var nodeRegistries = this.resourceSetToNodeRegistries[resourceSet];
	if (nodeRegistries == null) {
		nodeRegistries = [];
	}	
	return nodeRegistries;
};

NodeRegistryManager.prototype.getResourceUrisForNodeRegistry = function(nodeRegistry) {
	var resourceUris = [];
	for (var resourceUri in this.resourceUriToResourceSet) {	
		var nodeRegistries = this.resourceSetToNodeRegistries[this.resourceUriToResourceSet[resourceUri]];
		if (this.contains(nodeRegistries, nodeRegistry)) {
			resourceUris.push(resourceUri);	
		}
	}
	return resourceUris;
};

NodeRegistryManager.prototype.getResourceUrisForResourceSet = function(resourceSet) {
	var resourceUris = this.resourceSetToResourceUris[resourceSet];
	if (resourceSet == null) {
		resourceUris = [];
	}
	return resourceUris;
};

NodeRegistryManager.prototype.getNodeRegistries = function() {
	var list = [];
	for (var resourceSet in this.resourceSetToNodeRegistries) {
		var nodeRegistries = this.resourceSetToNodeRegistries[resourceSet];
		for (var i=0; i < nodeRegistries.length; i++) {
			if (!this.contains(list, nodeRegistries[i])) {
				list.push(nodeRegistries[i]);	
			}
		}
	}	
	return list;
};

NodeRegistryManager.prototype.getResourceUris = function() {
	var resourceUris = [];
	for (var resourceUri in this.resourceUriToResourceSet) {
		resourceUris.push(resourceUri);
	}
	return resourceUris;
};

NodeRegistryManager.prototype.linkResourceNodeWithNodeRegistry = function(resourceUri, resourceSet, nodeRegistry) {
	// add resourceUri to resourceSet
	var resourceUris = this.resourceSetToResourceUris[resourceSet];
	if (resourceUris == null) {
		resourceUris = [];
		this.resourceSetToResourceUris[resourceSet] = resourceUris;
	}
	resourceUris.push(resourceUri);
	this.resourceUriToResourceSet[resourceUri] = resourceSet;
	
	// add resourceSet to registry
	var nodeRegistries = this.resourceSetToNodeRegistries[resourceSet];
	if (nodeRegistries == null) {
		nodeRegistries = [];
		this.resourceSetToNodeRegistries[resourceSet] = nodeRegistries;
	}	
	nodeRegistries.push(nodeRegistry);
		
	// listen for resourceNode properties modifications like isDirty
	var resourceNode = nodeRegistry.getNodeById(resourceUri);
	this.externalInvocator.addEventListener(resourceNode.properties, "propertyChange", this.resourceNodeUpdated);
	this.resourceNodeUpdated(this.externalInvocator.createUpdateEvent(resourceNode, Constants.IS_DIRTY, false, resourceNode.properties[Constants.IS_DIRTY]));
};

NodeRegistryManager.prototype.unlinkResourceNodeFromNodeRegistry = function(resourceUri, nodeRegistry) {
	// change isDirty to false and dispatch event
	var resourceNodeFromRegistry = nodeRegistry.getNodeById(resourceUri);
	nodeRegistry.setPropertyValue(resourceNodeFromRegistry, Constants.IS_DIRTY, false);
	
	this.externalInvocator.removeEventListener(resourceNodeFromRegistry, "propertyChange", this.resourceNodeUpdated);
	
	var resourceSet = this.resourceUriToResourceSet[resourceUri];
	
	// remove resourceUri from resourceSet
	var resourceUris = this.resourceSetToResourceUris[resourceSet];
	if (resourceUris != null) {
		resourceUris.splice(resourceUris.indexOf(resourceUri), 1);
		if (resourceUris.length == 0) {
			delete this.resourceSetToResourceUris[resourceSet];
		}
	}
	
	// remove resourceUri from registry
	resourceUris = this.getResourceUrisForNodeRegistry(nodeRegistry);
	if (resourceUris != null) {		
		resourceUris.splice(resourceUris.indexOf(resourceUri), 1);			
		if (resourceNodeFromRegistry.nodeUri != nodeRegistry.getRootNodeUri() && 
			resourceNodeFromRegistry.parent == null && resourceUris.indexOf(resourceUri) < 0) {
			nodeRegistry.unregisterNode(resourceNodeFromRegistry);
		}
	}
	
	// remove resourceSet from registry
	var nodeRegistries = this.resourceSetToNodeRegistries[resourceSet];
	if (nodeRegistries != null) {
		nodeRegistries.splice(nodeRegistries.indexOf(nodeRegistry), 1);
		if (nodeRegistries.length == 0 && this.resourceSetToResourceUris[resourceSet] == null) {			
			delete this.resourceSetToNodeRegistries[resourceSet];
			delete this.resourceUriToResourceSet[resourceUri];		
		}
	}	
};

NodeRegistryManager.prototype.expand = function(nodeRegistry, node, context) {
	if (node == null || !node.properties[Constants.AUTO_SUBSCRIBE_ON_EXPAND]) {
		nodeRegistry.expand(node, context);
	} else {
		var subscribableResources = node == null ? null : node.properties[Constants.SUBSCRIBABLE_RESOURCES];
		if (subscribableResources != null && subscribableResources.length > 0) {
			// a subscribable node => subscribe to the first resource
			var subscribableResource = subscribableResources.getItemAt(0);
			this.subscribe(subscribableResource.a, nodeRegistry, function(rootNode, resourceNode) {
				nodeRegistry.expand(node, context);
			});
		}
	}
};

NodeRegistryManager.prototype.collapse = function(nodeRegistry, node, refreshChildren) {
	// get all the resources starting from node
	var dirtyResourceUris = [];
	var savedResourceUris = [];
	
	this.getResourceUrisForSubTree(node, nodeRegistry, dirtyResourceUris, savedResourceUris);
	
	if (dirtyResourceUris.length > 0) { // at least one dirty resourceNode found -> show dialog			
		this.resourceOperationsManager.showSaveDialog([this], this.getResourceSetsForResourceUris(dirtyResourceUris), 
			function() {
				// wait for server response before collapse	
				this.collapseHandler(node, nodeRegistry, refreshChildren, dirtyResourceUris, savedResourceUris);
			}
		);
	} else {
		this.collapseHandler(node, nodeRegistry, refreshChildren, dirtyResourceUris, savedResourceUris);
	}
};

NodeRegistryManager.prototype.collapseHandler = function(node, nodeRegistry, refreshChildren, dirtyResourceUris, savedResourceUris) {
	for (var i=0; i < dirtyResourceUris.length; i++) {	
		this.unlinkResourceNodeFromNodeRegistry(dirtyResourceUris[i], nodeRegistry);
	}
	for (var i=0; i < savedResourceUris.length; i++) {	
		this.unlinkResourceNodeFromNodeRegistry(savedResourceUris[i], nodeRegistry);
	}
	nodeRegistry.collapse(node, refreshChildren);
};

/**
 * Iterate the subtree starting from <code>node</code> and add all the resources to the dirty or saved resource arrays.
 */
NodeRegistryManager.prototype.getResourceUrisForSubTree = function(node, nodeRegistry, dirtyResourceUris, savedResourceUris) {
	var subscribableResources = node.properties[Constants.SUBSCRIBABLE_RESOURCES];
	if (subscribableResources != null) {	
		for (var i = 0; i < subscribableResources.length; i++){
			var resourceUri = subscribableResources.getItemAt(i).a;				
			var resourceNode = nodeRegistry.getNodeById(resourceUri);
			if (resourceNode != null) {
				if (resourceNode.properties[Constants.IS_DIRTY]) {
					dirtyResourceUris.push(resourceUri);
				} else {
					savedResourceUris.push(resourceUri);
				}
			}
		}
	}
	
	// recurse
	if (node.children != null) {
		for (var i = 0; i < node.children.length; i++){	
			this.getResourceUrisForSubTree(node.children.getItemAt(i), nodeRegistry, dirtyResourceUris, savedResourceUris);
		}
	}
};

NodeRegistryManager.prototype.hasSubscribableResource = function(node, resourceUri) {
	var subscribableResources = node.properties[Constants.SUBSCRIBABLE_RESOURCES];
	if (subscribableResources == null || subscribableResources.length == 0) {
		return false;
	}
	for (var i = 0; i < subscribableResources.length; i++) {		
		if (subscribableResources.getItemAt[i].a == resourceUri) {
			return true;
		}
	}
	return false;
};

/**
 * Calls subscribe for <code>nodeId</code> on server. Callback functions:
 * <ul>
 * 	<li><code>subscribeResultCallback(resourceNode:Node):void</code>
 * 	<li><code>subscribeFaultCallback(event:FaultEvent):void</code>
 * </ul>
 */ 
NodeRegistryManager.prototype.subscribe = function(nodeId, nodeRegistry, subscribeResultCallback, subscribeFaultCallback) {
	var self = this;
	this.serviceInvocator.invoke("resourceService.subscribeToParentResource", [nodeId], 
		function (subscriptionInfo) {			
			subscriptionInfo.rootNode = nodeRegistry.mergeOrRegisterNode(subscriptionInfo.rootNode);
			if (subscriptionInfo.resourceNode != null) {
				subscriptionInfo.resourceNode = nodeRegistry.mergeOrRegisterNode(subscriptionInfo.resourceNode);
				self.linkResourceNodeWithNodeRegistry(subscriptionInfo.resourceNode.nodeUri, subscriptionInfo.resourceSet, nodeRegistry);
			}			
			if (subscribeResultCallback != null) {
				subscribeResultCallback.call(null, subscriptionInfo.rootNode, subscriptionInfo.resourceNode);
			}
		},
		function(fault) {
			self.externalInvocator.showMessageBox("editor.error.subscribe.title", "editor.error.subscribe.message", [fault.faultString]);
					
			if (subscribeFaultCallback != null) {
				subscribeFaultCallback.call(null, fault);
			}
		});
};

/**
 * Closes all editors without dispatching events and updates global state for save actions.
 */ 
NodeRegistryManager.prototype.removeNodeRegistries = function(nodeRegistries) {			
	for (var i = 0; i < nodeRegistries.length; i++) {
		var nodeRegistry = nodeRegistries[i];
		var resourceUris = this.getResourceUrisForNodeRegistry(nodeRegistry);
		for (var j=0; j < resourceUris.length; j++) {		
			this.unlinkResourceNodeFromNodeRegistry(resourceUris[j], nodeRegistry);
		}		
		for (var j=0; j < this.listeners.length; j++) {
			this.listeners[j].nodeRegistryRemoved(nodeRegistry);
		}		
	}
	
	this.resourceOperationsManager.resourceOperationsHandler.updateGlobalDirtyState(false);
};

NodeRegistryManager.prototype.getResourceSetsForResourceUris = function(resourceUris) {
	var resourceSets = [];
	for (var i = 0; i < resourceUris.length; i++) {	
		var resourceSet = this.resourceUriToResourceSet[resourceUris[i]];
		if (resourceSets.indexOf(resourceSet) < 0) {
			resourceSets.push(resourceSet);
		}
	}
	return resourceSets;
};

/**
 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
 * @return all dirty resourceUris found in <code>nodeRegistries</code>, without duplicates.
 */ 
NodeRegistryManager.prototype.getDirtyResourceSetsFromNodeRegistries = function(nodeRegistries, dirtyResourceNodeHandler) {			
	var dirtyResourceSets = [];
	for (var i = 0; i < nodeRegistries.length; i++) {	
		var nodeRegistry = nodeRegistries[i];
		var resourceUris = this.getResourceUrisForNodeRegistry(nodeRegistry);
		for (var j=0; j < resourceUris.length; j++) {		
			var resourceUri = resourceUris[j];
			var resourceSet = this.resourceUriToResourceSet[resourceUri];
			if (this.isResourceNodeDirty(resourceUri, nodeRegistry) && dirtyResourceSets.indexOf(resourceSet) == -1) {
				if (dirtyResourceNodeHandler != null) {
					dirtyResourceNodeHandler(resourceSet);
				}
				dirtyResourceSets.push(resourceSet);						
			}
		}
	}
	return dirtyResourceSets;
};

/**
 * @param returnIfAtLeastOneDirtyResourceNodeFound if <code>true</code>, returns the first dirty resourceNodeId found.
 * @param dirtyResourceNodeHandler function will be executed each time a dirty resourceNode is found.
 * @return all dirty resourceUris, without duplicates.
 */ 
NodeRegistryManager.prototype.getAllDirtyResourceSets = function(returnIfAtLeastOneDirtyResourceNodeFound, dirtyResourceNodeHandler) {
	var dirtyResourceSets = [];
	var nodeRegistries = this.getNodeRegistries();
	for (var i = 0; i < nodeRegistries.length; i++) {	
		var resourceUris = this.getResourceUrisForNodeRegistry(nodeRegistries[i]);
		for (var j = 0; j < resourceUris.length; j++) {
			var resourceSet = this.resourceUriToResourceSet[resourceUris[j]];
			if (this.isResourceNodeDirty(resourceUris[j], nodeRegistries[i]) && dirtyResourceSets.indexOf(resourceSet) == -1) {
				if (returnIfAtLeastOneDirtyResourceNodeFound) {
					return [resourceSet];
				}											
				if (dirtyResourceNodeHandler != null) {
					dirtyResourceNodeHandler(resourceSet);
				}
				dirtyResourceSets.push(resourceSet);						
			}
		}		
	}
	return dirtyResourceSets;
};
		
NodeRegistryManager.prototype.processUpdates = function(resourceNodeIdToUpdates) {
	for (var resourceNodeId in resourceNodeIdToUpdates) {
		var updates = resourceNodeIdToUpdates[resourceNodeId];			
		var nodeRegistries = this.getNodeRegistriesForResourceSet(resourceNodeId);
		for (var i = 0; i < nodeRegistries.length; i++) {
			nodeRegistries[i].processUpdates(updates);
		}
	}
};
		
/**
 * Unlink <code>resourceUris</code> from node registries.
 * 
 * <p>
 * Dispatches <code>resourceNodeRemoved</code> for each <code>NodeRegistry</code> found.
 * Note: Additional behavior can be added by listening to this event (e.g. in case of an editor, close it).
 */
NodeRegistryManager.prototype.unlinkResourceNodesForcefully = function(resourceUris) {
	var idsList = "";
	for (var i = 0; i < resourceUris.length; i++) {	
		var nodeRegistries = this.getNodeRegistriesForResourceSet(resourceUris[i]);
		for (var j = 0; j < nodeRegistries.length; j++) {					
			this.unlinkResourceNodeFromNodeRegistry(resourceUris[i], nodeRegistries[j]);
			for (var k = 0; k < this.listeners.length; k++) {			
				this.listeners[k].resourceNodeRemoved(resourceUris[i], nodeRegistries[j]);
			}
		}
		idsList += "\n* " + resourceUris[i];
	}
	this.externalInvocator.showMessageBox("editor.error.subscribe.title", "editor.error.subscribe.message", [idsList]);
};

NodeRegistryManager.prototype.isResourceNodeDirty = function(resourceNodeId, nodeRegistry) {
	var node = nodeRegistry.getNodeById(resourceNodeId);	
	return node == null ? false : node.properties[Constants.IS_DIRTY];
};
			
NodeRegistryManager.prototype.resourceNodeUpdated = function(event) {		
	if (event.property == Constants.IS_DIRTY) {				
		_nodeRegistryManager.resourceOperationsManager.resourceOperationsHandler.updateGlobalDirtyState(event.newValue);
	}
};

NodeRegistryManager.prototype.contains = function(list, obj) {
	for (var i=0; i < list.length; i++) {
		if (list[i] == obj) {
			return true;
		}
	}	
	return false;
};

var Constants = {IS_DIRTY: "isDirty", SUBSCRIBABLE_RESOURCES: "subscribableResources", AUTO_SUBSCRIBE_ON_EXPAND: "autoSubscribeOnExpand"};

var _nodeRegistryManager;