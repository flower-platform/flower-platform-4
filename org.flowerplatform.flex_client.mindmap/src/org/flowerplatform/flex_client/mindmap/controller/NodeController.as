/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
 * license-end
 */
package org.flowerplatform.flex_client.mindmap.controller {
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.OpenInNewEditorDialog;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeController extends MindMapModelController {
		
		override public function getChildren(context:DiagramShellContext, model:Object):IList {			
			return Node(model).children;
		}
					
		override public function getExpanded(context:DiagramShellContext, model:Object):Boolean {
			return Node(model).children != null && Node(model).children.length > 0;
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Mariana Gheorghe
		 */
		override public function setExpanded(context:DiagramShellContext, model:Object, value:Boolean):void {
			var node:Node = Node(model);
			if (value) {
				// open in new editor?
				if (node.properties[CoreConstants.IS_OPENABLE_IN_NEW_EDITOR]) {
//					var dialog:OpenInNewEditorDialog = new OpenInNewEditorDialog();
//					dialog.node = node;
//					dialog.setResultHandler(new OpenInNewEditorDialogResultHandler(function(result:Object):void {
//						if (result) {
//							// opened in new editor => collapse
//							collapse(context, node);
//						} else {
//							// default behaviour => expand node
//							expand(context, node);
//						}
//					}));
//					dialog.show();
					CorePlugin.getInstance().openEditor(node);
					collapse(context, node);
				} else {
					expand(context, node);
				}
			} else {				
				collapse(context, node);
			}		
		}
		
		private function expand(context:DiagramShellContext, node:Node):void {
			MindMapEditorDiagramShell(context.diagramShell).updateProcessor.requestChildren(context, node);
		}
		
		private function collapse(context:DiagramShellContext, node:Node):void {
			MindMapEditorDiagramShell(context.diagramShell).updateProcessor.removeChildren(context, node);
		}
		
		override public function getSide(context:DiagramShellContext, model:Object):int {
			var mindmapDiagramShell:MindMapEditorDiagramShell = MindMapEditorDiagramShell(context.diagramShell);
			var rootModel:Node = mindmapDiagramShell.updateProcessor.getNodeById(Node(MindMapRootModelWrapper(mindmapDiagramShell.rootModel).model).fullNodeId);
			
			if (rootModel != null && rootModel.properties[CoreConstants.CONTENT_TYPE] == MindMapConstants.MINDMAP_CONTENT_TYPE) {
				//root node is mm file -> get side from provider
				var sideProvider:GenericValueProviderFromDescriptor = NodeControllerUtils.getSideProvider(mindmapDiagramShell.registry, model);
				if (sideProvider != null) {
					var side:int = int(sideProvider.getValue(Node(model)));
					if (side == 0 && Node(model).parent != null) { // no side -> get side from parent
						side = getSide(context, Node(model).parent);
					}
					if (side != 0) { // side found (left/right)
						return side;
					}
				}
			}
			// default side
			return MindMapDiagramShell.POSITION_RIGHT;
		}
		
		override public function setSide(context:DiagramShellContext, model:Object, value:int):void {
		}

		override public function isRoot(context:DiagramShellContext, model:Object):Boolean {			
			return Node(model).parent == null;
		}
	}
}
import org.flowerplatform.flexutil.dialog.IDialogResultHandler;

/**
 * @author Mariana Gheorghe
 */
class OpenInNewEditorDialogResultHandler implements IDialogResultHandler {
	
	private var callbackFunction:Function;
	
	public function OpenInNewEditorDialogResultHandler(callbackFunction:Function) {
		this.callbackFunction = callbackFunction;
	}
	
	public function handleDialogResult(result:Object):void {
		callbackFunction(result);
	}
}