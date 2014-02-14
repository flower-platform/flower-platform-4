package org.flowerplatform.flex_client.core.mindmap.update {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;
	import mx.rpc.events.ResultEvent;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.remote.NodeWithVisibleChildren;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.PropertyUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.Update;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	
	use namespace mx_internal;
	
	/**
	 * @author Cristina Constantinescu
	 */
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
					
		private function addNode(node:Node, parent:Node = null, index:int = -1):void {			
			nodeRegistry.registerNode(node);
			
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
			} else {
				diagramShell.rootModel = new Node();
				MindMapDiagramShell(diagramShell).addModelInRootModelChildrenList(node, true);				
			}
		}
		
		private function removeNode(node:Node, parent:Node = null, index:int = -1):void {
			removeChildren(node, false);
			nodeRegistry.unregisterNode(node.id);
			
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
		
		/* REMOVE CHILDREN */
		
		public function removeChildren(node:Node, refreshChildrenAndPositions:Boolean = true):void {
			if (node.children != null) {
				for each (var child:Node in node.children) {
					removeChildren(child, false);
					nodeRegistry.unregisterNode(child.id);
				}
				node.children = null;
				
				if (refreshChildrenAndPositions) {
					MindMapDiagramShell(diagramShell).refreshRootModelChildren();
					MindMapDiagramShell(diagramShell).refreshModelPositions(node);
				}
			}			
		}
		
		/* REQUEST CHILDREN */
		
		public function requestChildren(node:Node):void {		
			if (node == null) {
				node = new Node();
				node.type = "freeplaneNode";
				node.resource = "mm://path_to_resource";
			}
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.getChildren", 
				[node, true], 
				function (result:ResultEvent):void {requestChildrenHandler(node, ArrayCollection(result.result));});	
		}
		
		protected function requestChildrenHandler(node:Node, children:ArrayCollection):void {
			if (node.id == null) {	// root node				
				// rootModel already set, remove it from diagram
				if (diagramShell.rootModel != null && MindMapDiagramShell(diagramShell).getDynamicObject(diagramShell.rootModel).children != null) {
					removeNode(Node(MindMapDiagramShell(diagramShell).getRoot()));
				}
				node = Node(children.getItemAt(0));
				
				addNode(node);
				
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
				}
				// set node list of children
				node.children = children;
			}		
			// refresh diagram's children and their positions
			MindMapDiagramShell(diagramShell).refreshRootModelChildren();
			MindMapDiagramShell(diagramShell).refreshModelPositions(node);				
		}
				
		/* GET UPDATES */
		
		public function checkForUpdates():void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"updateService.getUpdates",	
				[Node(MindMapDiagramShell(diagramShell).getRoot()), timestampOfLastRequest], rootNodeUpdatesHandler);	
		}
			
		protected function rootNodeUpdatesHandler(result:ResultEvent):void {
			var updates:ArrayCollection = ArrayCollection(result.result);
			
			if (updates != null && updates.length > 0) {
				for each (var update:Update in updates) {
					var nodeFromRegistry:Node = nodeRegistry.getNodeById(update.node.id);	
					if (nodeFromRegistry == null) { // node not registered, probably it isn't visible for this client
						continue;
					}
					if (update is PropertyUpdate) { // property update
						var propertyUpdate:PropertyUpdate = PropertyUpdate(update);
						if (propertyUpdate.isUnset) {
							delete nodeFromRegistry.properties[propertyUpdate.key];
						} else {
							nodeFromRegistry.properties[propertyUpdate.key] = propertyUpdate.value;
						}
						nodeFromRegistry.dispatchEvent(new NodeUpdatedEvent(nodeFromRegistry));	
					} else { // children update
						var childrenUpdate:ChildrenUpdate = ChildrenUpdate(update);
						var targetNodeInRegistry:Node = nodeRegistry.getNodeById(childrenUpdate.targetNode.id);	
						switch (childrenUpdate.type) {
							case ChildrenUpdate.ADDED:													
								if (nodeFromRegistry.children != null && nodeFromRegistry.children.contains(targetNodeInRegistry)) { // child already added, probably after refresh									
								} else if (nodeFromRegistry.children != null) { // node expanded
									var index:Number = -1; // -> add it last
									if (childrenUpdate.targetNodeAddedBefore != null) {
										index = nodeFromRegistry.children.getItemIndex(childrenUpdate.targetNodeAddedBefore);
									}
									addNode(childrenUpdate.targetNode, nodeFromRegistry, index);
								}
								break;
							case ChildrenUpdate.REMOVED:	
								if (targetNodeInRegistry == null // node not registered, probably it isn't visible for this client
									|| nodeFromRegistry.children == null || !nodeFromRegistry.children.contains(targetNodeInRegistry)) { // child already removed, probably after refresh									
								} else {
									removeNode(targetNodeInRegistry, nodeFromRegistry, -1);		
								}
								break;
						}
							
						MindMapDiagramShell(diagramShell).refreshRootModelChildren();
						MindMapDiagramShell(diagramShell).refreshModelPositions(nodeFromRegistry);						
					}					
				}				
				// store last update timestamp
				timestampOfLastRequest = Update(updates.getItemAt(updates.length - 1)).timestamp;
			}
		}	
		
		/* REFRESH */
		
		public function refresh(node:Node):void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.refresh", 
				[getNodeWithVisibleChildren(node)], 
				function (result:ResultEvent):void {
					refreshHandler(node, NodeWithVisibleChildren(result.result));
					
					MindMapDiagramShell(diagramShell).refreshRootModelChildren();
					MindMapDiagramShell(diagramShell).refreshModelPositions(node);});
		}
		
		private function getNodeWithVisibleChildren(node:Node):NodeWithVisibleChildren {
			var nodeWithVisibleChildren:NodeWithVisibleChildren = new NodeWithVisibleChildren();
			nodeWithVisibleChildren.node = Node(ObjectUtil.clone(node));
			nodeWithVisibleChildren.node.properties = null; // properties not needed
			
			if (node.children != null) {
				for each (var child:Node in node.children) {
					if (nodeWithVisibleChildren.visibleChildren == null) {
						nodeWithVisibleChildren.visibleChildren = new ArrayCollection();
					}
					nodeWithVisibleChildren.visibleChildren.addItem(getNodeWithVisibleChildren(child));
				}
			}
			return nodeWithVisibleChildren;
		}
		
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
						if (currentChildNode.id == newChildWithVisibleChildren.node.id) {
							exists = true;
							break;
						}
					}
					if (exists) {
						// child exists -> refresh its structure
						refreshHandler(currentChildNode, newChildWithVisibleChildren);
						// store, for the new child, its index in current list
						newNodeToCurrentNodeIndex[newChildWithVisibleChildren.node.id] = i;
					} else {
						// child doesn't exist in new list -> remove it from parent
						removeNode(currentChildNode, node);
					}
				}
			}
			
			// perform merge 
			
			for (i = 0; i < nodeWithVisibleChildren.visibleChildren.length; i++) {	
				var newChildNode:Node = NodeWithVisibleChildren(nodeWithVisibleChildren.visibleChildren.getItemAt(i)).node;
				if (!newNodeToCurrentNodeIndex.hasOwnProperty(newChildNode.id)) { // new child doesn't exist in current list -> add it
					addNode(newChildNode, node, i);
				} else if (newNodeToCurrentNodeIndex[newChildNode.id] != i) { // new child exists in current list, but different indexes -> get current child and move it to new index
					currentChildNode = nodeRegistry.getNodeById(newChildNode.id);
					removeNode(currentChildNode, node);
					addNode(currentChildNode, node, i);
				}
			}
		}
		
	}
}