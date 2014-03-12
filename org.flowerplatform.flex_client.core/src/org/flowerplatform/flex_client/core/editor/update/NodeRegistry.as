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
package org.flowerplatform.flex_client.core.editor.update {
	
	import flash.utils.Dictionary;
	
	import mx.core.mx_internal;
	
	import org.flowerplatform.flex_client.core.editor.update.event.NodeRemovedEvent;
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
