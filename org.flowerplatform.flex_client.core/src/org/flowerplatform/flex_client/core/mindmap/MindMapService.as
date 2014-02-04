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
package org.flowerplatform.flex_client.core.mindmap {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapService {
		
		public static const ID:String = "nodeService";
		
		public function getNodeFromId(node:Node, nodeId:String):Node {
			if (node.id == nodeId) {
				return node;
			}
			if (node.children != null) {
				for each (var child:Node in node.children) {
					var n:Node = getNodeFromId(child, nodeId);
					if (n != null) {
						return n;
					}
				}
			}
			return null;
		}
		
		public function getChildrenForNodeId(node:Node, callbackFunction:Function):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "getChildren", [node, true], callbackFunction);
		}
		
		public function reload(callbackFunction:Function):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "load", null, callbackFunction);
		}
		
		public function save():void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "save");
		}
		
		public function refresh(nodeId:String, callbackFunction:Function):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "refresh", [nodeId], callbackFunction);
		}
		
		public function setBody(node:Node, newBodyValue:String):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "setProperty", [node, "body", newBodyValue]);
		}
		
		public function addNode(parentNode:Node, child:Node):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "addChild", [parentNode, child]);
		}
		
		public function removeNode(parent:Node, child:Node):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "removeChild", [parent, child, false]);
		}
		
		public function moveNode(nodeId:String, newParentNodeId:String, newIndex:int):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "moveNode", [nodeId, newParentNodeId, newIndex]);
		}
	}
}