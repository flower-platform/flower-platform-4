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
package org.flowerplatform.flex_client.core.node {
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.core.mx_internal;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.NodeWithChildren;
	import org.flowerplatform.flex_client.core.editor.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.editor.remote.update.PropertyUpdate;
	import org.flowerplatform.flex_client.core.editor.remote.update.Update;
	import org.flowerplatform.flex_client.core.node.remote.ServiceContext;
	
	use namespace mx_internal;
	
	/**
	 * Holds a node registry (id -> Node) and handles the communication with the server. 
	 * 
	 * <p>
	 * This class is responsible for maintaing a consistent node tree. <br>
	 * E.g. when new nodes arrive from server, they are added in the registry and "linked" with their parent. When nodes are
	 * removed (e.g. node collapsed), they are removed from the registry and unlinked from their parents.
	 * 
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class NodeRegistry extends EventDispatcher {
						
		protected var registry:Dictionary = new Dictionary();
		
		protected var rootNodeUri:String;
		
		protected var nodeChangeListeners:IList = new ArrayCollection();
		
		protected var nodeRegistryManager:NodeRegistryManager;
		
		public function NodeRegistry(nodeRegistryManager:NodeRegistryManager) {	
			this.nodeRegistryManager = nodeRegistryManager;
		}
		
		public function getRootNodeUri():String {
			return rootNodeUri;
		}
		
		public function getNodeById(id:String):Node {
			return Node(registry[id]);
		}
		
		public function addNodeChangeListener(listener:INodeChangeListener):void {
			nodeChangeListeners.addItem(listener);
		}
		
		public function collapse(node:Node, refreshChildren:Boolean = true):void {
			if (!registry.hasOwnProperty(node.nodeUri)) {
				return;
			}
			if (node.children == null) {
				return;
			}
			while (node.children != null) {					
				unregisterNode(Node(node.children.getItemAt(0)), node);
			}			
		}
			
		public function expand(node:Node, context:Object):void {		
			if (!registry.hasOwnProperty(node.nodeUri)) {
				return;
			}
			
			var serviceContext:ServiceContext = new ServiceContext();
			if (context != null) {
				for (var key:Object in context) {
					serviceContext.context[key] = context[key];
				}
			}
			serviceContext.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
			
			nodeRegistryManager.serviceInvocator.invoke(
				"nodeService.getChildren", 
				[node.nodeUri, serviceContext], 
				function (result:Object):void {
					expandCallbackHandler(node, ArrayCollection(result)); 
					
					// additional handler to be executed
					if (context != null && context.hasOwnProperty(CoreConstants.HANDLER)) {
						context[CoreConstants.HANDLER]();
					}
				});	
		}
						
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
		public function processUpdates(updates:ArrayCollection):void {			
			if (updates == null) {
				// refresh all from root node (node.parent == null)
				for (var fullNodeId:Object in registry) {
					var node:Node = Node(registry[fullNodeId]);
					if (node.parent == null) {
						refresh(node);
					}
				}
				return;
			}
			
			for (var i:int = updates.length - 1; i >= 0; i--) {
				var update:Update = Update(updates.getItemAt(i));
				var nodeFromRegistry:Node = getNodeById(update.fullNodeId);	
				if (nodeFromRegistry == null) { // node not registered, probably it isn't visible for this client
					continue;
				}
				
				switch (update.type) {
					case CoreConstants.UPDATE_PROPERTY:
						var propertyUpdate:PropertyUpdate = PropertyUpdate(update);
						
						if (propertyUpdate.isUnset) {
							delete nodeFromRegistry.properties[propertyUpdate.key];						
						} else {
							setPropertyValue(nodeFromRegistry, propertyUpdate.key, propertyUpdate.value);						
						}					
						break;
					case CoreConstants.UPDATE_CHILD_ADDED:						
						if (nodeFromRegistry.children != null && !nodeFromRegistry.children.contains(getNodeById(ChildrenUpdate(update).targetNode.nodeUri))) {
							var index:Number = -1; // -> add it last
							if (ChildrenUpdate(update).fullTargetNodeAddedBeforeId != null) {
								// get targetNodeAddedBefore from registry 
								var targetNodeAddedBeforeInRegistry:Node = getNodeById(ChildrenUpdate(update).fullTargetNodeAddedBeforeId);
								if (targetNodeAddedBeforeInRegistry != null) { // exists, get its index in children list
									index = nodeFromRegistry.children.getItemIndex(targetNodeAddedBeforeInRegistry);	
								}
							}								
							registerNode(ChildrenUpdate(update).targetNode, nodeFromRegistry, index);								
						} else {
							// child already added, probably after refresh
							// e.g. I add a children, I expand => I get the list with the new children; when the
							// client polls for data, this children will be received as well, and thus duplicated.
							// NOTE: since the instant notifications for the client that executed => this doesn't apply
							// for him; but for other clients yes
							
							// Nothing to do								
						}							
						break;
					case CoreConstants.UPDATE_CHILD_REMOVED:
						// children update						
						var targetNodeInRegistry:Node = getNodeById(ChildrenUpdate(update).targetNode.nodeUri);	
						
						if (targetNodeInRegistry != null) {
							unregisterNode(targetNodeInRegistry, nodeFromRegistry);								
						} else {
							// node not registered, probably it isn't visible for this client
							// Nothing to do
						}										
						break;
					case CoreConstants.UPDATE_REQUEST_REFRESH:
						refresh(nodeFromRegistry);
						break;
					default:
						update.apply(this, nodeFromRegistry);
				}				
			}			
		}	
		
		public function refresh(node:Node):void {		
			if (!registry.hasOwnProperty(node.nodeUri)) {
				return;
			}
			nodeRegistryManager.serviceInvocator.invoke(
				"nodeService.refresh", 
				[getFullNodeIdWithChildren(node)], 
				function (result:Object):void {
					refreshHandler(node, NodeWithChildren(result));
				});
		}
		
		/**
		 * @return an hierarchical structure of <code>fullNodeId</code>s starting from <code>node</code>.
		 */ 
		protected function getFullNodeIdWithChildren(node:Node):FullNodeIdWithChildren {
			var fullNodeIdWithChildren:FullNodeIdWithChildren = new FullNodeIdWithChildren();
			fullNodeIdWithChildren.fullNodeId = node.nodeUri;
			
			if (node.children != null) {
				for each (var child:Node in node.children) {
					if (fullNodeIdWithChildren.visibleChildren == null) {
						fullNodeIdWithChildren.visibleChildren = new ArrayCollection();
					}
					fullNodeIdWithChildren.visibleChildren.addItem(getFullNodeIdWithChildren(child));
				}
			}
			return fullNodeIdWithChildren;
		}
		
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
		protected function refreshHandler(node:Node, nodeWithVisibleChildren:NodeWithChildren):void {
			// set new node properties and dispatch event			
			var nodeFromRegistry:Node = getNodeById(node.nodeUri);
			setNodeProperties(nodeFromRegistry, nodeWithVisibleChildren.node.properties);
						
			var newNodeToCurrentNodeIndex:Dictionary = new Dictionary();
			var i:int;
			var currentChildNode:Node;
			
			// no children -> remove the old ones
			if (nodeWithVisibleChildren.children == null) {
				collapse(node, false);
				return;
			}
			
			if (node.children != null) { // node has children -> merge current list with new list
				// serch for children that doesn't exist in new list
				var currentChildren:ArrayCollection = node.children != null ? new ArrayCollection(node.children.toArray()) : new ArrayCollection();			
				for (i = 0; i < currentChildren.length; i++) {	
					var exists:Boolean = false;
					currentChildNode = Node(currentChildren.getItemAt(i));
					for each (var newChildWithVisibleChildren:NodeWithChildren in nodeWithVisibleChildren.children) {
						if (currentChildNode.nodeUri == newChildWithVisibleChildren.node.nodeUri) {
							exists = true;
							break;
						}
					}
					if (exists) {
						// child exists -> refresh its structure
						refreshHandler(currentChildNode, newChildWithVisibleChildren);
						// store, for the new child, its index in current list
						newNodeToCurrentNodeIndex[newChildWithVisibleChildren.node.nodeUri] = i;
					} else {
						// child doesn't exist in new list -> remove it from parent
						unregisterNode(currentChildNode, node);
					}
				}
			}
			
			for (i = 0; i < nodeWithVisibleChildren.children.length; i++) {	
				var newChildNode:Node = NodeWithChildren(nodeWithVisibleChildren.children.getItemAt(i)).node;
				if (!newNodeToCurrentNodeIndex.hasOwnProperty(newChildNode.nodeUri)) { // new child doesn't exist in current list -> add it
					registerNode(newChildNode, node, i);
				} else if (newNodeToCurrentNodeIndex[newChildNode.nodeUri] != i) { // new child exists in current list, but different indexes -> get current child and move it to new index
					currentChildNode = getNodeById(newChildNode.nodeUri);
					unregisterNode(currentChildNode, node);
					registerNode(currentChildNode, node, i);
				}
			}
		}
		
		mx_internal function mergeOrRegisterNode(node:Node):Node {
			var existingNode:Node = getNodeById(node.nodeUri);
			if (existingNode == null) {
				registerNode(node, null);
				return node;
			}
			setNodeProperties(existingNode, node.properties);
			
			return existingNode;
		}
		
		/**
		 * Adds <code>node</code> to <code>parent</code> at given index and registers it in registry. <br>
		 * 
		 * If <code>parent</code> is null, the node will only be registered
		 * If <code>index</code> is -1, the node will be added last.
		 */ 
		mx_internal function registerNode(node:Node, parent:Node, index:int = -1):void {
			if (rootNodeUri == null) {
				rootNodeUri = node.nodeUri;
			}
			
			registry[node.nodeUri] = node;
			
			if (parent != null) {
				node.parent = parent;
				if (parent.children == null) {
					parent.children = new ArrayCollection();
				}						
				if (index != -1) {
					parent.children.addItemAt(node, index);
				} else {		
					parent.children.addItem(node);
				}
			}
			
			for each (var listener:INodeChangeListener in nodeChangeListeners) {
				listener.nodeAdded(node);
			}
		}
		
		/**
		 * Removes node from parent and un-registers it from registry.
		 */ 
		mx_internal function unregisterNode(node:Node, parent:Node = null):void {
			if (parent != null && (parent.children == null || !parent.children.contains(node))) {
				// not expanded or child already removed
				return;
			}
			// remove children recursive
			collapse(node, false);
			
			var nodeFromRegistry:Node = registry[node.nodeUri];
			if (nodeFromRegistry != null) {
				delete registry[node.nodeUri];
			}
			
			if (parent != null) {
				// remove from parent list of children
				parent.children.removeItemAt(parent.children.getItemIndex(node));				
				if (parent.children.length == 0) { // parent has no children left -> parent is leaf
					parent.children = null;
				}
			}
			for each (var listener:INodeChangeListener in nodeChangeListeners) {
				listener.nodeRemoved(node);
			}
		}
		
		mx_internal function expandCallbackHandler(node:Node, children:ArrayCollection):void {		
			if (children == null) {
				return;
			}
			
			// register each child
			for each (var child:Node in children) {
				registerNode(child, node);
			}
		}
		
		public function setPropertyValue(node:Node, property:String, newValue:Object):void {
			var oldValue:Object = node.properties.hasOwnProperty(property) ? node.properties[property] : null;
			node.properties[property] = newValue;
			
			for each (var listener:INodeChangeListener in nodeChangeListeners) {
				listener.nodeUpdated(node, property, oldValue, newValue);
			}
		}
				
		protected function setNodeProperties(node:Node, newProperties:Object):void {
			var classInfoProperties:Array = ObjectUtil.getClassInfo(newProperties).properties as Array;
			for each (var property:String in classInfoProperties) {
				setPropertyValue(node, property, newProperties[property]);
			}
		}
				
	}
}
