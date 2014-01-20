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
package  com.crispico.flower.flexdiagram.ui {
	import flash.events.Event;
	
	import mx.containers.VBox;
	import mx.controls.Label;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	/**
	 * 
	 * A component with border and semi-transparent background that can be used
	 * during move (and/or resize) operations for rectangular shaped EditParts.
	 *
	 * @author cristi
	 * @author Georg
	 * 
	 */
	public class MoveResizePlaceHolder extends UIComponent {
		
		private var color:uint = 0xCCCCCC;
		
		public function MoveResizePlaceHolder() {
			super();
			// default alpha
			alpha = 0.4;
		}
		
		// SMR-HACK
		public function setColorAndAlpha(newColor:uint, newAlpha:Number):void {
			if (newColor != color || newAlpha != alpha) {
				color = newColor;
				alpha = newAlpha;				
				invalidateDisplayList();
			}
		}
		
		// SMR-HACK
		public function resetPositionAndDimensions():void {
			x = y = width = height = 0;
		}
		
		/**
		 * 
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			graphics.lineStyle(1);
			graphics.beginFill(color, alpha);
			drawPlaceHolder(unscaledWidth, unscaledHeight);
		}
		
		protected function drawPlaceHolder(width:Number, height:Number):void {
			graphics.drawRect(0, 0, width, height);
		}
	}
}