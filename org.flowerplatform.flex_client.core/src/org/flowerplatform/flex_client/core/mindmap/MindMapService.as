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
		
		public static const ID:String = "mindMapService";
		
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
		
		public function getChildrenForNodeId(nodeId:String, callbackFunction:Function):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "getChildrenForNodeId", [nodeId], callbackFunction);
		}
		
		public function reload(callbackFunction:Function):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "reload", null, callbackFunction);
		}
		
		public function save():void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "save");
		}
		
		public function refresh(nodeId:String, callbackFunction:Function):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "refresh", [nodeId], callbackFunction);
		}
		
		public function setBody(nodeId:String, newBodyValue:String):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "setBody", [nodeId, newBodyValue]);
		}
		
		public function addNode(parentNodeId:String, type:String):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "addNode", [parentNodeId, type]);
		}
		
		public function removeNode(nodeId:String):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "removeNode", [nodeId]);
		}
		
		public function moveNode(nodeId:String, newParentNodeId:String, newIndex:int):void {
			CorePlugin.getInstance().serviceLocator.invoke(ID, "moveNode", [nodeId, newParentNodeId, newIndex]);
		}
	}
}