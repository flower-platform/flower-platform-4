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
	
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class SelectOrDragToCreateElementTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "SelectOrDragToCreateElementTool";
				
		public static const SELECT_MODE_ADD:String = "add";
		public static const SELECT_MODE_SUBSTRACT:String = "substract";
		
		public function SelectOrDragToCreateElementTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DRAG, 1);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {	
			context.ctrlPressed = initialEvent.ctrlKey;
			context.shiftPressed = initialEvent.shiftKey;
			// used to know if tool activated from WakeUpTool
			context.wakedUp = (getRendererFromDisplayCoordinates() is DiagramRenderer) && (context.ctrlPressed || context.shiftPressed);			
			return context.wakedUp;
		}	
		
		override public function activateAsMainTool():void {			
			if (context.wakedUp) {
				mouseDownHandler();
			} else {
				diagramRenderer.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			}
			super.activateAsMainTool();
		}
		
		override public function deactivateAsMainTool():void {		
			if (!context.wakedUp) {
				diagramRenderer.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			}
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);	
			
			diagramShell.getControllerProvider(diagramShell.rootModel).
				getSelectOrDragToCreateElementController(diagramShell.rootModel).deactivate(diagramShell.rootModel);
			
			delete context.wakedUp;
			delete context.initialMousePoint;			
			delete context.ctrlPressed;
			delete context.shiftPressed;
			
			super.deactivateAsMainTool();
		}		
		
		private function mouseDownHandler(event:MouseEvent = null):void {
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
				
			context.initialMousePoint = globalToDiagram(Math.ceil(diagramRenderer.stage.mouseX), Math.ceil(diagramRenderer.stage.mouseY));			
											
			diagramShell.getControllerProvider(diagramShell.rootModel).
				getSelectOrDragToCreateElementController(diagramShell.rootModel).activate(diagramShell.rootModel, context.initialMousePoint.x, context.initialMousePoint.y, getMode());
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {	
			if (diagramShell.modelToExtraInfoMap[diagramShell.rootModel].waitingToDeactivateDragTool) {
				// don't do nothing, tool waits to be deactivated
				return;
			}
			if (event == null || event.buttonDown) {
				var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));				
				var deltaX:int = mousePoint.x - context.initialMousePoint.x;
				var deltaY:int = mousePoint.y - context.initialMousePoint.y;
				
				diagramShell.getControllerProvider(diagramShell.rootModel).
					getSelectOrDragToCreateElementController(diagramShell.rootModel).drag(diagramShell.rootModel, deltaX, deltaY);				
			} else {				
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent):void {
			if (diagramShell.modelToExtraInfoMap[diagramShell.rootModel].waitingToDeactivateDragTool) {
				// don't do nothing, tool waits to be deactivated
				return;
			}
			diagramShell.getControllerProvider(diagramShell.rootModel).
				getSelectOrDragToCreateElementController(diagramShell.rootModel).drop(diagramShell.rootModel);
		}
		
		private function getMode():String {
			if (context.ctrlPressed) {
				return SELECT_MODE_SUBSTRACT;
			}
			return SELECT_MODE_ADD;
		}
	}
	
}
