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
	
	/**
	 * <p>
	 * The class is used by <code>ConnectionEditPart</code> to implement
	 * the "clip logic". A separate class was created to implement the 
	 * mathematical algorithms used when connecting a line to the source / 
	 * target figure. 
	 * 
	 * <p> 
	 * The 'clip' algorithm is used when two rectangular figures
	 * are connected using a <code>ConnectionFigure</code>. To compute the 
	 * connection's position (start point / end point) the method 
	 * <code>getCenterBindablePoint()</code> retrieves the center point for
	 * the source / target figure. But the connection uses as start point /
	 * end point the intersection point between the line that connects the
	 * centers of the two figures and their borders. In this way, the 
	 * connection's part which is under/on the source / target figure is
	 * not drawn.
	 * 
	 * <p>
	 * The class is also used by <code>ConnectionLabelEditPart</code>
	 * and its subclasses in order to compute which edge of a rectangle
	 * is intersected by a given connection.
	 * 
	 * @author Georgi
	 * 
	 */
	public class ClipUtils {
		
		/**
		 * The method computes the center of a rectangle given by an 
		 * array of values: x, y, width, height.
		 */ 
		public static function getCenterBindablePoint(rect:Array):BindablePoint {
			var p:BindablePoint = new BindablePoint(0,0);
			p.x = rect[0] + rect[2] / 2;
			p.y = rect[1] + rect[3] / 2;
			return p;
		}
		
		/**
		 * The method computes the intersection between the segments [AB] and [CD].
		 * To compute the equation of a line that passes through two given points
		 * (x1, y1) and (x2, y2):
		 * 		a * x1 + b * y1 + c = 0
		 * 		a * x2 + b * y2 + c = 0
		 * 		=> a = (y1 - y2); b = (x2 - x1); c = (x2 * y1 - x1 * y2)
		 * 		=> line's equation: a * x + b* y + c = 0;
		 * To compute the intersection point for two lines given by theirs equations:
		 * 		a1 * x1 + b1 * y1 + c1 = 0 (the equation of the line that passes through (x1, y1))
		 * 		a2 * x1 + b2 + y1 + c2 = 0 (the equation of the line that passes through (x2, y2))
		 * 		x1 = x2 = x and y1 = y2 = y (for the intersection point)
		 * 		=> x = (b1 * c2 - b2 * c1) / (a1 * b2 - a2 * b1)
		 * 		=> y = (a2 * c1 - a1 * c2) / (a1 * b2 - a2 * b1) 
		 * For validation: (x, y) belongs to the segment that connects (x1, y1) and (x2, y2)
		 * if x1 <= x <= x2 and y1 <= y <= y2.
 	 	 */
		public static function lineIntersection(A:BindablePoint, B:BindablePoint, C:BindablePoint,  D:BindablePoint):BindablePoint{
			var result:BindablePoint = new BindablePoint(int.MIN_VALUE, int.MIN_VALUE);
			var rX:Number, rY:Number;
			var a1:Number, b1:Number, c1:Number, a2:Number, b2:Number, c2:Number;
		
			a1 = A.y - B.y;
			b1 = B.x - A.x;
			c1 = A.y * B.x - B.y * A.x;

			a2 = C.y - D.y;
			b2 = D.x - C.x;
			c2 = C.y * D.x - D.y * C.x;
			
			var delta:int = b1 * a2 - b2 * a1;
			if (delta == 0)
				return result;
			
			rX = (c2 * b1 - c1 * b2) / delta;
			rY = (c1 * a2 - c2 * a1) / delta;

			var eps:Number = 2.0;
			if (rX + eps < Math.min(A.x, B.x) || rX + eps < Math.min(C.x, D.x) ||
			    rY + eps < Math.min(A.y, B.y) || rY + eps < Math.min(C.y, D.y) || 			
			    rX - eps > Math.max(A.x, B.x) || rX - eps > Math.max(C.x, D.x) ||
			    rY - eps > Math.max(A.y, B.y) || rY - eps > Math.max(C.y, D.y)) {
				return result;
			}
			result.x = rX;
			result.y = rY;
			return result;
		}
		
		/**
		 * The method computes the distance between two points in plane.
		 */
		public static function distance(p1:BindablePoint, p2:BindablePoint):Number{
			if (p1.x == int.MIN_VALUE || p2.x == int.MIN_VALUE)
				return 10000*10000*5;
			else
				return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
		}
		
		/**
		 * From the generalization of Pitagora theorem (a^2 + b^2 - 2ab*cos(teta) = c^2),
		 * where a, b, c are the sides of a triangle (computed from the coordinates of
		 * the tree given points) => teta = arccos((a^2 + b^2 - c^2)/(2*a*b)). 
		 * 
		 * The method is used by <code>ConnectionEditPart</code> in order to
		 * decide between 'remove' and 'modify' a bendpoint using the angle it creates
		 * the two segments connected by the given point.
		 */
		public static function computeAngle(leftPoint:BindablePoint, middlePoint:BindablePoint, rightPoint:BindablePoint):Number {
			var a:Number = distance(leftPoint, middlePoint);
			var b:Number = distance(middlePoint, rightPoint);
			var c:Number = distance(leftPoint, rightPoint);
			return Math.acos((a*a + b*b - c*c)/(2*a*b));
		}
		
		/**
		 * The method intersects the line that connects the center of a given figure 
		 * (using an array of integers: x, y, width, height) and a given point with 
		 * the egdes of the figure. In this case, the figure is a rectangle. 
		 */ 
		public static function computeClipBindablePoint(rect:Array, mid2:BindablePoint):Array {
			var result:BindablePoint = new BindablePoint(int.MIN_VALUE, int.MIN_VALUE);
			var mid1:BindablePoint = getCenterBindablePoint(rect);
			
			// the corners for the rectangle	
			var A:BindablePoint = new BindablePoint(rect[0], rect[1]);
			var B:BindablePoint = new BindablePoint(rect[0], rect[1] + rect[3]);
			var C:BindablePoint = new BindablePoint(rect[0] + rect[2], rect[1] + rect[3]);
			var D:BindablePoint = new BindablePoint(rect[0] + rect[2], rect[1]);
			
			if (rect[2] == 0 && rect[3] == 0) // no need to clip the line when it is connected to another line
				return rect;
			
			var p:BindablePoint;
			p = lineIntersection(mid1, mid2, A, B);
			if (distance(mid2, result) > distance(mid2, p))
				result = p;
			
			p = lineIntersection(mid1, mid2, B, C);
			if (distance(mid2, result) > distance(mid2, p)) 
				result = p;
						
			p = lineIntersection(mid1, mid2, C, D);
			if (distance(mid2, result) > distance(mid2, p)) 
				result = p;
			
			p = lineIntersection(mid1, mid2, D, A);
			if (distance(mid2, result) > distance(mid2, p)) 
				result = p;
				
			if(result.x == int.MIN_VALUE){
				var a1:int = Math.abs(mid2.x - rect[0]);
				var a2:int = Math.abs(rect[0] + rect[2] - mid2.x);
				var b1:int = Math.abs(mid2.y - rect[1]);
				var b2:int = Math.abs(rect[1] + rect[3] - mid2.y);
				if (a1 < a2)
					result.x = rect[0];
				else
					result.x = rect[0] + rect[2]; 
				
				if (b1 < b2)
					result.y = rect[1];
				else
					result.y = rect[1] + rect[3];
			}
			return [result.x, result.y];
		}
		
		/**
		 * The method returns an array containing two boolean values
		 * which represent:
		 * <ul>
		 * 		<li> if the connection intersects an horizontal elge of
		 * 			the rectangle area of its source/target;
		 * 		<li> if the connection intersects a top or left edge of
		 * 			the rectangle area of its source/target;
		 * </ul>
		 * 
		 * <p>The connection is represented by the line which connects
		 * the middle of the rectangle and mid2.
		 * 
		 * @param rect - the rectangular source/target figure
		 * @param mid2 - the (x,y) coordinates of one point from the connection
		 * 		(used to compute the connection line equation needed for computing
		 * 		which edge of the rectangle intersects the connection) 
		 * 
		 */
		public static function computeEdgeIntersectionProperty(rect:Array, mid2:BindablePoint):Array {
			var result:BindablePoint = new BindablePoint(int.MIN_VALUE, int.MIN_VALUE);
			var mid1:BindablePoint = getCenterBindablePoint(rect);
			
			var A:BindablePoint = new BindablePoint(rect[0], rect[1]);
			var B:BindablePoint = new BindablePoint(rect[0], rect[1] + rect[3]);
			var C:BindablePoint = new BindablePoint(rect[0] + rect[2], rect[1] + rect[3]);
			var D:BindablePoint = new BindablePoint(rect[0] + rect[2], rect[1]);
			
			var intersectHorizontal:Boolean;
			var intersectTopOrLeft:Boolean;
			
			var p:BindablePoint;
			// delta is used to avoid flicker when the
			// edge intersects two edges of the rectangle
			// the edge has the slope = 1 or -1
			var delta:int = 5;
			p = lineIntersection(mid1, mid2, A, B);
			if (distance(mid2, result) - delta > distance(mid2, p)) {
				result = p;
				intersectHorizontal = true;
				intersectTopOrLeft = true;
			}

			p = lineIntersection(mid1, mid2, B, C);
			if (distance(mid2, result) - delta > distance(mid2, p)) {
				intersectHorizontal = false;
				intersectTopOrLeft = false;
				result = p;
			}
			
			p = lineIntersection(mid1, mid2, C, D);
			if (distance(mid2, result) - delta > distance(mid2, p)) {
				intersectHorizontal = true;
				intersectTopOrLeft = false;
				result = p;
			}

			p = lineIntersection(mid1, mid2, D, A);
			if (distance(mid2, result) - delta > distance(mid2, p)) {
				intersectHorizontal = false;
				intersectTopOrLeft = true;
				result = p;
			}
			
			return [intersectHorizontal, intersectTopOrLeft];
		}
	}
}