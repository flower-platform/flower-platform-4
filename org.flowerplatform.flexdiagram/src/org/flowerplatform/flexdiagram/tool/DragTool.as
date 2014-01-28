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
package org.flowerplatform.flexdiagram.tool {
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DragTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "DragTool";
		
		public function DragTool(diagramShell:DiagramShell)	{
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DRAG);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			var renderer:IVisualElement = getRendererFromDisplayCoordinates();
			if (renderer is DiagramRenderer) {
				return false;
			}
			if (renderer is IDataRenderer) {
				var model:Object = getModelWithDragController(renderer);
				return diagramShell.getControllerProvider(model).getDragController(model) != null &&
					   (diagramShell.selectedItems.getItemIndex(model) != -1);						
			}
			return false;
		}
				
		override public function activateAsMainTool():void {			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
				
			context.initialMousePoint = diagramShell.convertCoordinates(
				new Rectangle(
					UIComponent(FlexGlobals.topLevelApplication).mouseX, 
					UIComponent(FlexGlobals.topLevelApplication).mouseY),
				UIComponent(FlexGlobals.topLevelApplication),
				diagramRenderer).topLeft;
			
			var acceptedDraggableModels:IList = getAcceptedDraggableModelsFromSelection(diagramShell.selectedItems);
			for (var i:int = 0; i < acceptedDraggableModels.length; i++) {					
				var model:Object = acceptedDraggableModels.getItemAt(i);				
				diagramShell.getControllerProvider(model).getDragController(model)
					.activate(model, context.initialMousePoint.x, context.initialMousePoint.y);					
			}
			context.draggableItems = acceptedDraggableModels;		
			
			super.activateAsMainTool();
		}
		
		override public function deactivateAsMainTool():void {			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			
			for (var i:int = 0; i < ArrayList(context.draggableItems).length; i++) {
				var model:Object = ArrayList(context.draggableItems).getItemAt(i);
				diagramShell.getControllerProvider(model).getDragController(model).deactivate(model);												
			}		
			
			delete context.initialMousePoint;			
			delete context.draggableItems;
			
			super.deactivateAsMainTool();
		}
		
		protected function mouseMoveHandler(event:MouseEvent):void {			
			if (event.buttonDown) {				
				 var mousePoint:Point = diagramShell.convertCoordinates(
					new Rectangle(
						event.stageX, 
						event.stageY),
					UIComponent(FlexGlobals.topLevelApplication),
					diagramRenderer).topLeft;
				var deltaX:int = mousePoint.x - context.initialMousePoint.x;
				var deltaY:int = mousePoint.y - context.initialMousePoint.y;
				
				for (var i:int = 0; i < ArrayList(context.draggableItems).length; i++) {
					var model:Object = ArrayList(context.draggableItems).getItemAt(i);
					diagramShell.getControllerProvider(model).getDragController(model).drag(model, deltaX, deltaY);										
				}
			} else {				
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		protected function mouseUpHandler(event:MouseEvent):void {
			for (var i:int = 0; i < ArrayList(context.draggableItems).length; i++) {
				var model:Object = ArrayList(context.draggableItems).getItemAt(i);
				diagramShell.getControllerProvider(model).getDragController(model).drop(model);				
			}
			diagramShell.mainToolFinishedItsJob();
		}
		
		protected function getAcceptedDraggableModelsFromSelection(selection:IList):IList {
			var acceptedDraggableModels:ArrayList = new ArrayList();
			for (var i:int = 0; i < selection.length; i++) {					
				var model:Object = selection.getItemAt(i);					
				var dragController:IDragController = diagramShell.getControllerProvider(model).getDragController(model);
				if (dragController != null) {
					acceptedDraggableModels.addItem(model);								
				}
			}	
			return acceptedDraggableModels;
		}
		
		private function getModelWithDragController(renderer:IVisualElement):Object {			
			if (renderer is IDataRenderer) {	
				var model:Object = IDataRenderer(renderer).data;
				if (renderer is DiagramRenderer) {
					return model;
				}				
				if (diagramShell.getControllerProvider(model).getDragController(model) != null) {
					return model;
				}				
			}	
			return getModelWithDragController(IVisualElement(renderer.parent));	
		}
	}
}