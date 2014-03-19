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
package org.flowerplatform.flex_client.core.mindmap.update {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.update.NodeUpdateProcessor;
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.remote.NodeWithChildren;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.PropertyUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.Update;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	
	/**
	 * Holds a node registry (fullNodeId -> Node) and handles the communication with the server. 
	 * This class is responsible for maintaing a consistent node tree. E.g. when new nodes arrive
	 * from server, they are added in the registry and "linked" with their parent. When nodes are
	 * removed (e.g. node collapsed), they are removed from the registry and unlinked from their 
	 * parents.
	 * 
	 * @author Cristina Constantinescu
	 */	
	public class MindMapNodeUpdateProcessor extends NodeUpdateProcessor {
		
		protected var diagramShell:MindMapDiagramShell;
				
		public function MindMapNodeUpdateProcessor(diagramShell:MindMapDiagramShell) {
			this.diagramShell = diagramShell;
		}
		
		/**
		 * Adds <code>node</code> as the rootNode of this diagramShell.
		 */ 
		public function addRootNode(context:DiagramShellContext, node:Node):void {
			nodeRegistry.registerNode(node);
			diagramShell.addModelInRootModelChildrenList(context, node, true);	
		}
		
		/**
		 * Adds <code>node</code> to <code>parent</code> at given index and registers it in registry. <br>
		 * If <code>index</code> is -1, the node will be added last.
		 */ 
		private function addNode(node:Node, parent:Node, index:int = -1):void {			
			nodeRegistry.registerNode(node);
			
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
		private function removeNode(context:DiagramShellContext, node:Node, parent:Node = null):void {
			if (parent != null && (parent.children == null || !parent.children.contains(node))) {
				// not expanded or child already removed
				return;
			}
			// remove children recursive
			removeChildren(context, node, false);
			
			nodeRegistry.unregisterNode(node.fullNodeId);
			
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
		 */
		public function removeChildren(context:DiagramShellContext, node:Node, refreshChildrenAndPositions:Boolean = true):void {
			if (node.children != null) {
				for each (var child:Node in node.children) {
					removeChildren(context, child, false);
					nodeRegistry.unregisterNode(child.fullNodeId);
				}
				node.children = null;
				
				if (refreshChildrenAndPositions) {					
					diagramShell.refreshRootModelChildren(context);
					diagramShell.refreshModelPositions(context, node);
				}
			}			
		}
		
		/* REQUEST CHILDREN */		

		/**
		 * Called from UI when a node is expanded.
		 */
		public function requestChildren(context:DiagramShellContext, node:Node):void {		
			// TODO CS: actiunea de reload, nu tr sa apeleze asta; ar trebui sa apeleze refresh
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.getChildren", 
				[node == null ? Node(diagramShell.rootModel).fullNodeId : node.fullNodeId, true], 
				function (result:Object):void {requestChildrenHandler(context, node, ArrayCollection(result));});	
		}
		
		protected function requestChildrenHandler(context:DiagramShellContext, node:Node, children:ArrayCollection):void {			
			if (node == null) {	// root node				
				// rootModel already set, remove it from diagram
				if (diagramShell.rootModel != null && diagramShell.getDynamicObject(context, diagramShell.rootModel).children != null) {
					removeNode(context, Node(diagramShell.getRoot(context)));
				}
								
				if (diagramShell.showRootModelAsRootNode) {
					node = Node(diagramShell.getRoot(context));
				} else {
					addRootNode(context, Node(children.getItemAt(0)));
					
					// by default, root node is considered expanded
					ControllerUtils.getMindMapModelController(context, node).setExpanded(context, node, true);
				}						
			} 
			
			if (node != null) {
				// register each child
				for each (var child:Node in children) {
					nodeRegistry.registerNode(child);
					child.parent = node;		
					// TODO CS: de refolosit add?
				}
				// set node list of children
				node.children = children;
			}		
			
			// refresh diagram's children and their positions
			diagramShell.refreshRootModelChildren(context);
			diagramShell.refreshModelPositions(context, node);				
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
		override public function rootNodeUpdatesHandler(context:Dictionary, updates:ArrayCollection):void {			
			var diagramShellContext:DiagramShellContext = DiagramShellContext(context);
			
			if (updates == null) {				
				if (diagramShell.rootModel != null) {
					refresh(diagramShellContext, Node(diagramShell.getRoot(diagramShellContext)));
				}
				return;
			}
				
			var nodeToNodeUpdatedEvent:Dictionary = new Dictionary();			
		
			for each (var update:Update in updates) {				
				var nodeFromRegistry:Node = nodeRegistry.getNodeById(update.fullNodeId);	
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
					var targetNodeInRegistry:Node = nodeRegistry.getNodeById(childrenUpdate.targetNode.fullNodeId);	
					switch (childrenUpdate.type) {
						case ChildrenUpdate.ADDED:	
							if (nodeFromRegistry.children != null && !nodeFromRegistry.children.contains(targetNodeInRegistry)) {
								var index:Number = -1; // -> add it last
								if (childrenUpdate.fullTargetNodeAddedBeforeId != null) {
									// get targetNodeAddedBefore from registry 
									var targetNodeAddedBeforeInRegistry:Node = nodeRegistry.getNodeById(childrenUpdate.fullTargetNodeAddedBeforeId);
									if (targetNodeAddedBeforeInRegistry != null) { // exists, get its index in children list
										index = nodeFromRegistry.children.getItemIndex(targetNodeAddedBeforeInRegistry);	
									}
								}
								addNode(childrenUpdate.targetNode, nodeFromRegistry, index);
							} else {
								// child already added, probably after refresh
								// e.g. I add a children, I expand => I get the list with the new children; when the
								// client polls for data, this children will be received as well, and thus duplicated.
								// NOTE: since the instant notifications for the client that executed => this doesn't apply
								// for him; but for other clients yes
								
								// Nothing to do								
							}			
							break;
						case ChildrenUpdate.REMOVED:	
							if (targetNodeInRegistry != null) {
								removeNode(diagramShellContext, targetNodeInRegistry, nodeFromRegistry);
							} else {
								// node not registered, probably it isn't visible for this client
								// Nothing to do
							}
							break;
					}
						
					diagramShell.refreshRootModelChildren(diagramShellContext);
					diagramShell.refreshModelPositions(diagramShellContext, nodeFromRegistry);						
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
		public function refresh(context:DiagramShellContext, node:Node):void {			
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.refresh", 
				[getFullNodeIdWithChildren(node)], 
				function (result:Object):void {
					refreshHandler(context, node, NodeWithChildren(result));
					
					diagramShell.refreshRootModelChildren(context);
					diagramShell.refreshModelPositions(context, node);});
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
		protected function refreshHandler(context:DiagramShellContext, node:Node, nodeWithVisibleChildren:NodeWithChildren):void {
			// set new node properties and dispatch event
			node.properties = nodeWithVisibleChildren.node.properties;
			node.dispatchEvent(new NodeUpdatedEvent(node));	
			
			var newNodeToCurrentNodeIndex:Dictionary = new Dictionary();
			var i:int;
			var currentChildNode:Node;
			
			// no children -> remove the old ones
			if (nodeWithVisibleChildren.children == null) {
				removeChildren(context, node, false);
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
						refreshHandler(context, currentChildNode, newChildWithVisibleChildren);
						// store, for the new child, its index in current list
						newNodeToCurrentNodeIndex[newChildWithVisibleChildren.node.fullNodeId] = i;
					} else {
						// child doesn't exist in new list -> remove it from parent
						removeNode(context, currentChildNode, node);
					}
				}
			}
								
			for (i = 0; i < nodeWithVisibleChildren.children.length; i++) {	
				var newChildNode:Node = NodeWithChildren(nodeWithVisibleChildren.children.getItemAt(i)).node;
				if (!newNodeToCurrentNodeIndex.hasOwnProperty(newChildNode.fullNodeId)) { // new child doesn't exist in current list -> add it
					addNode(newChildNode, node, i);
				} else if (newNodeToCurrentNodeIndex[newChildNode.fullNodeId] != i) { // new child exists in current list, but different indexes -> get current child and move it to new index
					currentChildNode = nodeRegistry.getNodeById(newChildNode.fullNodeId);
					removeNode(context, currentChildNode, node);
					addNode(currentChildNode, node, i);
				}
			}
		}
		
	}
}

