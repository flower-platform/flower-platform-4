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
  
var NodeRegistry = function(nodeRegistryManager) { 
	this.nodeRegistryManager = nodeRegistryManager;
	this.nodeChangeListeners = [];
	this.rootNodeUri = null;
	this.registry = {};
};

NodeRegistry.prototype.getRootNodeUri = function() {
	return this.rootNodeUri;
};
		
NodeRegistry.prototype.getNodeById = function(id) {
	if (id in this.registry) {
		return this.registry[id];
	}
	return this.registry[id];
};
		
NodeRegistry.prototype.addNodeChangeListener = function(listener) {
	this.nodeChangeListeners.push(listener);
};
		
NodeRegistry.prototype.removeNodeChangeListener = function(listener) {
	var i = this.nodeChangeListeners.indexOf(listener);	
	if(i != -1) {		
		this.nodeChangeListeners.splice(i, 1);		
	}
};			

NodeRegistry.prototype.unregisterNode = function(node, parent) {
	if (parent != null && (parent.children == null || !this.nodeRegistryManager.hostInvocator.contains(parent.children, node))) {
		// not expanded or child already removed
		return;
	}
	// remove children recursive
	this.collapse(node, false);
	
	var nodeFromRegistry = this.registry[node.nodeUri];
	if (nodeFromRegistry != null) {
		delete this.registry[node.nodeUri];
	}
			
	if (parent != null) {
		// remove from parent list of children
		this.nodeRegistryManager.hostInvocator.removeItemAt(parent.children, this.nodeRegistryManager.hostInvocator.getItemIndex(parent.children, node));		
		if (this.nodeRegistryManager.hostInvocator.getLength(parent.children) == 0) { // parent has no children left -> parent is leaf
			parent.children = null;
		}
	}
	for (var i = 0; i < this.nodeChangeListeners.length; i++){
		this.nodeChangeListeners[i].nodeRemoved(node);
	}
};

NodeRegistry.prototype.collapse = function(node) {
	if (!(node.nodeUri in this.registry)) {
		return;
	}
	if (node.children == null) {
		return;
	}
	while (node.children != null) {					
		this.unregisterNode(this.nodeRegistryManager.hostInvocator.getItemAt(node.children, 0), node);
	}			
};

NodeRegistry.prototype.expand = function(node, context) {	
	if (!(node.nodeUri in this.registry)) {		
		return;
	}
	
	var serviceContext = this.nodeRegistryManager.hostInvocator.createMapInstance();
	if (context != null) {
		for (var key in context) {
			this.nodeRegistryManager.hostInvocator.addInMap(serviceContext, key, context[key]);			
		}
	}
	this.nodeRegistryManager.hostInvocator.addInMap(serviceContext, "populateWithProperties", true);
	
	var self = this;			
	this.nodeRegistryManager.hostServiceInvocator.invoke(
		"nodeService.getChildren", 
		[node.nodeUri, serviceContext], 
		function (result) {
			self.expandCallbackHandler(node, result); 
			
			// additional handler to be executed
			if (context != null && ("handler" in context)) {
				context["handler"]();
			}
		});		
};

/**
 * Handles updates received from server.
 * 
 * There are 2 cases:
 * <ul>
 * 	<li> updates == null (server cannot provide any updates because no updates were registered before timestampOfLastRequest), a full refresh is needed </li>
 * 	<li> otherwise
 * 		<ul>
 * 			<li> property update (PropertyUpdate) -> set/unset property from <code>Node.properties</code> <br>
 * 				A <code>NodeUpdatedEvent</code> is dispatched at the end; it contains all updated & removed properties keys.
 * 			<li> child added (ChildrenUpdate.ADDED) -> adds new node in parent as last child OR, if <code>targetNodeAddedBefore</code> set, before given child's index.
 * 			<li> child removed (ChildrenUpdate.REMOVED) -> removes node from parent
 * 		</ul>
 * </ul>
 * 
 * Note: If a given <code>fullNodeId</code> doesn't exist in <code>nodeRegistry</code>, it means it isn't visible for current client, so ignore it.		
 */ 
NodeRegistry.prototype.processUpdates = function(updates) {			
	if (updates == null) {
		// refresh all from root node (node.parent == null)
		for (var fullNodeId in this.registry) {
			var node = this.registry[fullNodeId];
			if (node.parent == null) {
				this.refresh(node);
			}
		}
		return;
	}
			
	for (var i = updates.length - 1; i >= 0; i--) {
		var update = updates.getItemAt(i);
		var nodeFromRegistry = this.getNodeById(update.fullNodeId);	
		if (nodeFromRegistry == null) { // node not registered, probably it isn't visible for this client
			continue;
		}
		
		switch (String(update.type)) {
			case Constants.UPDATED:
				var propertyUpdate = update;
				
				if (propertyUpdate.isUnset) {
					delete this.nodeFromRegistry.properties[propertyUpdate.key];						
				} else {
					this.setPropertyValue(nodeFromRegistry, propertyUpdate.key, propertyUpdate.value);						
				}	
				break;
			case Constants.ADDED:
				var targetNodeInRegistry = this.getNodeById(update.targetNode.nodeUri);	
				
				if (nodeFromRegistry.children != null && !this.nodeRegistryManager.hostInvocator.contains(nodeFromRegistry.children, targetNodeInRegistry)) {
					var index = -1; // -> add it last
					if (update.fullTargetNodeAddedBeforeId != null) {
						// get targetNodeAddedBefore from registry 
						var targetNodeAddedBeforeInRegistry = this.getNodeById(update.fullTargetNodeAddedBeforeId);
						if (targetNodeAddedBeforeInRegistry != null) { // exists, get its index in children list
							index = this.nodeRegistryManager.hostInvocator.getItemIndex(nodeFromRegistry.children, targetNodeAddedBeforeInRegistry);	
						}
					}								
					this.registerNode(update.targetNode, nodeFromRegistry, index);								
				} else {
					// child already added, probably after refresh
					// e.g. I add a children, I expand => I get the list with the new children; when the
					// client polls for data, this children will be received as well, and thus duplicated.
					// NOTE: since the instant notifications for the client that executed => this doesn't apply
					// for him; but for other clients yes
							
					// Nothing to do								
				}		
				break;
			case Constants.REMOVED:
				var targetNodeInRegistry = this.getNodeById(update.targetNode.nodeUri);	
				
				if (targetNodeInRegistry != null) {
					this.unregisterNode(targetNodeInRegistry, nodeFromRegistry);								
				} else {
					// node not registered, probably it isn't visible for this client
					// Nothing to do
				}
				break;
			case Constants.REQUEST_REFRESH:
				this.refresh(nodeFromRegistry);
				break;
			default:
				update.apply(this, nodeFromRegistry);		
		}						
	}			
};

NodeRegistry.prototype.expandCallbackHandler = function(node, children) {	
	if (children == null) {
		return;
	}		

	// register each child
	for (var i = 0; i < this.nodeRegistryManager.hostInvocator.getLength(children); i++) {	
		this.registerNode(this.nodeRegistryManager.hostInvocator.getItemAt(children, i), node, -1);
	}
};

NodeRegistry.prototype.refresh = function(node) {		
	if (!(node.nodeUri in this.registry)) {
		return;
	}
	var self = this;
	this.nodeRegistryManager.hostServiceInvocator.invoke(
		"nodeService.refresh", 
		[this.getFullNodeIdWithChildren(node)], 
		function (result) {
			self.refreshHandler(node, result);
		});
};

/**
 * @return an hierarchical structure of <code>fullNodeId</code>s starting from <code>node</code>.
 */ 
NodeRegistry.prototype.getFullNodeIdWithChildren = function(node) {
	var fullNodeIdWithChildren = this.nodeRegistryManager.hostInvocator.createFullNodeIdWithChildrenInstance();
	fullNodeIdWithChildren.fullNodeId = node.nodeUri;
	
	if (node.children != null) {
		for (var i = 0; i < this.nodeRegistryManager.hostInvocator.getLength(node.children); i++) {
			if (fullNodeIdWithChildren.visibleChildren == null) {
				fullNodeIdWithChildren.visibleChildren = this.nodeRegistryManager.hostInvocator.createListInstance();
			}
			this.nodeRegistryManager.hostInvocator.addItem(fullNodeIdWithChildren.visibleChildren, this.getFullNodeIdWithChildren(this.nodeRegistryManager.hostInvocator.getItemAt(node.children, i)));
		}
	}
	return fullNodeIdWithChildren;
};

/**
 * Handles refresh response from server.
 * 
 * For each node:
 * <ul>
 * 	<li> re-set its properties and dispatch a <code>NodeUpdateEvent</code> without specifying the updated & removed properties keys
 * 	<li> if no new children, remove all current node's children
 *  <li> else, iterate on current list
 * 		<ul>
 * 			<li> if current child id exists in new list -> get its new structure
 * 			<li> else -> remove current child
 * 		</ul>
 * 	<li> at this point, the current list will contain only the children that haven't been removed
 *  <li> next, iterate on new list, there are 2 cases:
 * 		<ul>
 * 			<li> child added -> add new child in current list at the same index
 * 			<li> child moved to a different index -> remove child from current index, add it to new index
 * 		</ul>
 * </ul>
 */ 
NodeRegistry.prototype.refreshHandler = function(node, nodeWithVisibleChildren) {
	// set new node properties and dispatch event			
	var nodeFromRegistry = this.getNodeById(node.nodeUri);
	this.setNodeProperties(nodeFromRegistry, nodeWithVisibleChildren.node.properties);
				
	var newNodeToCurrentNodeIndex = [];
	var i, j;
	var currentChildNode;
	
	// no children -> remove the old ones
	if (nodeWithVisibleChildren.children == null) {
		this.collapse(node, false);
		return;
	}
	
	if (node.children != null) { // node has children -> merge current list with new list
		// first make a copy of the existing children, because this list will be altered during merging
		var currentChildren = this.nodeRegistryManager.hostInvocator.createListInstance();
		for (i = 0; i < this.nodeRegistryManager.hostInvocator.getLength(node.children); i++) {
			this.nodeRegistryManager.hostInvocator.addItem(currentChildren, 
					this.nodeRegistryManager.hostInvocator.getItemAt(node.children, i));
		}
		// search for children that don't exist in new list
		for (i = 0; i < this.nodeRegistryManager.hostInvocator.getLength(currentChildren); i++) {
			var exists = false;
			currentChildNode = this.nodeRegistryManager.hostInvocator.getItemAt(currentChildren, i);
			for (j = 0; j < this.nodeRegistryManager.hostInvocator.getLength(nodeWithVisibleChildren.children); j++) {
				var newChildWithVisibleChildren = this.nodeRegistryManager.hostInvocator.getItemAt(nodeWithVisibleChildren.children, j);
				if (currentChildNode.nodeUri == newChildWithVisibleChildren.node.nodeUri) {
					exists = true;
					break;
				}
			}
			if (exists) {
				// child exists -> refresh its structure
				this.refreshHandler(currentChildNode, newChildWithVisibleChildren);
				// store, for the new child, its index in current list
				newNodeToCurrentNodeIndex[newChildWithVisibleChildren.node.nodeUri] = i;
			} else {
				// child doesn't exist in new list -> remove it from parent
				this.unregisterNode(currentChildNode, node);
			}
		}
	}
	
	for (i = 0; i < this.nodeRegistryManager.hostInvocator.getLength(nodeWithVisibleChildren.children); i++) {	
		var newChildNode = this.nodeRegistryManager.hostInvocator.getItemAt(nodeWithVisibleChildren.children, i).node;
		if (!(newChildNode.nodeUri in newNodeToCurrentNodeIndex)) { // new child doesn't exist in current list -> add it
			this.registerNode(newChildNode, node, i);
		} else if (newNodeToCurrentNodeIndex[newChildNode.nodeUri] != i) { // new child exists in current list, but different indexes -> get current child and move it to new index
			currentChildNode = this.getNodeById(newChildNode.nodeUri);
			this.unregisterNode(currentChildNode, node);
			this.registerNode(currentChildNode, node, i);
		}
	}
};

NodeRegistry.prototype.mergeOrRegisterNode = function(node) {
	var existingNode = this.getNodeById(node.nodeUri);
	if (existingNode == null) {
		this.registerNode(node, null, -1);
		return node;
	}	
	this.setNodeProperties(existingNode, node.properties);
	
	return existingNode;
};

/**
 * Adds <code>node</code> to <code>parent</code> at given index and registers it in registry. <br>
 * 
 * If <code>parent</code> is null, the node will only be registered
 * If <code>index</code> is -1, the node will be added last.
 */
NodeRegistry.prototype.registerNode = function(node, parent, index) {
	if (this.rootNodeUri == null) {		
		this.rootNodeUri = node.nodeUri;
	}
	
	this.registry[node.nodeUri] = node;
	
	if (parent != null) {
		node.parent = parent;
		if (parent.children == null) {
			parent.children = this.nodeRegistryManager.hostInvocator.createListInstance();
		}						
		if (index != -1) {
			this.nodeRegistryManager.hostInvocator.addItemAt(parent.children, node, index);
		} else {		
			this.nodeRegistryManager.hostInvocator.addItem(parent.children, node);
		}
	}
	
	for (var i = 0; i < this.nodeChangeListeners.length; i++){
		this.nodeChangeListeners[i].nodeAdded(node);
	}
	
};

NodeRegistry.prototype.setPropertyValue = function(node, property, newValue) {
	var oldValue = (property in node.properties) ? node.properties[property] : null;	
	node.properties[property] = newValue;
		
	for (var i = 0; i < this.nodeChangeListeners.length; i++){		
		this.nodeChangeListeners[i].nodeUpdated(node, property, oldValue, newValue);
	}	
};
				
NodeRegistry.prototype.setNodeProperties = function(node, newProperties) {
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

