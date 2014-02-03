package org.flowerplatform.flex_client.core.mindmap.update {
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.Diagram;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.ChildrenListUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.ClientNodeStatus;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.NodeUpdate;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	
	use namespace mx_internal;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeUpdateProcessor {
		
		private static const FULL_CHILDREN_LIST_KEY:String = "fullChildrenList";
		
		private var _nodeRegistry:NodeRegistry = new NodeRegistry();
				
		protected var diagramShell:DiagramShell;
				
		public function NodeUpdateProcessor(diagramShell:DiagramShell) {
			this.diagramShell = diagramShell;
		}
		
		mx_internal function get nodeRegistry():NodeRegistry {
			return _nodeRegistry;
		}
		
		private function getClientNodeStatus(node:Node):ClientNodeStatus {
			var clientNodeStatus:ClientNodeStatus = new ClientNodeStatus();
			if (node != null) {
				clientNodeStatus.nodeId = node.id;
				clientNodeStatus.timestamp = node.properties["timestamp"];
				
				if (node.children != null) {
					for each (var child:Node in node.children) {
						if (clientNodeStatus.visibleChildren == null) {
							clientNodeStatus.visibleChildren = new ArrayCollection();
						}
						clientNodeStatus.visibleChildren.addItem(getClientNodeStatus(child));
					}
				}
			}
			return clientNodeStatus;
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
			var context:Object = new Object();
			context[FULL_CHILDREN_LIST_KEY] = true;
			
			checkForNodeUpdates(node, context);
		}
		
		public function removeChildren(node:Node, refreshChildrenAndPositions:Boolean = true):void {
			if (node.children != null) {
				for each (var child:Node in node.children) {
					removeChildren(child, false);
					nodeRegistry.unregisterNode(child.id);
				}		
				node.children = null;
				
				if (refreshChildrenAndPositions) {
					MindMapDiagramShell(diagramShell).refreshDiagramChildren();
					MindMapDiagramShell(diagramShell).refreshNodePositions(node);
				}
			}			
		}
		
		public function checkForNodeUpdates(node:Node, context:Object = null):void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"mindMapService", 
				"checkForNodeUpdates", 
				[getClientNodeStatus(node), context], nodeUpdatesHandler);	
		}
		
		protected function nodeUpdatesHandler(result:ResultEvent):void {
			var nodeUpdate:NodeUpdate = NodeUpdate(result.result);
			
			applyNodeUpdates(nodeUpdate);
			
			if (nodeUpdate.childrenListUpdates != null) {
				for each (var update:ChildrenListUpdate in nodeUpdate.childrenListUpdates) {
					var node:Node = nodeRegistry.getNodeById(update.parentNodeId);
					
					switch (update.type) {						
						case ChildrenListUpdate.ADDED:
							var nodeToAdd:Node = Node(update.node);
							var nodeInRegistry:Node = nodeRegistry.getNodeById(nodeToAdd.id);							
							if (node.children != null && node.children.contains(nodeInRegistry)) {									
							} else {
								addNode(nodeToAdd, node, update.index);
							}
							break;
						case ChildrenListUpdate.REMOVED:
							var nodeToRemove:Node = nodeRegistry.getNodeById(String(update.node));
							removeNode(nodeToRemove, node, update.index);					
							break;
					}
															
					MindMapDiagramShell(diagramShell).refreshDiagramChildren();
					MindMapDiagramShell(diagramShell).refreshNodePositions(node);
				}				
			}
		}
		
		protected function applyNodeUpdates(nodeUpdate:NodeUpdate):void {
			var node:Node = nodeRegistry.getNodeById(nodeUpdate.nodeId);
			
			// fullChildrenList has priority
			if (nodeUpdate.fullChildrenList != null) {
				if (node == null) {	// root node
					if (Diagram(diagramShell.rootModel).rootNode != null) {
						removeNode(Diagram(diagramShell.rootModel).rootNode);
					}
					node = Node(nodeUpdate.fullChildrenList.getItemAt(0));
					node.side = MindMapDiagramShell.NONE;
					
					addNode(node);
							
					Diagram(diagramShell.rootModel).rootNode = node;
					// by default, root node is considered expanded
					IMindMapControllerProvider(diagramShell.getControllerProvider(node)).getMindMapModelController(node).setExpanded(node, true);
				} else {
					if (node.children != null) {
						while (node.children.length > 0) {
							removeNode(Node(node.children.getItemAt(0)), node, 0);					
						}
					}
					// register each child
					for each (var child:Node in nodeUpdate.fullChildrenList) {
						nodeRegistry.registerNode(child);
						child.parent = node;					
					}
					// set node list of children
					node.children = nodeUpdate.fullChildrenList;
				}		
				// refresh diagram's children and their positions
				MindMapDiagramShell(diagramShell).refreshDiagramChildren();
				MindMapDiagramShell(diagramShell).refreshNodePositions(node);
				
				return;
			}
			
			if (node == null) {
				return;
			}
			// update node properties
			if (nodeUpdate.updatedProperties != null) {
				nodeRegistry.updateNode(node.id, nodeUpdate.updatedProperties);
			}
			
			// recursively apply updates through children
			if (nodeUpdate.nodeUpdatesForChildren != null) {
				for each (var childUpdate:NodeUpdate in nodeUpdate.nodeUpdatesForChildren) {
					applyNodeUpdates(childUpdate);
				}
			}
		}
		
	}
}