package org.flowerplatform.flex_client.core.mindmap.update {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.mx_internal;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.remote.NodeWithVisibleChildren;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.PropertyUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.Update;
	import org.flowerplatform.flex_client.core.mindmap.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	
	use namespace mx_internal;
	
	/**
	 * Holds a node registry (fullNodeId -> Node) and handles the communication with the server. 
	 * This class is responsible for maintaing a consistent node tree. E.g. when new nodes arrive
	 * from server, they are added in the registry and "linked" with their parent. When nodes are
	 * removed (e.g. node collapsed), they are removed from the registry and unlinked from their 
	 * parents.
	 * 
	 * @author Cristina Constantinescu
	 */
	// TODO CS: niste doc pe la metode (mai mult); inclusiv la evenimente (e.g. cine asculta)
	public class NodeUpdateProcessor {
		
		private var _nodeRegistry:NodeRegistry = new NodeRegistry();
				
		/**
		 * @see checkForUpdates
		 */ 
		private var timestampOfLastRequest:Number;
		
		protected var diagramShell:DiagramShell;
				
		public function NodeUpdateProcessor(diagramShell:DiagramShell) {
			this.diagramShell = diagramShell;
		}
		
		mx_internal function get nodeRegistry():NodeRegistry {
			return _nodeRegistry;
		}
					
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
		
		private function removeNode(node:Node, parent:Node = null, index:int = -1):void {
			if (parent.children == null || !parent.children.contains(node)) { // not expanded or child already removed			
				return;
			}
			removeChildren(node, false);
			nodeRegistry.unregisterNode(node.fullNodeId);
			
			if (parent != null) {
				if (index != -1) {
					parent.children.removeItemAt(index);
				} else {
					parent.children.removeItemAt(parent.children.getItemIndex(node));
				}
				if (parent.children.length == 0) {
					parent.children = null;
				}
			}
		}
		
		/**
		 * Called from <code>removeNode()</code> or from UI: when a node is collapsed.
		 */
		public function removeChildren(node:Node, refreshChildrenAndPositions:Boolean = true):void {
			if (node.children != null) {
				for each (var child:Node in node.children) {
					removeChildren(child, false);
					nodeRegistry.unregisterNode(child.fullNodeId);
				}
				node.children = null;
				
				if (refreshChildrenAndPositions) {
					MindMapDiagramShell(diagramShell).refreshRootModelChildren();
					MindMapDiagramShell(diagramShell).refreshModelPositions(node);
				}
			}			
		}
		
		/* REQUEST CHILDREN */		

		/**
		 * Called from UI when a node is expanded.
		 */
		public function requestChildren(node:Node):void {		
			// TODO CS: actiunea de reload, nu tr sa apeleze asta; ar trebui sa apeleze refresh
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.getChildren", 
				[node == null ? "freeplaneNode|mm://path_to_resource|": node.fullNodeId, true], 
				function (result:Object):void {requestChildrenHandler(node, ArrayCollection(result));});	
		}
		
		protected function requestChildrenHandler(node:Node, children:ArrayCollection):void {
			if (node == null) {	// root node				
				// rootModel already set, remove it from diagram
				if (diagramShell.rootModel != null && MindMapDiagramShell(diagramShell).getDynamicObject(diagramShell.rootModel).children != null) {
					removeNode(Node(MindMapDiagramShell(diagramShell).getRoot()));
				}
				node = Node(children.getItemAt(0));
				
				diagramShell.rootModel = new Node();
				MindMapDiagramShell(diagramShell).addModelInRootModelChildrenList(node, true);	
				
				// by default, root node is considered expanded
				IMindMapControllerProvider(diagramShell.getControllerProvider(node)).getMindMapModelController(node).setExpanded(node, true);
			} else {
				if (node.children != null) { // node already has children, so remove them
					while (node.children.length > 0) {
						removeNode(Node(node.children.getItemAt(0)), node, 0);					
					}
				}
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
			MindMapDiagramShell(diagramShell).refreshRootModelChildren();
			MindMapDiagramShell(diagramShell).refreshModelPositions(node);				
		}
				
		public function checkForUpdates():void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"updateService.getUpdates",	
				[Node(Node(MindMapDiagramShell(diagramShell).getRoot()).fullNodeId), timestampOfLastRequest], rootNodeUpdatesHandler);	
				// TODO CS: de init timestamp la subscribe
		}
			
		public function rootNodeUpdatesHandler(updates:ArrayCollection):void {
			if (updates == null) {				
				if (diagramShell.rootModel != null) {
					refresh(Node(MindMapDiagramShell(diagramShell).getRoot()));
				}
				return;
			}
			
			var propertiesUpdated:ArrayList;
			var propertiesRemoved:ArrayList;
			for each (var update:Update in updates) {
				var nodeFromRegistry:Node = nodeRegistry.getNodeById(update.fullNodeId);	
				if (nodeFromRegistry == null) { // node not registered, probably it isn't visible for this client
					continue;
				}
				if (update is PropertyUpdate) { // property update
					var propertyUpdate:PropertyUpdate = PropertyUpdate(update);
					if (propertyUpdate.isUnset) {
						delete nodeFromRegistry.properties[propertyUpdate.key];
						if (propertiesRemoved == null) {
							propertiesRemoved = new ArrayList();
						}
						propertiesRemoved.addItem(propertyUpdate.key);
					} else {
						nodeFromRegistry.properties[propertyUpdate.key] = propertyUpdate.value;
						if (propertiesUpdated == null) {
							propertiesUpdated = new ArrayList();
						}
						propertiesUpdated.addItem(propertyUpdate.key);
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
								removeNode(targetNodeInRegistry, nodeFromRegistry, -1);
							} else {
								// node not registered, probably it isn't visible for this client
								// Nothing to do
							}
							break;
					}
						
					MindMapDiagramShell(diagramShell).refreshRootModelChildren();
					MindMapDiagramShell(diagramShell).refreshModelPositions(nodeFromRegistry);						
				}					
			}		
			
			if (propertiesUpdated != null || propertiesRemoved != null) {
				nodeFromRegistry.dispatchEvent(new NodeUpdatedEvent(nodeFromRegistry, propertiesUpdated, propertiesRemoved));
			}
			
			// store last update timestamp
			timestampOfLastRequest = Update(updates.getItemAt(updates.length - 1)).timestamp;		
		}	
		
		public function refresh(node:Node):void {
			// TODO CC: to be modified
			return;
//			CorePlugin.getInstance().serviceLocator.invoke(
//				"nodeService.refresh", 
//				[getNodeWithVisibleChildren(node)], 
//				function (result:Object):void {
//					refreshHandler(node, NodeWithVisibleChildren(result));
//					
//					MindMapDiagramShell(diagramShell).refreshRootModelChildren();
//					MindMapDiagramShell(diagramShell).refreshModelPositions(node);});
		}
		
//		private function getNodeWithVisibleChildren(node:Node):NodeWithVisibleChildren {
//			var nodeWithVisibleChildren:NodeWithVisibleChildren = new NodeWithVisibleChildren();
//			nodeWithVisibleChildren.fullNodeId = node.fullNodeId;
//			
//			if (node.children != null) {
//				for each (var child:Node in node.children) {
//					if (nodeWithVisibleChildren.visibleChildren == null) {
//						nodeWithVisibleChildren.visibleChildren = new ArrayCollection();
//					}
//					nodeWithVisibleChildren.visibleChildren.addItem(getNodeWithVisibleChildren(child));
//				}
//			}
//			return nodeWithVisibleChildren;
//		}
		
		protected function refreshHandler(node:Node, nodeWithVisibleChildren:NodeWithVisibleChildren):void {
			if (nodeWithVisibleChildren.node.properties == null) {
				throw new Error("No properties available for node! This should NOT happen!");
			}
			// set new node properties and dispatch event
			node.properties = nodeWithVisibleChildren.node.properties;
			node.dispatchEvent(new NodeUpdatedEvent(node));	
			
			var newNodeToCurrentNodeIndex:Dictionary = new Dictionary();
			var i:int;
			var currentChildNode:Node;
			
			if (node.children != null) { // node has children -> merge current list with new list
				
				// serch for children that doesn't exist in new list
				var currentChildren:ArrayCollection = node.children != null ? new ArrayCollection(node.children.toArray()) : new ArrayCollection();			
				for (i = 0; i < currentChildren.length; i++) {	
					var exists:Boolean = false;
					currentChildNode = Node(currentChildren.getItemAt(i));
					for each (var newChildWithVisibleChildren:NodeWithVisibleChildren in nodeWithVisibleChildren.visibleChildren) {
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
			
			// perform merge 
			
			for (i = 0; i < nodeWithVisibleChildren.visibleChildren.length; i++) {	
				var newChildNode:Node = NodeWithVisibleChildren(nodeWithVisibleChildren.visibleChildren.getItemAt(i)).node;
				if (!newNodeToCurrentNodeIndex.hasOwnProperty(newChildNode.fullNodeId)) { // new child doesn't exist in current list -> add it
					addNode(newChildNode, node, i);
				} else if (newNodeToCurrentNodeIndex[newChildNode.fullNodeId] != i) { // new child exists in current list, but different indexes -> get current child and move it to new index
					currentChildNode = nodeRegistry.getNodeById(newChildNode.fullNodeId);
					removeNode(currentChildNode, node);
					addNode(currentChildNode, node, i);
				}
			}
		}
		
	}
}
