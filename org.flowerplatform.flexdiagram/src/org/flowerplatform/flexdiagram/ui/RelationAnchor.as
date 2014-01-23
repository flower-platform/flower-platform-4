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
package org.flowerplatform.flexdiagram.ui {
	
	import flash.display.Bitmap;
	import flash.display.DisplayObject;
	import flash.display.GradientType;
	import flash.display.Stage;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.geom.Point;
	
	import mx.core.UIComponent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RelationAnchor extends UIComponent {
		
		// this component changes visual apparence depending on its state
		// it can be in one of the following states
		
		public static const NORMAL:int = 0;
		
		public static const HIGHLIGHT:int = 1;
		
		public static const PRESSED:int = 2;
		
		// current state
		private var _state:int = NORMAL;
		
		// the iocon displayed can be either anchor_big or anchor_small
		private var icon:Bitmap;
		
		// keep this reference to add mouse listener on Stage and to be able to remove it
		private var stageBackup:Stage = null;
		
		[Embed(source="../icons/anchor_big.gif")]
		private var anchor_big:Class;
		
		[Embed(source="../icons/anchor_small.gif")]
		private var anchor_small:Class;
		
		/**
		 * @parameter useSmallAnchor if <code>true</code> this component will display a small arrow: w/h = 16/12
		 * otherwise will display a bigger arrow : w/h = 20/15
		 */ 				
		public function RelationAnchor(useSmallAnchor:Boolean = false) {
			super();
			var anchor:Class = null;
			
			if (useSmallAnchor) {
				anchor = anchor_small;
				width = 16;
				height = 12;
			} else {
				anchor = anchor_big;
				width = 20;
				height = 14;
			}
			
			icon = new anchor();
			addChild(icon);
			icon.x = 2;
			icon.y = 2;
			
			addEventListener(Event.ADDED_TO_STAGE, addedToStageHandler);
		}
		
		protected function get state():int {
			return _state;
		}
		
		protected function set state(value:int):void {
			if (value != state) {
				_state = value;
				invalidateDisplayList();
			}
		}
		
		// change state depending on the mouse position
		
		protected function mouseOverHandler(event:MouseEvent):void {
			if (state == NORMAL) {
				state = HIGHLIGHT;
			}
		}	
		
		protected function mouseOutHandler(event:MouseEvent):void {
			if (!event.buttonDown) {
				state = NORMAL;
			}
		}
		
		protected function mouseDownHandler(event:MouseEvent):void {
			state = PRESSED;
		}
		
		protected function mouseUpHandler(event:MouseEvent):void {
			if (stage == null) {
				// this can happen in mouseUp is cached after object is removed from stage and
				// removedFromStageHandler did not execute yet
				return;
			}
			var arr:Array = stage.getObjectsUnderPoint(new Point(event.stageX, event.stageY));
			
			// if this is still under nouse cursor then go into HIGHLIGHT state
			if (arr[0] == this) { 
				state = HIGHLIGHT;
			} else {
				// go back to normal state
				state = NORMAL;
			}
		}
		
		/**
		 * Adds Mouse ROLL_OVER, ROLL_OUT, MOUSE_DOWN. 
		 * Adds listener on stage MOUSE_UP event to go back to NORMAL state when the user finished interaction
		 * with the component.
		 */ 
		protected function addedToStageHandler(event:Event):void {
			stage.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			stageBackup = stage;
			
			addEventListener(MouseEvent.ROLL_OVER, mouseOverHandler);
			addEventListener(MouseEvent.ROLL_OUT, mouseOutHandler);
			addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			
			addEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
		}
		
		/**
		 * Remove all listeners.
		 */ 
		protected function removedFromStageHandler(event:Event):void {
			stageBackup.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			stageBackup = null;
			removeEventListener(MouseEvent.ROLL_OVER, mouseOverHandler);
			removeEventListener(MouseEvent.ROLL_OUT, mouseOutHandler);
			removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			
			removeEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
		}
		
		/**
		 * Draw the features of this RelationAnchor depending on its state.
		 */ 
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			// clear anyway to go back to the normal state
			graphics.clear();
			
			if (state != NORMAL) {
				graphics.beginFill(0xFFFFFF, 1);
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
				graphics.endFill();
				var matrix:Matrix = new Matrix();
				matrix.createGradientBox(unscaledWidth, unscaledHeight, 3 * Math.PI / 2, 0, 0);
				
				if (state == HIGHLIGHT) {
					graphics.beginGradientFill(GradientType.LINEAR, [0xB0C4DE, 0xFFFFFF], [.5, .3], [0, 178], matrix);
					
				} else if (state == PRESSED) {
					graphics.beginGradientFill(GradientType.LINEAR, [0x87CEEB, 0xFFFFFF], [.8, .5], [0, 178], matrix);
				}
				
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
				graphics.endFill();
				graphics.lineStyle(1);
				graphics.lineGradientStyle(GradientType.LINEAR, [0x0000FF, 0x87CEEB], [1, .9], [0, 255], matrix);
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);	
			}
		}	
	}
}