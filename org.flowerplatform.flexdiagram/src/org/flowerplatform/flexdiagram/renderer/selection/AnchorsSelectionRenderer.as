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
package org.flowerplatform.flexdiagram.renderer.selection {
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.ui.Multitouch;
	
	import mx.core.IVisualElement;
	import mx.events.MoveEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.tool.ResizeTool;
	import org.flowerplatform.flexdiagram.ui.ResizeAnchor;

	/**	
	 * @author Cristina Constantinescu
	 */
	public class AnchorsSelectionRenderer extends AbstractSelectionRenderer {
		
		[Embed(source="../icons/diag1Cursor.gif")]		
		protected var diag1Cursor:Class;
		
		[Embed(source="../icons/diag2Cursor.gif")]		
		protected var diag2Cursor:Class;
		
		[Embed(source="../icons/horizCursor.gif")]		
		protected var horizCursor:Class;
		
		[Embed(source="../icons/vertCursor.gif")]		
		protected var vertCursor:Class;
		
		[Embed(source="../icons/crossCursor.gif")]
		protected var crossCursor:Class;
		
		override public function activate(diagramShell:DiagramShell, target:IVisualElement):void {
			super.activate(diagramShell, target);			
			
			// set the handler that move/resize anchors with parent renderer.
			DisplayObject(target).addEventListener(MoveEvent.MOVE, handleTargetMoveResize); 
			DisplayObject(target).addEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);	
			if(!Multitouch.supportsGestureEvents) {  // don't add cursor on touch screen
				DisplayObject(diagramShell.diagramRenderer).addEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
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
		override public function deactivate():void {
			// remove move/resize listeners
			DisplayObject(target).removeEventListener(MoveEvent.MOVE, handleTargetMoveResize);
			DisplayObject(target).removeEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);	
			if(!Multitouch.supportsGestureEvents) { // don't add cursor on touch screen
				DisplayObject(diagramShell.diagramRenderer).removeEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
			}
			
			super.deactivate();
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
						currentCursor = diag2Cursor;
						break;
					case ResizeAnchor.LEFT_MIDDLE:
						currentCursor = horizCursor;
						break;
					case ResizeAnchor.LEFT_UP:
						currentCursor = diag1Cursor;
						break;
					case ResizeAnchor.MIDDLE_DOWN:
						currentCursor = vertCursor;
						break;
					case ResizeAnchor.MIDDLE_UP:
						currentCursor = vertCursor;
						break;
					case ResizeAnchor.RIGHT_DOWN:
						currentCursor = diag1Cursor;
						break;
					case ResizeAnchor.RIGHT_MIDDLE:
						currentCursor = horizCursor;
						break;
					case ResizeAnchor.RIGHT_UP:
						currentCursor = diag2Cursor;
						break;
				}
				cursorManager.removeAllCursors();
				cursorManager.setCursor(currentCursor, 2, -16, -16);		
			} else if (!(diagramShell.mainTool is ResizeTool)) {
				cursorManager.removeAllCursors();
			}				
		}
	}
	
}