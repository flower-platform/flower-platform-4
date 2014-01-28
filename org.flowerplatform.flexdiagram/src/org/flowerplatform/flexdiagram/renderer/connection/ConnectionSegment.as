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
	import flash.display.DisplayObjectContainer;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	
	/**
	 * The <code>ConnectionSegment</code> class is the graphic figure
	 * used to build a <code>ConnectionFigure</code>. The class draws a
	 * line between two given points: sourcePoint and targetPoint. 
	 * 
	 * @author Georgi
	 * 
	 */
	public class ConnectionSegment extends UIComponent {
			
		private var _sourcePoint:BindablePoint;
			
		private var _targetPoint:BindablePoint;
		
		private var _dashSegmentSize:int;
		
		private var _thickness:Number;
		
		private var _color:uint;  
		
		/**
		 * Creates a new ConnectionSegment. As this is always part of a figure, it receives the parent's EditPart,
		 * the source and target points inside the figure and optional the desired line style:
		 * <li>dashedSegmentSize when dashed line style is desired
		 * <li>lineThickness to determine the thickness of the line
		 * <li>lineColor to establish other color than black
		 * 
		 * 
		 */
		public function ConnectionSegment(sp:BindablePoint, tp:BindablePoint,
			dashSegementSize:int = 0, lineThickness:Number = 1, lineColor:uint = 0x000000) {
			super();
			_sourcePoint = sp;
			_targetPoint = tp;
			_dashSegmentSize = dashSegementSize;
			_thickness = lineThickness;
			_color = lineColor;
			_sourcePoint.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateDisplay);
			_targetPoint.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateDisplay);
		}
		
		/**
		 * 
		 */
		private function updateDisplay(event:PropertyChangeEvent):void {
			invalidateDisplayList();
		}
	
		public function get sourcePoint():BindablePoint {
			return _sourcePoint;
		}
		
		public function get targetPoint():BindablePoint {
			return _targetPoint;
		}
		
		public function get dashSegmentSize():int {
			return _dashSegmentSize;
		}
		
		public function get thickness():Number {
			return _thickness;
		}
		
		public function get color():uint {
			return _color;
		}
		
		public function set sourcePoint(sp:BindablePoint):void {
			_sourcePoint.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateDisplay);
			_sourcePoint = sp;
			_sourcePoint.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateDisplay);
		}
		
		public function set targetPoint(tp:BindablePoint):void {
			_targetPoint.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateDisplay);
			_targetPoint = tp;
			_targetPoint.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateDisplay);
		}
	
		public function set dashSegmentSize(segmentSize:int):void {
			_dashSegmentSize = segmentSize;
			invalidateDisplayList();
		}
		
		public function set thickness(lineThickness:Number):void {
			_thickness = lineThickness;
			invalidateDisplayList();
		}
		
		public function set color(lineColor:uint):void {
			_color = lineColor;
			invalidateDisplayList();
		}
		
		/**
		 * The function draws a dashed line between the two points
		 * received as arguments.
		 */
		private function drawDash(s:BindablePoint, t:BindablePoint):void {
			var lineLength:Number = Math.sqrt(Math.pow(s.x - t.x, 2) + Math.pow(s.y - t.y, 2));
			var angle:Number = Math.atan((t.y - s.y)/(t.x - s.x));
			var degrees:Number = angle*(180/Math.PI);
			var gap:int = 5;
			var steps:int = lineLength / (dashSegmentSize + gap);
			
			var dashstepX:Number = dashSegmentSize * Math.cos(angle);
			var dashstepY:Number = dashSegmentSize * Math.sin(angle);
			var gapstepX:Number = gap * Math.cos(angle);
			var gapstepY:Number = gap * Math.sin(angle);
			
			var stepcount:Number = 0;
		
			var dashendX:Number;
			var dashendY:Number;
			
			var dashstartX:Number;
			var dashstartY:Number;
			
			while ((stepcount ++) < steps) {
				dashstartX = s.x + (stepcount - 1)*(dashstepX + gapstepX);
				dashstartY = s.y + (stepcount - 1)*(dashstepY + gapstepY);
				dashendX = dashstartX + dashstepX;
				dashendY = dashstartY + dashstepY;
				
				graphics.moveTo(dashstartX, dashstartY);
				graphics.lineTo(dashendX, dashendY);
			}
			
			//draw a part of a dashSegment if we still have blank space to be filled
			if (steps == 0) {
				graphics.moveTo(s.x, s.y);
				graphics.lineTo(t.x, t.y);
			} else if (steps * (dashSegmentSize + gap) < lineLength) {
				dashstartX = dashendX + gapstepX;
				dashstartY = dashendY + gapstepY;
				
				graphics.moveTo(dashstartX, dashstartY);
				graphics.lineTo(t.x, t.y);
			} 
		}
		/**
		 * The transparent line is thicker than the current line. It is used
		 * to 'grab' more easily the line.
		 * 
		 */
		private function drawTransparentLine():void {
			graphics.lineStyle(5, 0xffffff, 0);
			graphics.moveTo(sourcePoint.x, sourcePoint.y);
			graphics.lineTo(targetPoint.x, targetPoint.y);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			drawTransparentLine();
			graphics.lineStyle(thickness, color);
			if (dashSegmentSize == 0) { //continuous line
				graphics.moveTo(sourcePoint.x, sourcePoint.y);
				graphics.lineTo(targetPoint.x, targetPoint.y);
			} else { // dashed line
				if (sourcePoint.x > targetPoint.x)
					drawDash(targetPoint, sourcePoint);
				else 
					drawDash(sourcePoint, targetPoint);
			}
		}
		
		/**
		 * The method computes the slope of the segment.
		 * 
		 */
		public function getSegmentSlope():Number {
			return (sourcePoint.y - targetPoint.y) / (sourcePoint.x - targetPoint.x);
		}
		
		/**
		 * The method computes the length of the segment.
		 * 
		 */
		public function getSegmentLength():Number {
			return Math.sqrt(Math.pow(sourcePoint.x - targetPoint.x, 2) + Math.pow(sourcePoint.y - targetPoint.y, 2));
		}
		
		/**
		 * The method computes the (x, y) coordinates of the point of the
		 * segment which is at "distance" distance of the left point of the
		 * segment.
		 * 
		 */
		public function getSegmentPoint(distance:int):BindablePoint {
			var referencePoint:BindablePoint = sourcePoint;
			var dx:int;
			var dy:int;
			// if the segment is almost vertical => the slope is infinite =>
			// compute dx and dy without taking into consideration the slope
			if (sourcePoint.x >= targetPoint.x - 5 && sourcePoint.x <= targetPoint.x + 5) {
				dx = 0;
				if (sourcePoint.y < targetPoint.y) {
					dy = distance
				} else {
					dy = -distance;
				}
			} else {
				var alfa:Number = Math.atan(getSegmentSlope());
				dx = int(distance * Math.cos(alfa));
				dy = int(distance * Math.sin(alfa));
				if (sourcePoint.x > targetPoint.x) {
					dx = -dx;
					dy = -dy;
				}
			}
			return new BindablePoint(referencePoint.x + dx, referencePoint.y + dy);
		}
		
		// SMR-HACK
		/**
		 * Returns the intersection point with an rectangle, returns null
		 * if the segments don't intersect.
		 */
		public function intersectsSegment(p1:Point, p2:Point):Point {
			var xa:Number = targetPoint.x - sourcePoint.x;
			var ya:Number = targetPoint.y - sourcePoint.y;
			var xb:Number = p2.x - p1.x;
			var yb:Number = p2.y - p1.y;
			
			var d:Number = yb*xa - xb*ya;
			var ua:Number = (xb * (sourcePoint.y - p1.y) - yb * (sourcePoint.x - p1.x))/d;
			
			if (ua >= 0 && ua <=1) {
				var ub:Number = (xa * (sourcePoint.y - p1.y) - ya*(sourcePoint.x - p1.x))/d;
				if (ub >= 0 && ub <= 1) {
					return new Point(p1.x + xb * ub, p1.y + yb * ub);
				} else {
					return null;
				}
			}
			return null;
		}
		
		// SMR-HACK
		/**
		 * Returns true if segment intersects or is inside a given rectangle, returns
		 * false otherwise.
		 */
		public function intersectsRect(rect:Rectangle):Boolean {
			if (rect.contains(sourcePoint.x, sourcePoint.y)) return true;
			if (rect.contains(targetPoint.x, targetPoint.y)) return true;
			// get rect bounds 
			var p1:Point = new Point(rect.x, rect.y);
			var p2:Point = new Point(rect.x + rect.width, rect.y);
			var p3:Point = new Point(rect.x, rect.y + rect.height);
			var p4:Point = new Point(rect.x + rect.width, rect.y + rect.height);
			// check if the segment intersects any of the rect edges
			if (intersectsSegment(p1, p2) != null) return true;
			if (intersectsSegment(p1, p3) != null) return true;
			if (intersectsSegment(p2, p4) != null) return true;
			if (intersectsSegment(p3, p4) != null) return true;
			return false;
		}
	}
}