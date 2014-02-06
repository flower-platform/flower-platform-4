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
package org.flowerplatform.flex_client.core.mindmap.controller {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeController extends ControllerBase implements IMindMapModelController {
		
		public function NodeController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		private function get mindMapDiagramShell():MindMapEditorDiagramShell {
			return MindMapEditorDiagramShell(diagramShell);
		}
				
		public function getChildren(model:Object):IList {			
			return Node(model).children;
		}
					
		public function getExpanded(model:Object):Boolean {
			return Node(model).children != null && Node(model).children.length > 0;
		}
		
		public function setExpanded(model:Object, value:Boolean):void {
			if (value) {
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.getChildren", [Node(model), true], function(result:ResultEvent):void {				
					getChildrenForNodeIdCallbackHandler(Node(model), result);
				});					
			} else {				
				disposeModel(model);				
			}		
		}
		
		public function getSide(model:Object):int {
			return Node(model).side;
		}
		
		public function setSide(model:Object, value:int):void {
//			Node(model).side = value;
		}
		
		public function isRoot(model:Object):Boolean {			
			return Node(model).parent == null;
		}
				
		private function getChildrenForNodeIdCallbackHandler(node:Node, result:ResultEvent):void {
			for each (var child:Node in ArrayCollection(result.result)) {
				child.parent = node;
			}
			node.children = ArrayCollection(result.result);			
			MindMapDiagramShell(diagramShell).refreshRootModelChildren();
			
			MindMapDiagramShell(diagramShell).refreshModelPositions(node);
		}
		
		public function disposeModel(model:Object, disposeModel:Boolean = false):void {
			disposeModelHandlerRecursive(model, disposeModel);
			
			Node(model).children = null;
			mindMapDiagramShell.refreshRootModelChildren();
			
			MindMapDiagramShell(diagramShell).refreshModelPositions(model);
			MindMapDiagramShell(diagramShell).shouldRefreshVisualChildren(diagramShell.rootModel);
		}
		
		private function disposeModelHandlerRecursive(model:Object, disposeModel:Boolean = false):void {
			if (Node(model).properties["hasChildren"] && Node(model).children != null) {
				for (var i:int=0; i < Node(model).children.length; i++) {
					disposeModelHandlerRecursive(Node(model).children.getItemAt(i), true);
				}		
			}
			if (disposeModel) {
				diagramShell.unassociateModelFromRenderer(model, diagramShell.getRendererForModel(model), true);
			}
		}		
		
	}
}