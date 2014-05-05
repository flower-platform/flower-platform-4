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
package org.flowerplatform.flex_client.mindmap.action {
	
	import flash.geom.Point;
	import flash.net.getClassByAlias;
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.shortcut.Shortcut;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AbstractNavigateAction extends ActionBase {
		
		protected var appendNodesToCurrentSelection:Boolean = false;
		
		public function AbstractNavigateAction(label:String, shortcut:Shortcut, appendNodesToCurrentSelection:Boolean = false) {
			if (appendNodesToCurrentSelection) {
				this.id += "+";
			}
			this.appendNodesToCurrentSelection = appendNodesToCurrentSelection;
			this.label = appendNodesToCurrentSelection ? Resources.getMessage("mindmap.navigate.action.append", [label]) : label;
			
			FlexUtilGlobals.getInstance().keyBindings.registerBinding(shortcut, id);			
		}
		
		private function getSibling(node:Node, context:DiagramShellContext, side:int, previous:Boolean):Node {
			var parent:Node = node.parent;
			if (parent == null) {
				return null;
			}
			var children:IList = MindMapDiagramShell(context.diagramShell).getChildrenBasedOnSide(context, parent, side);
			if (children != null && children.length > 0) {
				for (var i:int = 0; i < children.length; i++) {
					if (children.getItemIndex(node) + (previous ? -1 : 1) == i) {
						return Node(children.getItemAt(i));
					}
				}
			}
			return null;
		}
		
		private function addBranchNodes(node:Node, context:DiagramShellContext, nodes:Array):void {
			if (node.children == null || node.children.length == 0) {
				return;
			}
			for (var i:int = 0; i < node.children.length; i++) {
				var child:Node = Node(node.children.getItemAt(i));
				nodes.push(child);
				addBranchNodes(child, context, nodes);
			}			
		}
						
		protected function getNodes_navigateUpDown(node:Node, context:DiagramShellContext, page:Boolean, down:Boolean):Array {
			var sibling:Node = null;
			var children:IList;
			
			var parent:Node = node;
			var side:int = MindMapDiagramShell(context.diagramShell).getModelController(context, node).getSide(context, node);
			
			// get next/previous sibling for node
			
			// if no sibling found for current node -> get next/previous sibling for parent node 
			do {
				sibling = getSibling(parent, context, side, !down);
				parent = parent.parent;
			} while (sibling == null && parent != null);
								
			if (sibling != null) {
				// sibling found -> iterate on sibling's branch and try to get the first/last child having the depth closer to the initial node's depth -> that will be our new sibling				
				while (context.diagramShell.getDynamicObject(context, sibling).depth < context.diagramShell.getDynamicObject(context, node).depth) {
					children = MindMapDiagramShell(context.diagramShell).getChildrenBasedOnSide(context, sibling, side);
					if (children.length > 0) {
						sibling = Node(children.getItemAt(down ? 0 : children.length - 1));
					} else {
						break;
					}
				}
			}
			var nodes:Array = [];		
			if (sibling != null) {
				// sibling found 
				if (page) {
					// page down/up -> get last/first child from sibling's parent, if append, add also all nodes found in the way
					children = MindMapDiagramShell(context.diagramShell).getChildrenBasedOnSide(context, sibling.parent, side);
					if (appendNodesToCurrentSelection) {
						for (var i:int = (down ? children.getItemIndex(sibling) : 0); i < (down ? children.length : children.getItemIndex(sibling) + 1); i++) {
							nodes.push(Node(children.getItemAt(i)));
						}					
					} else {
						nodes.push(down ? Node(children.getItemAt(children.length - 1)) : Node(children.getItemAt(0)));
					}
				} else {
					// down/up -> we found our sibling to select
					nodes.push(sibling);
				}
			}
			return nodes;
		}
		
		protected function getNodes_navigateLeftRight(node:Node, context:DiagramShellContext, direction:int):Array {
			var nodes:Array;
			
			// if not root and must get nodes from the opposite direction -> get parent node
			if (node.parent != null && direction != ControllerUtils.getMindMapModelController(context, node).getSide(context, node)) {
				nodes = [node.parent];
				if (appendNodesToCurrentSelection) { 
					// add all nodes from parent's branch 					
					addBranchNodes(node.parent, context, nodes);					
				}
				return nodes;
			}		
			
			// if node has children, but it's collapsed -> expand node and after getting all children from server, run this action again to select the preferred nodes
			if (Boolean(node.properties[CoreConstants.HAS_CHILDREN]).valueOf() && !ControllerUtils.getMindMapModelController(context, node).getExpanded(context, node)) {
				context[CoreConstants.HANDLER] = run; 
				ControllerUtils.getMindMapModelController(context, node).setExpanded(context, node, true);				
				return null;
			}

			// node is expanded or it doesn't have children
			
			// no children -> return
			var children:IList = MindMapDiagramShell(context.diagramShell).getChildrenBasedOnSide(context, node, direction);
			if (children == null || children.length == 0) {
				return null;
			}
			
			// node is expanded and has children
			
			if (appendNodesToCurrentSelection) {
				// add all nodes from node's branch
				nodes = [];
				addBranchNodes(node, context, nodes);
				return nodes;
			}
			
			// otherwise get the preferred node
			
			// preferred node = the nearest child node (the distance between node and a child is the smallest)
			var preferredChild:Node;
			
			var dynamicObject:Object = context.diagramShell.getDynamicObject(context, node);
			var pointA:Point = new Point(dynamicObject.x + dynamicObject.width/2, dynamicObject.y + dynamicObject.height/2);
								
			var minDistance:Number = int.MAX_VALUE;
			for (var i:int = 0; i < children.length; i++) {
				var child:Node = Node(children.getItemAt(i));
				var childDynamicObject:Object = context.diagramShell.getDynamicObject(context, child);
				var pointB:Point = new Point(childDynamicObject.x + childDynamicObject.width/2, childDynamicObject.y + childDynamicObject.height/2);
				
				var distance:Number = Math.pow(pointB.x - pointA.x, 2) + Math.pow(pointB.y - pointA.y, 2)
				if (distance < minDistance) {
					minDistance = distance;
					preferredChild = child;
				}				
			}
			return [preferredChild];					
		}
				
		protected function getNodes(node:Node, context:DiagramShellContext):Array {
			throw new Error("This must be implemented!");
		}
		
		override public function get showInMenu():Boolean {
			return false;
		}
		
		override public function get visible():Boolean {
			// get current active editor
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
			var editor:UIComponent = workbench.getEditorFromViewComponent(workbench.getActiveView());
			
			if (!(editor is MindMapEditorFrontend)) { // not a mindmap editor
				return false;
			}
			
			return MindMapEditorFrontend(editor).diagramShell != null && // may happen in mobile, at startup
				MindMapEditorFrontend(editor).diagramShell.mainSelectedItem != null && // has at least one node selected 
				MindMapEditorFrontend(editor).diagramShell.mainSelectedItem is Node;
		}
		
		override public function run():void {
			// get current active mindmap editor
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
			var editor:MindMapEditorFrontend = MindMapEditorFrontend(workbench.getEditorFromViewComponent(workbench.getActiveView()));			
			
			var context:DiagramShellContext = new DiagramShellContext(editor.diagramShell);
			
			// get nodes to be selected
			var nodes:Array = getNodes(Node(editor.diagramShell.mainSelectedItem), context);
			if (nodes == null || nodes.length == 0) {
				return;
			}
		
			if (!appendNodesToCurrentSelection) { // don't append -> reset current selection
				try {					
					// Because an addItem is called after, the eventsCanBeIgnored is set to true,
					// this way listeners can limit the number of unwanted events.
					editor.diagramShell.selectedItems.eventsCanBeIgnored = true;
					editor.diagramShell.selectedItems.removeAll();							
				} finally {
					editor.diagramShell.selectedItems.eventsCanBeIgnored = false;
				}
			}
			
			// add nodes to selection
			for (var i:int = 0; i < nodes.length; i++) {
				editor.diagramShell.selectedItems.addItem(nodes[i]);
			}
						
		}
	}
}
