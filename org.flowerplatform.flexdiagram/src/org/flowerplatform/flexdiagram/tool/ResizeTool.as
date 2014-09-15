/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.tool.controller.ResizeController;
	import org.flowerplatform.flexdiagram.ui.ResizeAnchor;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ResizeTool extends Tool implements IWakeUpableTool {
			
		public static const ID:String = "ResizeTool";
		
		public function ResizeTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DOWN);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			return getResizeAnchorFromDisplayCoordinates() != null;
		}
		
		override public function activateAsMainTool():void {			
			context.initialMousePoint = diagramShell.convertCoordinates(
				new Rectangle(
					UIComponent(FlexGlobals.topLevelApplication).mouseX, 
					UIComponent(FlexGlobals.topLevelApplication).mouseY),
					UIComponent(FlexGlobals.topLevelApplication),
					diagramRenderer).topLeft;
			var resizeAnchor:ResizeAnchor = getResizeAnchorFromDisplayCoordinates();		
			context.resizeType = resizeAnchor.type;
			context.shellContext = diagramShell.getNewDiagramShellContext();
			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
				var model:Object = diagramShell.selectedItems.getItemAt(i);
				var resizeController:ResizeController = ControllerUtils.getResizeController(context.shellContext, model);
				if (resizeController != null) {
					resizeController.activate(context.shellContext, model);
				}				
			}	
			super.activateAsMainTool();
		}
		
		override public function deactivateAsMainTool():void {
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
				var model:Object = diagramShell.selectedItems.getItemAt(i);
				var resizeController:ResizeController = ControllerUtils.getResizeController(context.shellContext, model);
				if (resizeController != null) {
					resizeController.deactivate(context.shellContext, model);
				}				
			}	
			
			delete context.initialMousePoint;
			delete context.resizeType;
			delete context.shellContext;
			
			super.deactivateAsMainTool();
		}
				
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {
				var mousePoint:Point = diagramShell.convertCoordinates(
					new Rectangle(
						event.stageX, 
						event.stageY),
						UIComponent(FlexGlobals.topLevelApplication),
						diagramRenderer).topLeft;	
								
				var deltaX:int = mousePoint.x - context.initialMousePoint.x;
				var deltaY:int = mousePoint.y - context.initialMousePoint.y;		
				
				for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
					var model:Object = diagramShell.selectedItems.getItemAt(i);
					var resizeController:ResizeController = ControllerUtils.getResizeController(context.shellContext, model);
					if (resizeController != null) {
						resizeController.drag(context.shellContext, model, deltaX, deltaY, context.resizeType);
					}				
				}				
			} else {
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent = null):void {
			for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
				var model:Object = diagramShell.selectedItems.getItemAt(i);
				var resizeController:ResizeController = ControllerUtils.getResizeController(context.shellContext, model);
				if (resizeController != null) {
					resizeController.drop(context.shellContext, model);
				}				
			}		
			diagramShell.mainToolFinishedItsJob();
		}
				
		private function getResizeAnchorFromDisplayCoordinates():ResizeAnchor  {
			var stage:Stage = DisplayObject(diagramShell.diagramRenderer).stage;
			var array:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
			
			var anchor:ResizeAnchor;
			var i:int;
			for (i = array.length - 1; i >= 0;  i--) {
				anchor = getResizeAnchorFromDisplay(array[i]);
				if (anchor != null) {					
					return anchor;
				}
			}
			return null;
		}
		
		private function getResizeAnchorFromDisplay(obj:Object):ResizeAnchor {			
			// in order for us to traverse its hierrarchy it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}			
			// traverse all the obj's hierarchy	
			while (obj != null) {				
				if (obj is ResizeAnchor) { // found it					
					return ResizeAnchor(obj);					
				}
				obj = DisplayObject(obj).parent;
			}			
			return null;
		}
	}
}