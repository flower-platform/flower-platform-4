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
package com.crispico.flower.util.popup {
	
	import com.crispico.flower.util.panel.CustomTitlePanel;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.core.IFlexDisplayObject;
	import mx.core.IVisualElement;
	import mx.core.mx_internal;
	import mx.effects.Fade;
	import mx.events.CloseEvent;
	import mx.events.EffectEvent;
	import mx.events.FlexEvent;
	import mx.managers.CursorManager;
	import mx.managers.PopUpManager;
	
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	
	use namespace mx_internal;
	
	/**
	 * Implements a resizable popup.
	 * 
	 * A subclass should:
	 * 
	 * <ul>
	 * 	<li>If it has a cancel button, it should use <code>formCancelHandler()</code> as handler, that closes
	 * 		the popup. This method might be of course, overridden if needed.
	 * </ul>
	 * 
	 * A subclass may:
	 * <ul>
	 * 	<li>Override <code>keyDownHandler1()</code>, but should call super.
	 * 	<li>Override <code>handleEscPressed()</code> if no popup message is wanted on ESC.
	 * 	<li>Override <code>getEscMessage()</code> if the ESC popup message needs to be customized.
	 * </ul>
	 * 
	 * @author Florin
	 * @author Cristina
	 * 
	 */	
	public class ResizablePopupWindow extends CustomTitlePanel {
       
        /**
         * Diagonal resize cursor.
         */
        [Bindable]
        [Embed(source="/cursors/resizeCursor.gif")]      
		protected var resizeCursor:Class;
		
		/**
		 * Hoizontal resize cursor
		 * 
		 * @author mircea
		 */
		[Bindable]
		[Embed(source="/cursors/horizCursor.gif")]
		protected var horizCursor:Class ;
		
		/**
		 * Vertical resize cursor.
		 * 
		 * @author mircea
		 */
		[Bindable]
		[Embed(source="/cursors/vertCursor.gif")]    
		protected var vertCursor:Class;
				
		/**
		 * Flag telling when the mouse is down.
		 * 		 
		 */
		protected var mouseIsDown:Boolean = false;
		
		/**
		 * Flag that tells us where the mouse was when the mouseDown
		 * event occured.
		 * Used for restricting resizing of the popup.
		 * 
		 * @author mircea
		 */
		protected var locationOnMouseDown:Number = 0;
		
		/**
		 * Flag that tells when a cursor was changed, so 
		 * we do not call CursorManager.removeAllCursors 
		 * at the every move of the mouse.
		 * 
		 * @author mircea
		 */
		protected var cursorChanged:Boolean = false;
		
		public var closeOnEscape:Boolean = false;
		
		/**
		 * Constants regarding the position of mouse
		 * relativ to the popup
		 * 
		 * @author mircea
		 */
		private const LOCATION_MOUSE_RESIZE_CORNER:Number = 1;
		private const LOCATION_MOUSE_UPPER_EDGE:Number = 2;
		private const LOCATION_MOUSE_LEFT_EDGE:Number = 3;
		private const LOCATION_MOUSE_RIGHT_EDGE:Number = 4;
		private const LOCATION_MOUSE_BOTOOM_EDGE:Number = 5;
		
		/**
		 * Constructor
		 */
		public function ResizablePopupWindow() {
			super();				
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			addEventListener(CloseEvent.CLOSE, formCancelHandler);
			showCloseButton = true;		
		}

		/**
		 * Utility method that displays the popup.
		 */ 
		public function showPopup(initialWidth:Number = NaN, initialHeight:Number = NaN, parent:DisplayObject = null, isModal:Boolean = true, closeOnEscape:Boolean = true):void {
			// Mariana: set the initial width and height only if they do not exceed the minimum values
			if (!isNaN(initialWidth) && initialWidth >= minWidth)
				this.width = initialWidth;
			if (!isNaN(initialHeight) && initialHeight >= minHeight)
				this.height = initialHeight;
			if (parent == null)
				parent = Application.application as DisplayObject;
			PopUpManager.addPopUp(this, parent, isModal);
			PopUpManager.centerPopUp(this);			
			var fade:Fade = new Fade();
			fade.alphaFrom = 0;
			fade.alphaTo = 1;
			if (!isModal) {
				fade.duration = 200;
			} else {
				// we do this, because I think that the 2 effects are running together
				// i.e. modal duration and our fade, and the user doesn't see our fade
				var modalTransparencyDuration:Number = getStyle("modalTransparencyDuration");
				fade.duration = modalTransparencyDuration + 300;
			}
			fade.play([this]);
			this.closeOnEscape = closeOnEscape;
			this.setFocus(); // without it pressing escape key wouldn't work.
		}
		
		public function bringToFront():void {
			PopUpManager.bringToFront(this);
		}

		/**
		 * Adds handlers for MouseDown and MouseMove events.
		 * Need to add them here (on CreationCompleteHandler), because
		 * systemManager is null in constructor.
		 * 
		 * <p>
		 * At the end, resizes the popUp if necessary.
		 * 
		 * @author mircea
		 */
		private function creationCompleteHandler(event:FlexEvent):void {
			addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			systemManager.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler, true);
			// we need to know when this panel is closed so we can remove the added listeners
			// (more exactley to remove the mouseMoveHandler which is put in the systemManager)
			addEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
			
			addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler1);
			
			// the window will acquire some kind of focus, and the ESC
			// handler is enabled immediately. Otherwise, the user has to click
			// on a focus-enabled control (e.g. a text-box, button) in order for the whole form to get focus
			// (=> ESC key handler functional). However, if the user clicks on a textbox outside the form,
			// and then clicks back on the form, but not on a textbox (e.g. title zone, empty form area),
			// the form doens't behave like in Windows, i.e. get focus. A click on a text-box (or button, etc.) is needed.
			setFocus();
			
			resizeIfNecessary();			
		}
			
		/**
		 * Draw resize graphics in the lower right corner.
		 * 		 
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);

			// Draw resize graphics if not minimzed.				
			graphics.clear()
			graphics.lineStyle(2);
			graphics.moveTo(unscaledWidth - 6, unscaledHeight - 1)
			graphics.curveTo(unscaledWidth - 3, unscaledHeight - 3, unscaledWidth - 1, unscaledHeight - 6);						
			graphics.moveTo(unscaledWidth - 6, unscaledHeight - 4)
			graphics.curveTo(unscaledWidth - 5, unscaledHeight - 5, unscaledWidth - 4, unscaledHeight - 6);						

		}
		
		/**
		 * Analyze the cursor position and returns the following values:
		 * <ul>
		 * 	<li> 0 - No interesting position </li>
		 *	<li> LOCATION_MOUSE_RESIZE_CORNER </li>
		 *  <li> LOCATION_MOUSE_LEFT_EDGE </li>
		 *  <li> LOCATION_MOUSE_RIGHT_EDGE </li>
		 * 	<li> LOCATION_MOUSE_BOTOOM_EDGE </li>
		 * </ul>
		 * 
		 * @author mircea
		 */
		protected function getCursorLocation(mousePositionX:Number, mousePositionY:Number):Number {
			
			//lower right corner of panel
			var lowerRightX:Number = x + width; 
			var lowerRightY:Number = y + height;
				
			// Upper left corner of 7x7 hit area
			var upperLeftX:Number = lowerRightX - 7;
			var upperLeftY:Number = lowerRightY - 7;
			
			// distance to the edge for the cursor to be changed
			var edgeDistance:Number = 3;
			
			// first check if the mouse is inside the popup
			if (mousePositionX < x || mousePositionX > lowerRightX 
				|| mousePositionY < y || mousePositionY > lowerRightY) {
					return 0;
			}
			
			// check is mouse inside resize pannel
			if (mousePositionX <= lowerRightX && mousePositionX >= upperLeftX 
				&& mousePositionY <= lowerRightY && mousePositionY >= upperLeftY) {
					return LOCATION_MOUSE_RESIZE_CORNER;
			}
			
			// check if mouse on the left edge
			if (mousePositionX >= x - edgeDistance && mousePositionX <=  x + edgeDistance)  {
				return LOCATION_MOUSE_LEFT_EDGE;
			}

			// check if mouse on the right edge
			if (mousePositionX >= lowerRightX - edgeDistance && mousePositionX <= lowerRightX + edgeDistance) {
				return LOCATION_MOUSE_RIGHT_EDGE;
			}
			
			// check if mouse on the upper edge
			if (mousePositionY >= y - edgeDistance && mousePositionY <= y + edgeDistance) {
				return LOCATION_MOUSE_UPPER_EDGE;
			}
			
			// check if mouse on the bottom edge
			if (mousePositionY >= lowerRightY - edgeDistance && mousePositionY <= lowerRightY + edgeDistance) {
				return LOCATION_MOUSE_BOTOOM_EDGE;
			}
			
			return 0;			
		}
		
		/**
		 * Handler for MOUSE_MOVE event.
		 * 		
		 */
		protected function mouseMoveHandler(event:MouseEvent):void {			
			if (!event.buttonDown) {
				var location:Number = getCursorLocation(event.stageX, event.stageY);
				if (location == LOCATION_MOUSE_RESIZE_CORNER) {
					CursorManager.setCursor(resizeCursor, 2, -16, -16); // center the icon's image (32x32) over the resize corner
					// announce that we have changed the cursor
					cursorChanged = true;
				} else if (location == LOCATION_MOUSE_LEFT_EDGE 
					|| location == LOCATION_MOUSE_RIGHT_EDGE) {
					CursorManager.setCursor(horizCursor, 2, -16, -16); 
					// announce that we have changed the cursor
					cursorChanged = true;
				} else if (location == LOCATION_MOUSE_BOTOOM_EDGE
					|| location == LOCATION_MOUSE_UPPER_EDGE) {
					CursorManager.setCursor(vertCursor, 2, -16, -16); 
					// announce that we have changed the cursor
					cursorChanged = true;
				} else {
					if (cursorChanged) {
						CursorManager.removeAllCursors();
						cursorChanged = false;
					}
				}
			}
			
			// if the mouse was down on the lower right corner
			// resize the window on the fly
			if (mouseIsDown) {
				
				var newWidth:Number = event.stageX - x;
				var newHeight:Number = event.stageY - y;

				// if the the corner or the right edge has been pulled
				// then grow/shrink on horizontal				
				if (locationOnMouseDown == LOCATION_MOUSE_RESIZE_CORNER 
					|| locationOnMouseDown == LOCATION_MOUSE_RIGHT_EDGE) {
					if (newWidth < minWidth) {
						width = minWidth; 
					} else if (newWidth + x < systemManager.screen.width) {
						width = newWidth;
					}
				}
				
				// if the corner or the bottom edge has been pulled
				// the grow/shrink on the veritcal
				if (locationOnMouseDown == LOCATION_MOUSE_RESIZE_CORNER 
					|| locationOnMouseDown == LOCATION_MOUSE_BOTOOM_EDGE) {
					if (newHeight < minHeight) {
						height = minHeight; 
					} else if (newHeight + y < systemManager.screen.height) {
						height = newHeight;
					}
				}
				
				// if the pulled edge is the left one
				// we need to modify the position of the 
				// popup as well as its dimensions
				if (locationOnMouseDown == LOCATION_MOUSE_LEFT_EDGE && event.stageX > 0) {
					newWidth = x - event.stageX + width;
					if (newWidth < minWidth) {
						// stop moving the right edge when the mouse is very close to the
						// minimum size
						x = x + width - minWidth;
						width = minWidth; 
					} else if (newWidth + x < systemManager.screen.width) {
						width = newWidth;
						x = event.stageX;
					}
				}
				
				// if the pulled edge is on the upper edge
				// we need to modify the position of the 
				// popup as well as its dimensions
				if (locationOnMouseDown == LOCATION_MOUSE_UPPER_EDGE && event.stageY > 0) {
					newHeight = y - event.stageY + height;
					if (newHeight < minHeight) {
						// stop moving the right edge when the mouse is very close to the
						// minimum size
						y = y + height - minHeight;
						height = minHeight; 
					} else if (newHeight + y < systemManager.screen.height) {
						height = newHeight;
						y = event.stageY;
					}
				}
				
				// stop the systemManager from moving the popup when we want to resize it
				event.stopImmediatePropagation();
				event.stopPropagation();
				
				// notify flex that it needs to recompute size
				invalidateSize();			
			}
		}
		
		/**
		 * Handler for MOUSE_DOWN event.
		 * 		 
		 */
		private function mouseDownHandler(event:MouseEvent):void {
			locationOnMouseDown = getCursorLocation(event.stageX, event.stageY); 		
			if (locationOnMouseDown > 0) {
				event.stopPropagation();	
				systemManager.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler, true);
				mouseIsDown = true;				
			}				
		}
		
		/**
		 * Handler for MOUSE_UP event.
		 * 		
		 */
		protected function mouseUpHandler(event:MouseEvent):void {
			event.stopImmediatePropagation();		

			invalidateSize();
			systemManager.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler, true	);

			CursorManager.removeAllCursors();
			mouseIsDown = false;							
		}
		
		/**
		 * When this window is closed (removeFromStage), we need to clear the
		 * handler (especially the one on systemManager).
		 * 
		 * @author mircea
		 */
		private function removedFromStageHandler(event:Event):void {			
			systemManager.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler, true);
			removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			removeEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
			
			removeEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler1);
		}
		
		/**
		 * Handler for <code>KeyboardEvent.KEY_DOWN</code> event.
		 * 
		 * <p>
		 * When ESC key is pressed calls <code>handleEscPressed()</code>.
		 * 
		 * <p>
		 * Has the trailing 1 because a method named like this already exists in
		 * one of the parent clases, and has a slightly different usage (i.e. not
		 * listening always).
		 */
		protected function keyDownHandler1(event:KeyboardEvent):void {			
			if (event.keyCode == Keyboard.ESCAPE) {
				handleEscPressed();
			}
		}
		
		protected function handleEscPressed():void {
			if (closeOnEscape)
				formCancelHandler();
			else
				Alert.show(getEscMessage(), "Confirmation", Alert.OK | Alert.CANCEL, this, alertCloseHandler, null, Alert.OK);
		}
		
		protected function getEscMessage():String {
			return "Close this window?";	
		}
		
		protected function alertCloseHandler(event:CloseEvent):void {
			if (event.detail == Alert.OK) {
				formCancelHandler();
			}
		}
				
		protected function formCancelHandler(event:Event = null):void {
			closeForm();
		}
		
		/**
		 * Closes the current popup.
		 */ 
		public function closeForm():void {			
			var fade:Fade = new Fade();
			fade.alphaFrom = 1;
			fade.alphaTo = 0;
			fade.duration = 200;
			fade.play([this]);
			fade.addEventListener(EffectEvent.EFFECT_END, fadeOutEffectEndHandler);
		}
		
		protected function fadeOutEffectEndHandler(event:EffectEvent):void {
			PopUpManager.removePopUp(this as IFlexDisplayObject);			
		}
		
		/**
		 * If pop-up dimensions are smaller than the screen, they will be forced to screen sizes,
		 * making scrollbars appear.
		 * 
		 * <p>
		 * This way, we make sure that the window is usable on small screens as well.
		 * 
		 * @see creationCompleteHandler()
		 */ 
		private function resizeIfNecessary():void {
			if (y + this.height > systemManager.screen.height) {
				this.height = systemManager.screen.height - y;
			}
			if (x + this.width > systemManager.screen.width) {
				this.width = systemManager.screen.width - x;
			}
		}		
	}	
	
}