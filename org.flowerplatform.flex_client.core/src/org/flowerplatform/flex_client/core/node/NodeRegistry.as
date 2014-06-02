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
	import org.flowerplatform.flex_client.core.node.event.BeforeRemoveNodeChildrenEvent;
	import org.flowerplatform.flex_client.core.node.event.NodeRemovedEvent;
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.node.event.RefreshEvent;
	import org.flowerplatform.flex_client.core.node.event.RootNodeAddedEvent;
	import org.flowerplatform.flex_client.core.node.remote.ServiceContext;
	
	use namespace mx_internal;
	
	/**
	 * Holds a node registry (fullNodeId -> Node) and handles the communication with the server. 
	 * This class is responsible for maintaing a consistent node tree. E.g. when new nodes arrive
	 * from server, they are added in the registry and "linked" with their parent. When nodes are
	 * removed (e.g. node collapsed), they are removed from the registry and unlinked from their 
	 * parents.
	 * 
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class NodeRegistry extends EventDispatcher {
				
		/**
		 * Reference to registry's "start" node.
		 * If <code>useStartingNodeAsRootNode</code>, first level of nodes will be "linked" with this node as parent,
		 * otherwise the first node will be used as root node.
		 */ 
		public var startingNode:Node = null;
		public var useStartingNodeAsRootNode:Boolean = true;
		
		private var registry:Dictionary = new Dictionary();
		
		public function getNodeById(id:String):Node {
			return Node(registry[id]);
		}
		
		public function registerNode(node:Node):void {			
			registry[node.fullNodeId] = node;
		}
		
		private function unregisterNode(nodeId:String):void {
			var nodeFromRegistry:Node = registry[nodeId];
			if (nodeFromRegistry != null) {
				nodeFromRegistry.dispatchEvent(new NodeRemovedEvent(nodeFromRegistry));
				delete registry[nodeId];
			}
		}
						
		/**
		 * Adds <code>node</code> to <code>parent</code> at given index and registers it in registry. <br>
		 * 
		 * If <code>parent</code> is null, the node will be added as root node.
		 * If <code>index</code> is -1, the node will be added last.
		 */ 
		protected function addNode(node:Node, parent:Node = null, index:int = -1):void {		
			if (parent == null) {
				// we have a root node
				
				var nodeFromRegistry:Node = getNodeById(node.fullNodeId);
				if (nodeFromRegistry) { 
					// node already in registry -> we want to update only its properties
					nodeFromRegistry.properties = node.properties;
				} else {
					registerNode(node);
					nodeFromRegistry = getNodeById(node.fullNodeId);
				}		
				
				startingNode = nodeFromRegistry;
				
				// notify listeners that a rootNode has been added/updated (additional behavior can be added like expand it immediately)
				dispatchEvent(new RootNodeAddedEvent(nodeFromRegistry));
				return;
			}
			
			// normal node -> register it and link it with its parent
			
			registerNode(node);
			
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
		
		/**
		 * Removes node from parent and un-registers it from registry. <br>
		 * Note: parent == null -> remove root node
		 */ 
		protected function removeNode(node:Node, parent:Node = null):void {
			if (parent != null && (parent.children == null || !parent.children.contains(node))) {
				// not expanded or child already removed
				return;
			}
			// remove children recursive
			removeChildren(node, false);
			
			unregisterNode(node.fullNodeId);
			
			if (parent != null) {
				// remove from parent list of children
				parent.children.removeItemAt(parent.children.getItemIndex(node));				
				if (parent.children.length == 0) { // parent has no children left -> parent is leaf
					parent.children = null;
				}
			}
		}
		
		/**
		 * Called from <code>removeNode()</code> or from UI: when a node is collapsed.
		 * 
		 * <p>
		 * If the node is subscribable, remove it from the resource nodes list.
		 */
		public function removeChildren(node:Node, refreshChildren:Boolean = true, shouldDispatchPreRemoveEvent:Boolean = true):void {
			var event:BeforeRemoveNodeChildrenEvent = new BeforeRemoveNodeChildrenEvent(node, refreshChildren);
			if (shouldDispatchPreRemoveEvent) {
				dispatchEvent(event);	
			}
			
			if (!event.dontRemoveChildren) {
				removeChildrenRecursive(node, refreshChildren);
			}		
		}
		
		private function removeChildrenRecursive(node:Node, refreshChildren:Boolean = true):void {
			if (node.children != null) {
				for each (var child:Node in node.children) {
					removeChildrenRecursive(child, false);
					unregisterNode(child.fullNodeId);
				}
				node.children = null;
				
				if (refreshChildren) {	
					dispatchEvent(new RefreshEvent(node));
				}
			}
		}
				
		/* REQUEST CHILDREN */		
		
		/**
		 * Called from UI when a node is expanded.
		 * 
		 * <p>
		 * If the node is subscribable, first subscribe, then request the children if the subscribe was successful.
		 */
		public function requestChildren(node:Node, additionalHandler:Function = null):void {		
			// TODO CS: actiunea de reload, nu tr sa apeleze asta; ar trebui sa apeleze refresh
			var isSubscribable:Boolean = node == null ? false : node.properties[CoreConstants.IS_SUBSCRIBABLE];
			if (!isSubscribable) {
				requestChildrenInternal(node, additionalHandler);
			} else {
				CorePlugin.getInstance().resourceNodesManager.subscribeToSelfOrParentResource(node.fullNodeId, this, function(resourceNode:Node):void {
					requestChildrenInternal(node, additionalHandler);
				});
			}
		}
		
		private function requestChildrenInternal(node:Node, additionalHandler:Function = null):void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.getChildren", 
				[node == null ? startingNode.fullNodeId : node.fullNodeId, new ServiceContext().add(CoreConstants.POPULATE_WITH_PROPERTIES, true)], 
				function (result:Object):void {
					requestChildrenCallbackHandler(node, ArrayCollection(result)); 
					
					// additional handler to be executed
					if (additionalHandler != null) {
						additionalHandler();
					}
				});
		}
		
		public function requestChildrenCallbackHandler(node:Node, children:ArrayCollection):void {			
			if (node == null) {	// root node				
				if (useStartingNodeAsRootNode) {
					node = startingNode;
				} else {
					node = Node(children.getItemAt(0));
					addNode(node);
					
					// set to null -> don't enter in next if
					children = null;
				}				
			}
			
			if (node != null && children != null) {
				// register each child
				for each (var child:Node in children) {
					registerNode(child);
					child.parent = node;		
					// TODO CS: de refolosit add?
				}
				// set node list of children
				node.children = children;
			}
						
			if (children != null) {
				// refresh diagram's children and their positions
				dispatchEvent(new RefreshEvent(node));
			}			
		}
		
		/* UPDATES */	
		
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
		 * 
		 * At the end, stores in <code>timestampOfLastRequest</code>, the last update's timestamp.
		 */ 
		public function nodeUpdatesHandler(updates:ArrayCollection):void {			
			if (updates == null) {	
				refresh(startingNode);
				return;
			}
			
			var nodeToNodeUpdatedEvent:Dictionary = new Dictionary();			
			
			for each (var update:Update in updates) {				
				var nodeFromRegistry:Node = getNodeById(update.fullNodeId);	
				if (nodeFromRegistry == null) { // node not registered, probably it isn't visible for this client
					continue;
				}
				if (update is PropertyUpdate) { // property update
					var propertyUpdate:PropertyUpdate = PropertyUpdate(update);
					if (!nodeToNodeUpdatedEvent.hasOwnProperty(nodeFromRegistry.fullNodeId)) {
						nodeToNodeUpdatedEvent[nodeFromRegistry.fullNodeId] = new NodeUpdatedEvent(nodeFromRegistry);
					}					
					if (propertyUpdate.isUnset) {
						delete nodeFromRegistry.properties[propertyUpdate.key];						
						NodeUpdatedEvent(nodeToNodeUpdatedEvent[nodeFromRegistry.fullNodeId]).addRemovedProperty(propertyUpdate.key);
					} else {
						nodeFromRegistry.properties[propertyUpdate.key] = propertyUpdate.value;						
						NodeUpdatedEvent(nodeToNodeUpdatedEvent[nodeFromRegistry.fullNodeId]).addUpdatedProperty(propertyUpdate.key);
					}					
				} else { // children update
					var childrenUpdate:ChildrenUpdate = ChildrenUpdate(update);
					var targetNodeInRegistry:Node = getNodeById(childrenUpdate.targetNode.fullNodeId);	
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
								var newChild:Node = new Node(childrenUpdate.targetNode.fullNodeId);
								newChild.properties = childrenUpdate.targetNode.properties;
								addNode(newChild, nodeFromRegistry, index);
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
								removeNode(targetNodeInRegistry, nodeFromRegistry);
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
		
		/* REFRESH */	
		
		/**
		 * Called from Refresh action.
		 */	
		public function refresh(node:Node):void {			
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
		private function getFullNodeIdWithChildren(node:Node):FullNodeIdWithChildren {
			var fullNodeIdWithChildren:FullNodeIdWithChildren = new FullNodeIdWithChildren();
			fullNodeIdWithChildren.fullNodeId = node.fullNodeId;
			
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
			var nodeFromRegistry:Node = getNodeById(node.fullNodeId);
			if (nodeFromRegistry != null && nodeFromRegistry != node) {
				nodeFromRegistry.properties = nodeWithVisibleChildren.node.properties;
			}
			node.dispatchEvent(new NodeUpdatedEvent(node));	
			
			var newNodeToCurrentNodeIndex:Dictionary = new Dictionary();
			var i:int;
			var currentChildNode:Node;
			
			// no children -> remove the old ones
			if (nodeWithVisibleChildren.children == null) {
				removeChildren(node, false);
				return;
			}
			
			if (node.children != null) { // node has children -> merge current list with new list
				
				// serch for children that doesn't exist in new list
				var currentChildren:ArrayCollection = node.children != null ? new ArrayCollection(node.children.toArray()) : new ArrayCollection();			
				for (i = 0; i < currentChildren.length; i++) {	
					var exists:Boolean = false;
					currentChildNode = Node(currentChildren.getItemAt(i));
					for each (var newChildWithVisibleChildren:NodeWithChildren in nodeWithVisibleChildren.children) {
						if (currentChildNode.fullNodeId == newChildWithVisibleChildren.node.fullNodeId) {
							exists = true;
							break;
						}
					}
					if (exists) {
						// child exists -> refresh its structure
						refreshHandler(currentChildNode, newChildWithVisibleChildren);
						// store, for the new child, its index in current list
						newNodeToCurrentNodeIndex[newChildWithVisibleChildren.node.fullNodeId] = i;
					} else {
						// child doesn't exist in new list -> remove it from parent
						removeNode(currentChildNode, node);
					}
				}
			}
			
			for (i = 0; i < nodeWithVisibleChildren.children.length; i++) {	
				var newChildNode:Node = NodeWithChildren(nodeWithVisibleChildren.children.getItemAt(i)).node;
				if (!newNodeToCurrentNodeIndex.hasOwnProperty(newChildNode.fullNodeId)) { // new child doesn't exist in current list -> add it
					addNode(newChildNode, node, i);
				} else if (newNodeToCurrentNodeIndex[newChildNode.fullNodeId] != i) { // new child exists in current list, but different indexes -> get current child and move it to new index
					currentChildNode = getNodeById(newChildNode.fullNodeId);
					removeNode(currentChildNode, node);
					addNode(currentChildNode, node, i);
				}
			}
		}
				
	}
}
