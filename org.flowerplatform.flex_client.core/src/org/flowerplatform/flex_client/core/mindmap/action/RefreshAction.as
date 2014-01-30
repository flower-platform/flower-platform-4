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
package org.flowerplatform.flex_client.core.mindmap.action {
	
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.utils.DescribeTypeCache;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.Diagram;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.action.ActionBase;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class RefreshAction extends ActionBase {
		
		private var recursive:Boolean;
		
		private var editorFrontend:MindMapEditorFrontend;
				
		public function RefreshAction(editorFrontend:MindMapEditorFrontend, recursive:Boolean = false) {
			this.editorFrontend = editorFrontend;
			this.recursive = recursive;
			if (recursive) {
				label = CorePlugin.getInstance().getMessage("mindmap.action.refresh.recursive");
				orderIndex = 50;
			} else {
				label = CorePlugin.getInstance().getMessage("mindmap.action.refresh");
				orderIndex = 40;
			}			
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			refresh(Node(selection.getItemAt(0)));
		}
		
		private function refresh(node:Node):void {			
			if (recursive) {
				for each (var child:Node in node.children) {
					refresh(child);
				}
			}
			CorePlugin.getInstance().mindMapService.refresh(node.id, refreshCallbackHandler);
		}
		
		private function refreshCallbackHandler(result:ResultEvent):void {
			var diagramShell:MindMapDiagramShell = MindMapDiagramShell(editorFrontend.diagramShell);
			
			var oldNode:Node = CorePlugin.getInstance().mindMapService.getNodeFromId(Node(Diagram(diagramShell.rootModel).rootNode), result.result[0]);
			var newNode:Node = result.result[1];
			copyProperties(newNode, oldNode, false);			
		}
		
		// TODO CC: temporary code (to be refactored when update mechanism implemented)
		protected function copyProperties(source:IEventDispatcher, dest:IEventDispatcher, postProcessOnly:Boolean):void {			
			var classInfo:XML = DescribeTypeCache.describeType(dest).typeDescription;
			for each (var v:XML in classInfo..accessor) {
				if (v.@name != null && v.@access != 'readonly' && !ObjectUtil.hasMetadata(dest, v.@name, 'Transient')) {
					copyProperty(source, dest, v.@name, postProcessOnly);
				}
			}
		}
		
		// TODO CC: temporary code (to be refactored when update mechanism implemented)
		protected function copyProperty(source:Object, dest:Object, propertyName:String, postProcessOnly:Boolean):void {
			if (!postProcessOnly && dest[propertyName] != source[propertyName]) {
				dest[propertyName] = source[propertyName];
			}
		}
	}
}