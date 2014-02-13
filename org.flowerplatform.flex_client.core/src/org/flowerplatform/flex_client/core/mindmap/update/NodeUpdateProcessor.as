package org.flowerplatform.flex_client.core.mindmap.update {
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
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
		
		public function requestChildren(node:Node):void {		
			if (node == null) {
				node = new Node();
				node.type = "freeplaneNode";
				node.resource = "mm://path_to_resource";
			}
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.getChildren", 
				[node, true], 
				function (result:ResultEvent):void {setChildren(node, ArrayCollection(result.result));});	
		}
		
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
		
		public function checkForUpdates():void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"updateService.getUpdates",	
				[Node(MindMapDiagramShell(diagramShell).getRoot()), timestampOfLastRequest], rootNodeUpdatesHandler);	
		}
		
		protected function setChildren(node:Node, children:ArrayCollection):void {
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
								} else {
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
		
	}
}