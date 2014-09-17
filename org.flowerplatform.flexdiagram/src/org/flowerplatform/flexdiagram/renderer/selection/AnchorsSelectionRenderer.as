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
package org.flowerplatform.flexdiagram.renderer.selection {
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.ui.Multitouch;
	
	import mx.events.MoveEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.FlexDiagramAssets;
	import org.flowerplatform.flexdiagram.tool.ResizeTool;
	import org.flowerplatform.flexdiagram.ui.ResizeAnchor;

	/**	
	 * @author Cristina Constantinescu
	 */
	public class AnchorsSelectionRenderer extends AbstractSelectionRenderer {
		
		override public function activate(context:DiagramShellContext, model:Object):void {
			super.activate(context, model);			
			
			// set the handler that move/resize anchors with parent renderer.
			DisplayObject(target).addEventListener(MoveEvent.MOVE, handleTargetMoveResize); 
			DisplayObject(target).addEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);	
			if(!Multitouch.supportsGestureEvents) {  // don't add cursor on touch screen
				DisplayObject(context.diagramShell.diagramRenderer).addEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
			}
			// update position
			handleTargetMoveResize(null);
		}
		
		/**
		 * 
		 * Called when we don't need the anchors, 
		 * and also when we don't want the anchors shown.
		 * 
		 */
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			// remove move/resize listeners
			DisplayObject(target).removeEventListener(MoveEvent.MOVE, handleTargetMoveResize);
			DisplayObject(target).removeEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);	
			if(!Multitouch.supportsGestureEvents) { // don't add cursor on touch screen
				DisplayObject(context.diagramShell.diagramRenderer).removeEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
			}
			
			super.deactivate(context, model);
		}
		
		protected function handleTargetMoveResize(event:Event):void {
			setLayoutBoundsPosition(target.x, target.y);
			setLayoutBoundsSize(target.width, target.height);
		}
		
		protected function mouseOverHandler(event:MouseEvent):void {
			var currentCursor:Class = null;
			if (event.target is ResizeAnchor) {
				switch (ResizeAnchor(event.target).type) {
					case ResizeAnchor.LEFT_DOWN:
						currentCursor = FlexDiagramAssets.diag2Cursor;
						break;
					case ResizeAnchor.LEFT_MIDDLE:
						currentCursor = FlexDiagramAssets.horizCursor;
						break;
					case ResizeAnchor.LEFT_UP:
						currentCursor = FlexDiagramAssets.diag1Cursor;
						break;
					case ResizeAnchor.MIDDLE_DOWN:
						currentCursor = FlexDiagramAssets.vertCursor;
						break;
					case ResizeAnchor.MIDDLE_UP:
						currentCursor = FlexDiagramAssets.vertCursor;
						break;
					case ResizeAnchor.RIGHT_DOWN:
						currentCursor = FlexDiagramAssets.diag1Cursor;
						break;
					case ResizeAnchor.RIGHT_MIDDLE:
						currentCursor = FlexDiagramAssets.horizCursor;
						break;
					case ResizeAnchor.RIGHT_UP:
						currentCursor = FlexDiagramAssets.diag2Cursor;
						break;
				}
				cursorManager.removeAllCursors();
				cursorManager.setCursor(currentCursor, 2, -16, -16);		
			} else if (!(context.diagramShell.mainTool is ResizeTool)) {
				cursorManager.removeAllCursors();
			}				
		}
	}
	
}