package org.flowerplatform.flex_client.core.mindmap.update {
	
	import flash.utils.Dictionary;
	
	import mx.core.mx_internal;
	
	import org.flowerplatform.flex_client.core.mindmap.event.NodeRemovedEvent;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	
	use namespace mx_internal;
		
	/**
	 * @see NodeUpdateProcessor
	 * @author Cristina Constantinescu
	 */ 
	public class NodeRegistry {
		
		private var _registry:Dictionary = new Dictionary();
						
		public function getNodeById(id:String):Node {
			return Node(registry[id]);
		}
		
		mx_internal function get registry():Dictionary {
			return _registry;
		}
				
		public function clearRegistry():void {
			_registry = new Dictionary();
		}
		
		public function registerNode(node:Node):void {			
			registry[node.fullNodeId] = node;
		}
		
		public function unregisterNode(nodeId:String):void {
			var nodeFromRegistry:Node = registry[nodeId];
			if (nodeFromRegistry != null) {
				nodeFromRegistry.dispatchEvent(new NodeRemovedEvent(nodeFromRegistry));
				delete registry[nodeId];
			}
		}
						
	}
}
