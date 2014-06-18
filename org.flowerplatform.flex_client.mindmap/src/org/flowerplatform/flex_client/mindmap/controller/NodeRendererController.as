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
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.event.NodeRemovedEvent;
	import org.flowerplatform.flex_client.mindmap.MindMapConnector;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRendererController extends MindMapModelRendererController {
		
		public function NodeRendererController(rendererClass:Class, orderIndex:int = 0) {
			super(rendererClass, orderIndex);
			mindMapConnectorClass = MindMapConnector;
		}
		
		override public function associatedModelToRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {				
			Node(model).addEventListener(NodeRemovedEvent.NODE_REMOVED, function (event:NodeRemovedEvent):void {nodeRemovedHandler(event, context);});			
		}
		
		override public function unassociatedModelFromRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {		
			if (isModelDisposed) {			
				Node(model).removeEventListener(NodeRemovedEvent.NODE_REMOVED, function (event:NodeRemovedEvent):void {nodeRemovedHandler(event, context);});				
			}
			super.unassociatedModelFromRenderer(context, model, renderer, isModelDisposed);	
		}
		
		protected function nodeRemovedHandler(event:NodeRemovedEvent, context:DiagramShellContext):void {			
			context.diagramShell.unassociateModelFromRenderer(context, event.node, context.diagramShell.getRendererForModel(context, event.node), true);			
		}
		
	}
}
