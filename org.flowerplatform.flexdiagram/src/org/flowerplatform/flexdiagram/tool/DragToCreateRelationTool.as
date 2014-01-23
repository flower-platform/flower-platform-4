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
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.renderer.selection.AnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.ui.RelationAnchor;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DragToCreateRelationTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "DragToCreateRelationTool";
				
		public function DragToCreateRelationTool(diagramShell:DiagramShell)	{
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DRAG, 1);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			return getRelationAnchorFromDisplayCoordinates() != null;
		}
		
		override public function activateAsMainTool():void {			
			var relationAnchor:RelationAnchor = getRelationAnchorFromDisplayCoordinates();			
			context.model = AnchorsSelectionRenderer(relationAnchor.parent).getTargetModel();
			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			var controller:IDragToCreateRelationController = diagramShell.getControllerProvider(context.model).getDragToCreateRelationController(context.model);
			if (controller == null) {
				diagramShell.mainToolFinishedItsJob();
				return;
			}
			controller.activate(context.model);
			
			super.activateAsMainTool();
		}
		
		override public function deactivateAsMainTool():void {
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			var controller:IDragToCreateRelationController = diagramShell.getControllerProvider(context.model).getDragToCreateRelationController(context.model);
			if (controller != null) {
				controller.deactivate(context.model);
			}
			delete context.model;
			
			super.deactivateAsMainTool();
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {
			if (diagramShell.modelToExtraInfoMap[context.model].waitingToDeactivateDragTool) {
				// don't do nothing, tool waits to be deactivated
				return;
			}
			if (event.buttonDown) {			
				var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
				var deltaX:int = mousePoint.x;
				var deltaY:int = mousePoint.y;
				
				diagramShell.getControllerProvider(context.model).
					getDragToCreateRelationController(context.model).drag(context.model, deltaX, deltaY);
			} else {
				mouseUpHandler();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent = null):void {	
			if (diagramShell.modelToExtraInfoMap[context.model].waitingToDeactivateDragTool) {
				// don't do nothing, tool waits to be deactivated
				return;
			}
			var controller:IDragToCreateRelationController = diagramShell.getControllerProvider(context.model).
				getDragToCreateRelationController(context.model);
			if (controller) {
				var renderer:IVisualElement = getRendererFromDisplayCoordinates(true);
				var model:Object = getModelWithDragToCreateRelationController(renderer);				
				controller.drop(context.model, model);
			}
		}
		
		private function getRelationAnchorFromDisplayCoordinates():RelationAnchor  {
			var stage:Stage = DisplayObject(diagramShell.diagramRenderer).stage;
			var array:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
			
			var anchor:RelationAnchor;
			var i:int;
			for (i = array.length - 1; i >= 0;  i--) {
				anchor = getRelationAnchorFromDisplay(array[i]);
				if (anchor != null) {					
					return anchor;
				}
			}
			return null;
		}
		
		private function getRelationAnchorFromDisplay(obj:Object):RelationAnchor {			
			// in order for us to traverse its hierrarchy it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}			
			// traverse all the obj's hierarchy	
			while (obj != null) {				
				if (obj is RelationAnchor) { // found it					
					return RelationAnchor(obj);					
				}
				obj = DisplayObject(obj).parent;
			}			
			return null;
		}
		
		private function getModelWithDragToCreateRelationController(renderer:IVisualElement):Object {
			if (renderer == null) {
				return null;
			}
			if (renderer is IDataRenderer) {	
				var model:Object = IDataRenderer(renderer).data;
				if (renderer is DiagramRenderer) {
					return model;
				}				
				if (diagramShell.getControllerProvider(model).getDragToCreateRelationController(model) != null) {
					return model;
				}				
			}	
			return getModelWithDragToCreateRelationController(IVisualElement(renderer.parent));	
		}
	}
}
