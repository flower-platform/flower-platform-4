/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flex_client.core.node {
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.NodeWithChildren;
	import org.flowerplatform.flex_client.core.editor.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.editor.remote.update.PropertyUpdate;
	import org.flowerplatform.flex_client.core.editor.remote.update.Update;
	import org.flowerplatform.flex_client.core.node.event.NodeRemovedEvent;
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.node.event.RefreshEvent;
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
		
		public function getNodeById(id:String):Node {
			return Node(registry[id]);
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
								
			if (refreshChildren) {	
				dispatchEvent(new RefreshEvent(node));
			}						
		}
			
		public function expand(node:Node, additionalHandler:Function = null):void {		
			if (!registry.hasOwnProperty(node.nodeUri)) {
				return;
			}
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.getChildren", 
				[node.nodeUri, new ServiceContext().add(CoreConstants.POPULATE_WITH_PROPERTIES, true)], 
				function (result:Object):void {
					expandCallbackHandler(node, ArrayCollection(result)); 
					
					// additional handler to be executed
					if (additionalHandler != null) {
						additionalHandler();
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
						break;
					}
				}
				return;
			}
			
			var nodeToNodeUpdatedEvent:Dictionary = new Dictionary();			
			
			for (var i:int = updates.length - 1; i >= 0; i--) {
				var update:Update = Update(updates.getItemAt(i));
				var nodeFromRegistry:Node = getNodeById(update.fullNodeId);	
				if (nodeFromRegistry == null) { // node not registered, probably it isn't visible for this client
					continue;
				}
				if (update is PropertyUpdate) { // property update
					var propertyUpdate:PropertyUpdate = PropertyUpdate(update);
					if (!nodeToNodeUpdatedEvent.hasOwnProperty(nodeFromRegistry.nodeUri)) {
						nodeToNodeUpdatedEvent[nodeFromRegistry.nodeUri] = new NodeUpdatedEvent(nodeFromRegistry);
					}					
					if (propertyUpdate.isUnset) {
						delete nodeFromRegistry.properties[propertyUpdate.key];						
						NodeUpdatedEvent(nodeToNodeUpdatedEvent[nodeFromRegistry.nodeUri]).addRemovedProperty(propertyUpdate.key);
					} else {
						nodeFromRegistry.properties[propertyUpdate.key] = propertyUpdate.value;						
						NodeUpdatedEvent(nodeToNodeUpdatedEvent[nodeFromRegistry.nodeUri]).addUpdatedProperty(propertyUpdate.key);
					}					
				} else { // children update
					var childrenUpdate:ChildrenUpdate = ChildrenUpdate(update);
					var targetNodeInRegistry:Node = getNodeById(childrenUpdate.targetNode.nodeUri);	
					var refresh:Boolean = false;
					
					switch (childrenUpdate.type) {
						case CoreConstants.UPDATE_CHILD_ADDED:	
							if (nodeFromRegistry.children != null && !nodeFromRegistry.children.contains(targetNodeInRegistry)) {
								var index:Number = -1; // -> add it last
								if (childrenUpdate.fullTargetNodeAddedBeforeId != null) {
									// get targetNodeAddedBefore from registry 
									var targetNodeAddedBeforeInRegistry:Node = getNodeById(childrenUpdate.fullTargetNodeAddedBeforeId);
									if (targetNodeAddedBeforeInRegistry != null) { // exists, get its index in children list
										index = nodeFromRegistry.children.getItemIndex(targetNodeAddedBeforeInRegistry);	
									}
								}
								// Children that come with the <code>ChildrenUpdate</code>s must be cloned, so that a different instance
								// is applied to each editor. Otherwise, the same node will be added to all the node registries, and then 
								// any <code>NodeUpdatedEvent</code> on this node will be caught by all the renderers from all open editors
								var newChild:Node = new Node(childrenUpdate.targetNode.nodeUri);
								newChild.type = childrenUpdate.targetNode.type;
								newChild.properties = childrenUpdate.targetNode.properties;
								registerNode(newChild, nodeFromRegistry, index);
								refresh = true;
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
							if (targetNodeInRegistry != null) {
								unregisterNode(targetNodeInRegistry, nodeFromRegistry);
								refresh = true;
							} else {
								// node not registered, probably it isn't visible for this client
								// Nothing to do
							}
							break;
					}
					
					if (refresh) {
						dispatchEvent(new RefreshEvent(nodeFromRegistry));
					}
				}				
			}		
			
			for (var key:String in nodeToNodeUpdatedEvent) {
				var event:NodeUpdatedEvent = NodeUpdatedEvent(nodeToNodeUpdatedEvent[key]);
				event.node.dispatchEvent(event);				
			}
		}	
		
		public function refresh(node:Node):void {		
			if (!registry.hasOwnProperty(node.nodeUri)) {
				return;
			}
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.refresh", 
				[getFullNodeIdWithChildren(node)], 
				function (result:Object):void {
					refreshHandler(node, NodeWithChildren(result));
					
					dispatchEvent(new RefreshEvent(node));
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
			node.properties = nodeWithVisibleChildren.node.properties;
			var nodeFromRegistry:Node = getNodeById(node.nodeUri);
			if (nodeFromRegistry != null && nodeFromRegistry != node) {
				nodeFromRegistry.properties = nodeWithVisibleChildren.node.properties;
			}
			node.dispatchEvent(new NodeUpdatedEvent(node));	
			
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
		
		/**
		 * Adds <code>node</code> to <code>parent</code> at given index and registers it in registry. <br>
		 * 
		 * If <code>parent</code> is null, the node will only be registered
		 * If <code>index</code> is -1, the node will be added last.
		 */ 
		mx_internal function registerNode(node:Node, parent:Node, index:int = -1):void {		
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
				nodeFromRegistry.dispatchEvent(new NodeRemovedEvent(nodeFromRegistry));
				delete registry[node.nodeUri];
			}
			
			if (parent != null) {
				// remove from parent list of children
				parent.children.removeItemAt(parent.children.getItemIndex(node));				
				if (parent.children.length == 0) { // parent has no children left -> parent is leaf
					parent.children = null;
				}
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
			
			// refresh diagram's children and their positions
			dispatchEvent(new RefreshEvent(node));
		}
				
	}
}
