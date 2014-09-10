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
	if(id in this.registry) {
		return this.registry[id];
	}
	return null;
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
	if (parent != null && (parent.children == null || !parent.children.contains(node))) {
		// not expanded or child already removed
		return;
	}
	// remove children recursive
	this.collapse(node);
	
	if (node.nodeUri in this.registry) {
		delete this.registry[node.nodeUri];
	}
			
	if (parent != null) {
		// remove from parent list of children
		parent.children.removeItemAt(parent.children.getItemIndex(node));				
		if (parent.children.length == 0) { // parent has no children left -> parent is leaf
			parent.children = null;
		}
	}
	for (var i=0; i < this.nodeChangeListeners.length; i++){
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
		this.unregisterNode(node.children.getItemAt(0), node);
	}			
};

NodeRegistry.prototype.expand = function(node, context) {		
	if (!(node.nodeUri in this.registry)) {
		return;
	}
	
	var serviceContext = this.nodeRegistryManager.externalInvocator.getServiceContextInstance();
	if (context != null) {
		for (var key in context) {
			serviceContext.context[key] = context[key];
		}
	}
	serviceContext.add("populateWithProperties", true);
	
	var self = this;			
	this.nodeRegistryManager.serviceInvocator.invoke(
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
		
		switch (update.type) {
			case "UPDATED":
				var propertyUpdate = update;
				
				if (propertyUpdate.isUnset) {
					delete this.nodeFromRegistry.properties[propertyUpdate.key];						
				} else {
					this.setPropertyValue(nodeFromRegistry, propertyUpdate.key, propertyUpdate.value);						
				}	
				break;
			case "ADDED":
				var targetNodeInRegistry = this.getNodeById(update.targetNode.nodeUri);	
				
				if (nodeFromRegistry.children != null && !nodeFromRegistry.children.contains(targetNodeInRegistry)) {
					var index = -1; // -> add it last
					if (update.fullTargetNodeAddedBeforeId != null) {
						// get targetNodeAddedBefore from registry 
						var targetNodeAddedBeforeInRegistry = this.getNodeById(update.fullTargetNodeAddedBeforeId);
						if (targetNodeAddedBeforeInRegistry != null) { // exists, get its index in children list
							index = nodeFromRegistry.children.getItemIndex(targetNodeAddedBeforeInRegistry);	
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
			case "REMOVED":
				var targetNodeInRegistry = this.getNodeById(update.targetNode.nodeUri);	
				
				if (targetNodeInRegistry != null) {
					this.unregisterNode(targetNodeInRegistry, nodeFromRegistry);								
				} else {
					// node not registered, probably it isn't visible for this client
					// Nothing to do
				}
				break;
			case "REQUEST_REFRESH":
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
	for (var i = 0; i < children.length; i++) {	
		this.registerNode(children.getItemAt(i), node, -1);
	}
};

NodeRegistry.prototype.refresh = function(node) {		
	if (!(node.nodeUri in this.registry)) {
		return;
	}
	var self = this;
	this.nodeRegistryManager.serviceInvocator.invoke(
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
	var fullNodeIdWithChildren = this.nodeRegistryManager.externalInvocator.getNewFullNodeIdWithChildrenInstance();
	fullNodeIdWithChildren.fullNodeId = node.nodeUri;
	
	if (node.children != null) {
		for (var i = 0; i < node.children.length; i++) {
			if (fullNodeIdWithChildren.visibleChildren == null) {
				fullNodeIdWithChildren.visibleChildren = this.nodeRegistryManager.externalInvocator.getNewListInstance();
			}
			fullNodeIdWithChildren.visibleChildren.addItem(this.getFullNodeIdWithChildren(node.children.getItemAt(i)));
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
	var i;
	var currentChildNode;
	
	// no children -> remove the old ones
	if (nodeWithVisibleChildren.children == null) {
		this.collapse(node);
		return;
	}
	
	if (node.children != null) { // node has children -> merge current list with new list
		// serch for children that doesn't exist in new list
		var currentChildren = node.children != null ? node.children/*.slice()*/ : [];			
		for (i = 0; i < currentChildren.length; i++) {	
			var exists = false;
			currentChildNode = currentChildren.getItemAt(i);
			for (var j = 0; j < nodeWithVisibleChildren.children.length(); j++) {
//			for (var newChildWithVisibleChildren in nodeWithVisibleChildren.children) {
				newChildWithVisibleChildren = nodeWithVisibleChildren.children.getItemAt(j);
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
	
	for (i = 0; i < nodeWithVisibleChildren.children.length; i++) {	
		var newChildNode = nodeWithVisibleChildren.children.getItemAt(i).node;
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
			parent.children = this.nodeRegistryManager.externalInvocator.getNewListInstance();
		}						
		if (index != -1) {
			parent.children.addItemAt(node, index);
		} else {		
			parent.children.addItem(node);
		}
	}
	
	for (var i = 0; i < this.nodeChangeListeners.length; i++){
		this.nodeChangeListeners[i].nodeAdded(node);
	}
	
};

NodeRegistry.prototype.unregisterNode = function(node, parent) {	
	if (parent != null && (parent.children == null || !parent.children.contains(node))) {
		// not expanded or child already removed
		return;
	}
	// remove children recursive
	this.collapse(node);
			
	if (node.nodeUri in this.registry) {		
		delete this.registry[node.nodeUri];
	}
			
	if (parent != null) {
		// remove from parent list of children
		parent.children.removeItemAt(parent.children.getItemIndex(node));				
		if (parent.children.length == 0) { // parent has no children left -> parent is leaf
			parent.children = null;
		}
	}
	for (var i = 0; i < this.nodeChangeListeners.length; i++){
		this.nodeChangeListeners[i].nodeRemoved(node);
//		throw node;
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
//	throw newProperties["propertyB"];
	for(var property in node.properties){
		if(!(property in newProperties)){
			delete node.properties[property];
		}
	}
};
