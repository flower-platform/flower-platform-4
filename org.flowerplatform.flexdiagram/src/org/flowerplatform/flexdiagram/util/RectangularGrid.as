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
package org.flowerplatform.flexdiagram.util {
	
	import flash.display.CapsStyle;
	import flash.display.GradientType;
	import flash.display.JointStyle;
	import flash.display.SpreadMethod;
	import flash.geom.Matrix;
	
	import mx.core.UIComponent;
	import mx.managers.IFocusManagerComponent;
		
	/**
	 * Graphical component that fills the surface with horizontal and/or vertical lines or bars.
	 * 
	 * <code>IFocusManagerComponent</code> needed to set focus on diagram
	 * when clicking on the grid.
	 * 
	 * @author Luiza
	 * @author Cristina Constantinescu
	 */ 
	[SecureSWF(rename="off")]
	public class RectangularGrid extends UIComponent implements IFocusManagerComponent {
		
		protected var _drawHorizontalLines:Boolean = true;
		
		protected var _drawVerticalLines:Boolean = true;
		
		protected var _size:uint = 20;
		 
		protected var _dashSize:uint = 0;
		
		protected var _color:uint = 0xCCCCCC;
		
		protected var _alternatingColors:Array;
		
		protected var _colorAlpha:Number = 1;
		
		/**
		 * When <code>true</code> the grid draws horizontal lines spaced by <code>size</code>.
		 * 
		 * @default true
		 */ 
		public function get drawHorizontalLines():Boolean {
			return _drawHorizontalLines;
		}
		
		public function set drawHorizontalLines(value:Boolean):void {
			if (_drawHorizontalLines != value) {
				_drawHorizontalLines = value;
				invalidateDisplayList();
			}
		}
		
		/**
		 * When <code>true</code> the grid draws vertical lines spaced by <code>size</code>.
		 * 
		 * @default true
		 */ 
		public function get drawVerticalLines():Boolean {
			return _drawVerticalLines;
		}
		
		public function set drawVerticalLines(value:Boolean):void {
			if (_drawHorizontalLines != value) {
				_drawHorizontalLines = value;
				invalidateDisplayList();
			}
		}
		
		/**
		 * Defines the distance between two horizontal/vertical lines.
		 * 
		 * <p>Note that if #alternatingColors are provided, the rows are no longer marked with horizontal lines
		 * but filled with color and this is the height of a row.
		 * 
		 * @default 20
		 */
		public function get size():uint {
			return _size;
		}
		
		public function set size(value:uint):void {
			if (value != _size) {
				_size = value;
				invalidateDisplayList();
			}
		}
		
		/**
		 * Defines the style of the lines making up the grid.
		 * 
		 * <p>Use <code>0</code> for continuous lines and bigger values for dashed lines.
		 * 
		 * @default 0
		 */ 
		public function get dashSize():uint {
			return _dashSize;
		}
		
		public function set dashSize(value:uint):void {
			if (value != _dashSize) {
				_dashSize = value;
				invalidateDisplayList();
			}
		}
		
		/**
		 * Color used to paint the lines of the grid.
		 * 
		 * @default 0xCCCCCC
		 */ 
		public function get color():uint {
			return _color;
		}
		
		public function set color(value:uint):void {
			if (value != _color) {
				_color = value;
				invalidateDisplayList();
			}
		}	
		
		/**
		 * Colors for the rows. Optional.
		 * If provided then draws colored rows, otherwise just lines.
		 */
		public function get alternatingColors():Array {
			return _alternatingColors;
		}
		
		/**
		 * @flowerModelElementId _DiJL4MZWEd-Fc6yTEUbBkQ
		 */
		public function set alternatingColors(colors:Array):void {
			var triggerPaint:Boolean = false;
			
			if (_alternatingColors == null) {
				_alternatingColors = colors;
				triggerPaint = true;
			
			} else if (colors != null) {
				if (colors.length != _alternatingColors.length) {
					// changing the number of colors used				
					triggerPaint = true;
					
				} else {
					for (var i:int = 0; i < colors.length; i++) {
						// detect a change in colors
						if (colors[i] != _alternatingColors[i]) {
							_alternatingColors = colors;
							triggerPaint = true;
							break;			
						}
					}
				}
				
			} else {// new colors are null
				_alternatingColors = colors;
				triggerPaint = true;
			}
			
			if (triggerPaint) {
				invalidateDisplayList()
			}
		}
			
		/**
		 * Controls the opacity of the color used to paint the component. This affects both lines and filled rows.
		 * 
		 * <p>Notice that when adding multiple grids on a surface (one on top of the other) it is imperative to set 
		 * different oppacity to make sure components will be visible.
		 * 
		 * @default 1
		 */ 
		public function get colorAlpha():Number {
			return _colorAlpha;
		}
		
		public function set colorAlpha(value:Number):void {
			if (value != _colorAlpha) {
				_colorAlpha = value;
				invalidateDisplayList();	
			}
		}
		
		/**
		 * Draws a line (vertical or horizontal). Acording to #dashSize value, the line style can be 
		 * continous or dashed.
		 * 
		 * @see dashSize.
		 */ 
		public function drawLine(fromX:int, fromY:int, toX:int, toY:int):void {
			graphics.lineStyle(1, color, colorAlpha);
			
			// draw simple line
			if (dashSize == 0) { 
				graphics.moveTo(fromX, fromY);
				graphics.lineTo(toX, toY);
			
			// draw dashed/dotted line
			} else {
				// the old algorithm of drawing dashed lines was replaced because it was too expensive (it was growing in time as the grid became bigger)
				// slowing down the zoom on the diagram
				// this improves very much the performance of drawing (from 45 ms and more to 1 ms)
				var gradientBoxMatrix:Matrix = new Matrix();
				// for generality this should be the line's slope
				// but for this particular case (we draw only vertical an horizontal lines) will be ok
				var angle:Number = Math.PI/4;
				if (fromX == toX ) { 
					// draw vertical line
					angle = Math.PI / 2;
				} else if (fromY == toY) {
					// draw horizontal line
					angle = Math.PI * 2;
				} 
   				gradientBoxMatrix.createGradientBox(2 * dashSize, 2 * dashSize, angle); 
				graphics.lineStyle(1, color, colorAlpha, false, "normal", CapsStyle.NONE, JointStyle.ROUND, dashSize);
    			graphics.lineGradientStyle(GradientType.LINEAR, [color, color], [1, 0], [100, 100], gradientBoxMatrix, SpreadMethod.REPEAT);
  				graphics.moveTo(fromX, fromY);
				graphics.lineTo(toX, toY);	
			}	
		}
		
		/**
		 * Called from #updateDisplayList to render the grid. Draws vertical and horizontal lines spaced by #size.
		 * If #alternatingColors are provided then horizontal lines are replaced with rows filled with the given colors.
		 * 
		 * Subclasses may call this function instead of calling <code>invalidateDisplayList()</code>
		 * whenever repainting is necessary.
		 * 
		 * @author Luiza
		 * @author Mircea Negreanu
		 */ 
		protected function paintGrid():void {
			graphics.clear();
			
			// fills the background so that the events are dispatched correctly
			graphics.lineStyle(0, 0xFFFFFF, 0);
			graphics.beginFill(0xFFFFFF, 0);
			graphics.drawRect(0, 0, width, height);
			graphics.endFill();
			 
			// now draw the lines
			if (drawHorizontalLines) {
				var rows:int = height / size;
				var colorIndex:int = 0;
				for (var y:int = 0; y < rows + 1; y++) {
					if (alternatingColors) {
						colorIndex = colorIndex % alternatingColors.length;
						
						// correct (shrink) the last row if needed
						var h:int = size;
						if (y * size + size > height) {
							h = height - y * size;
						}

						drawHorizontalRectangle(y * size, h, colorIndex);
						colorIndex++;
					} else {
						drawLine(0, y * size, width, y * size);
					}
				}
			 }
			 
			 if (drawVerticalLines) {
			 	var columns:int = width / size;
			 	for (var x:int = 0; x < columns + 1; x++) {
			 		drawLine(x * size, 0, x * size, height);
			 	}
			 }
		}
		
		protected function drawHorizontalRectangle(y:int, h:int, colorIndex:int):void {
			graphics.beginFill(alternatingColors[colorIndex], _colorAlpha);
			graphics.drawRect(0, y, width, h);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			paintGrid();
		}
		
		/**
		 * Apply flower styles on this grid. 
		 */ 
		override public function setStyle(styleProp:String, newValue:*):void {
			var isFlowerStyle:Boolean = false;
			
			if (styleProp == "gridLineColor") {	
				isFlowerStyle = true;
				color = newValue;
			}
			
			if (styleProp == "gridColorAlpha") {
				isFlowerStyle = true;
				colorAlpha = newValue; 	
			}
			
			if (styleProp == "gridLineDashSize") {
				isFlowerStyle = true;
				dashSize = newValue;
			}
			
			if (!isFlowerStyle) {
				super.setStyle(styleProp, newValue);	
			}
		}		
	}
}