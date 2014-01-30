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
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.Keyboard;
	import flash.ui.Mouse;
	import flash.ui.Multitouch;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.managers.CursorManager;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	
	import spark.components.Scroller;
	import spark.core.IViewport;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ScrollTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "ScrollTool";
		
		[Embed(source="../icons/move_cursor.png")]
		private var _moveCursor:Class;
				
		public function ScrollTool(diagramShell:DiagramShell) {
			super(diagramShell);		
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DRAG);
		}
	
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {	
			// TODO CC: de revazut
			context.initialX = initialEvent.stageX + diagramRenderer.scrollRect.x;
			context.initialY = initialEvent.stageY + diagramRenderer.scrollRect.y;
			
			return getRendererFromDisplayCoordinates() is DiagramRenderer;
		}
		
		override public function activateAsMainTool():void {			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			if(!Multitouch.supportsGestureEvents) { // don't show cursor on touch screens
				diagramRenderer.cursorManager.setCursor(_moveCursor, 2, -16, -16);
			}
			super.activateAsMainTool();
		}
				
		override public function deactivateAsMainTool():void {			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			if(!Multitouch.supportsGestureEvents) {
				diagramRenderer.cursorManager.removeAllCursors();
			}
			delete context.initialX;
			delete context.initialY;
			
			super.deactivateAsMainTool();
		}
			
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {		
				scrollUnscaledPointToDiagramScreenPoint(context.initialX, context.initialY, event.stageX, event.stageY);
			} else {
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		// TODO CC: de revazut
		private function scrollUnscaledPointToDiagramScreenPoint(unscaledPointX:Number, unscaledPointY:Number, diagramScreenPointX:Number, diagramScreenPointY:Number):void {
			var localPoint:Point = new Point();		
			localPoint.x = diagramScreenPointX + diagramRenderer.scrollRect.x;
			localPoint.y = diagramScreenPointY + diagramRenderer.scrollRect.y;
			
			var deltaX:int = unscaledPointX - localPoint.x;
			var deltaY:int = unscaledPointY -  localPoint.y;				
						
			var newPosX:Number = diagramRenderer.horizontalScrollPosition + deltaX;				
			diagramRenderer.horizontalScrollPosition = newPosX;
				
			var newPosY:Number = diagramRenderer.verticalScrollPosition + deltaY;					
			diagramRenderer.verticalScrollPosition = newPosY;
					
//			diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
		}
		
		private function mouseUpHandler(event:MouseEvent):void {			
			diagramShell.mainToolFinishedItsJob();
		}
		
	}	
}