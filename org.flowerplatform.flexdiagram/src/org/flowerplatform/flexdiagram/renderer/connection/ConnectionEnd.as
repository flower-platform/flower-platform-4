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
package org.flowerplatform.flexdiagram.renderer.connection {
	
	import mx.core.UIComponent;

	/**
	 *  
	 * UIComponent that represents the arrow/rhomb/triangle for a 
	 * connection figure. The class receives the coordinates for a point,
	 * an angle and the type of figure that needs to be drawn.
	 * 
	 */ 
	public class ConnectionEnd extends UIComponent {
	
		private const ARROW_WIDTH:int = 4;
		private const ARROW_HEIGHT:int = 8;
		
		/**
		 * A connection figure can have a decorator at each side. If 
		 * sourceEndType/targetEndType is NONE => no decorator is 
		 * being used.
		 */
		public static const NONE:String = "none";
		public static const OPEN_ARROW:String = "open_arrow";
		public static const CLOSED_ARROW:String = "closed_arrow";
		public static const FILLED_ARROW:String = "filled_arrow";
		public static const DIAMOND:String = "diamond";
		public static const FILLED_DIAMOND:String = "filled_diamond";
		
		/**
		 * Represents the source/target point for a connection 
		 * (where the figure must be drawn).
		 */
		public var point:BindablePoint; 
		
		/**
		 * The required angle.
		 */
		public var angle:Number; 
		
		/**
		 * The type of figure. Can be one of the constants
		 * defined above.
		 */
		public var type:String;
		
		public function ConnectionEnd() {
			super();
		}
		
		private function getParent():ConnectionRenderer {
			return ConnectionRenderer(parent);
		}
		
		/**
		 * The simple arrow marks the navigability property of an 
		 * attribute by drawing an arrow on the association line 
		 * that connects two classes.
		 */
		private function drawOpenArrow():void {
			graphics.moveTo(point.x, point.y);
			graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) - ARROW_WIDTH*Math.sin(angle),
  									point.y + ARROW_HEIGHT*Math.sin(angle) + ARROW_WIDTH*Math.cos(angle));
  			graphics.lineTo(point.x, point.y);
  	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) + ARROW_WIDTH*Math.sin(angle),
  	  								point.y + ARROW_HEIGHT*Math.sin(angle) - ARROW_WIDTH*Math.cos(angle));
		}
		
		/**
		 * Similar to simpleArrow function but it fills the
		 * interior of the arrow with a given color. If no color
		 * is sent, the used color is white.
		 * 
		 * used by generalizations, realizations and interface implementations.
		 */
		private function drawClosedArrow(fillColor:uint = 0xffffff):void {
			graphics.beginFill(fillColor);
  			graphics.moveTo(point.x, point.y);
	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) - ARROW_WIDTH*Math.sin(angle),
  									point.y + ARROW_HEIGHT*Math.sin(angle) + ARROW_WIDTH*Math.cos(angle));
  	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) + ARROW_WIDTH*Math.sin(angle),	
  									point.y + ARROW_HEIGHT*Math.sin(angle) - ARROW_WIDTH*Math.cos(angle));		
  			graphics.lineTo(point.x, point.y);					
			graphics.endFill();
		}
		
		/**
		 * Draws a rhomb - for associations (shared/composite)
		 * Used by associations.
		 */
		private function drawDiamond(fillColor:uint = 0xffffff):void {
			graphics.beginFill(fillColor);
			graphics.moveTo(point.x, point.y);
			graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) - ARROW_WIDTH*Math.sin(angle),
  									point.y + ARROW_HEIGHT*Math.sin(angle) + ARROW_WIDTH*Math.cos(angle));
  			graphics.lineTo(point.x, point.y);
  	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) + ARROW_WIDTH*Math.sin(angle),
  	  								point.y + ARROW_HEIGHT*Math.sin(angle) - ARROW_WIDTH*Math.cos(angle));
  	  		graphics.lineTo(point.x + 2*ARROW_HEIGHT*Math.cos(angle), point.y + 2*ARROW_HEIGHT*Math.sin(angle));
  	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) - ARROW_WIDTH*Math.sin(angle),
  	  								point.y + ARROW_HEIGHT*Math.sin(angle) + ARROW_WIDTH*Math.cos(angle));
  	  		graphics.endFill();
		}
		
		/**
		 * The method calls a method to draw an arrow/triangle/diamond.
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			graphics.lineStyle(getParent().thickness, getParent().color);
			switch (type) {
				case OPEN_ARROW:
					drawOpenArrow();
					break;
				case CLOSED_ARROW:
					drawClosedArrow();
					break;
				case FILLED_ARROW:
					drawClosedArrow(getParent().color);
					break;
				case DIAMOND:
					drawDiamond();
					break;
				case FILLED_DIAMOND:
					drawDiamond(getParent().color);
					break;
				default:
					break;
			}
		}
	}
}