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
package org.flowerplatform.flexutil.gradient_list {
	import flash.display.GradientType;
	import flash.display.Graphics;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.graphics.BitmapFill;
	import mx.graphics.GradientEntry;
	import mx.graphics.LinearGradient;
	import mx.rpc.events.HeaderEvent;
	
	import spark.components.List;
	import spark.primitives.Rect;
	
	/**
	 * Logic for gradient background lists. It is intended to be used
	 * by both the desktop skin, as well as the mobile skin. 
	 * 
	 * <p>
	 * This skin uses the following styles set on the host component:
	 * <ul>
	 * 		<li> contentBackgroundColor - the color for the background
	 * 		<li> gradientColor - gradient color
	 * 		<li> borderColor - border color
	 * 		<li> gradientStyle (optional, default value is RIGHT_GRADIENT) - the position for the gradient
	 * 		<li> sidesBorderStyle (optional, default value is FULL_SIDE_BORDER) - which side borders are visible
	 * </ul>
	 * 
	 * @author Mariana
	 */ 
	public class GradientListSkinLogic {
		
		[SecureSWF(rename="off")]
		public static const NO_GRADIENT:int = 0;
		
		[SecureSWF(rename="off")]
		public static const LEFT_GRADIENT:int = 1;
		
		[SecureSWF(rename="off")]
		public static const RIGHT_GRADIENT:int = 2;
		
		[SecureSWF(rename="off")]
		public static const FULL_GRADIENT:int = 3;
		
		/** 
		 * Left and right borders are not visible. 
		 */ 
		[SecureSWF(rename="off")]
		public static const NO_SIDE_BORDER:int = 0;
		
		/**
		 * Only top, bottom and left border are visible.
		 */ 
		[SecureSWF(rename="off")]
		public static const LEFT_BORDER:int = 1;
		
		/**
		 * Only top, bottom and right border are visible.
		 */ 
		[SecureSWF(rename="off")]
		public static const RIGHT_BORDER:int = 2;
		
		/**
		 * All borders are visible.
		 */ 
		[SecureSWF(rename="off")]
		public static const FULL_SIDE_BORDER:int = 3;
		
		/**
		 * Set default styles on the <code>hostComponent</code>.
		 */ 
		public static function initializationComplete(hostComponent:List):void {
			hostComponent.setStyle("borderVisible", false); // hide the default border
		}
		
		/**
		 * Gradient fill and border.
		 * 
		 * @param background the background Rect, if the skin has one
		 */ 
		public static function updateDisplayList(unscaledWidth:int, unscaledHeight:int, hostComponent:List, background:Rect = null):void {
			if (background) {
				background.fill = null;
			} 
			
			var graphics:Graphics = hostComponent.skin.graphics;
			
			// draw the gradient
			var backgroundColor:int = hostComponent.getStyle("backgroundColor");
			var gradientColor:int = hostComponent.getStyle("gradientColor");
			var gradientWidth:int = hostComponent.getStyle("gradientWidth");
			var gradientStyle:int = GradientListSkinLogic[String(hostComponent.getStyle("gradientStyle")).toUpperCase()];
			
			graphics.beginFill(backgroundColor);
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			graphics.endFill();
			
			if (gradientStyle & LEFT_GRADIENT) {
				drawGradient(graphics, [new GradientEntry(gradientColor), new GradientEntry(backgroundColor)], 0, gradientWidth, unscaledHeight);
			}
			if (gradientStyle & RIGHT_GRADIENT) {
				drawGradient(graphics, [new GradientEntry(backgroundColor), new GradientEntry(gradientColor)], unscaledWidth - gradientWidth, gradientWidth, unscaledHeight);
			}
			
			// draw the border
			var borderStyle:int = GradientListSkinLogic[String(hostComponent.getStyle("sidesBorderStyle")).toUpperCase()];
			graphics.lineStyle(1, hostComponent.getStyle("borderColor"), 1); 
			graphics.moveTo(0, 0);
			graphics.lineTo(unscaledWidth - 1, 0);				// top border
			if (borderStyle & RIGHT_BORDER) {
				graphics.lineTo(unscaledWidth - 1, unscaledHeight);	// right border
			} else {
				graphics.moveTo(unscaledWidth - 1, unscaledHeight);
			}
			graphics.lineTo(0, unscaledHeight);					// bottom border
			if (borderStyle & LEFT_BORDER) {	
				graphics.lineTo(0, 0);							// left border
			}
		}
		
		private static function drawGradient(graphics:Graphics, entries:Array, left:int, width:int, height:int):void {
			var fill:LinearGradient = new LinearGradient();
			fill.entries = entries;
			fill.begin(graphics, new Rectangle(left, 0, width, height), new Point());
			graphics.moveTo(left, 0);
			graphics.lineTo(left + width, 0);
			graphics.lineTo(left + width, height);
			graphics.lineTo(left, height);
			graphics.lineTo(left, 0);		
			fill.end(graphics);
		}
	}
}