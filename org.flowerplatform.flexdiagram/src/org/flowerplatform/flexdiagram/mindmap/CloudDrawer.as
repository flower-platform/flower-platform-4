package org.flowerplatform.flexdiagram.mindmap {
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Alexandra Topoloaga
	 */
	public class CloudDrawer {
	
		public var center:Boolean = false;
		
		public var nr:int = 0;
		
		public var firstX:Number;
		
		public var firstY:Number;
		
		public var diagramShell:MindMapDiagramShell;
		
		public var side:int;
		
		public function drawCloud(renderer:MindMapRenderer, unscaledWidth:Number, unscaledHeight:Number):void {			
			if (renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_ARC || 
					renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_STAR ||
					renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_RECTANGLE ||
					renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_ROUNDED_RECTANGLE) {
				renderer.graphics.lineStyle(2, 0x808080); // gray line with bigger thickness
				renderer.graphics.beginFill(Utils.convertValueToColor(renderer.cloudColor), 1);
				
				var diagramShell:MindMapDiagramShell = MindMapDiagramShell(renderer.diagramShellContext.diagramShell);
				diagramShell.setPropertyValue(renderer.diagramShellContext, renderer.data, "additionalPadding", diagramShell.additionalPadding);
				var cloudPadding:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, renderer.data, "additionalPadding");
				var side:int = diagramShell.getModelController(renderer.diagramShellContext, renderer.data).getSide(renderer.diagramShellContext, renderer.data);
				
				var expandedWidth:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, renderer.data, "expandedWidth");
				var expandedHeight:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, renderer.data, "expandedHeight");
				
				var shapeX:Number = - cloudPadding/2;
				var shapeY:Number = - diagramShell.getDeltaBetweenExpandedHeightAndHeight(renderer.diagramShellContext, renderer.data, true)/2;
				var shapeWidth:Number = expandedWidth + cloudPadding;
				var shapeHeight:Number = Math.max(expandedHeight, renderer.height + cloudPadding);
				
				var expandedWidthLeft:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, renderer.data, "expandedWidthLeft");
				var expandedWidthRight:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, renderer.data, "expandedWidthRight");
				var expandedHeightLeft:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, renderer.data, "expandedHeightLeft");
				var expandedHeightRight:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, renderer.data, "expandedHeightRight");
				
				if (side == MindMapDiagramShell.POSITION_LEFT) {
					shapeX -= (expandedWidth - renderer.width);
				}
				
				if (side == MindMapDiagramShell.POSITION_CENTER) {
					shapeX -= expandedWidthLeft - renderer.width;
					shapeY = - (Math.max(expandedHeightLeft, expandedHeightRight) - renderer.height - cloudPadding)/2;
					shapeWidth = expandedWidthLeft + expandedWidthRight - renderer.width; 
					shapeHeight = Math.max(expandedHeightLeft, expandedHeightRight);
				}
				
				if (renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_RECTANGLE) {
					renderer.graphics.drawRect(shapeX, shapeY, shapeWidth, shapeHeight);
				} else if (renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_ROUNDED_RECTANGLE){
					renderer.graphics.drawRoundRect(shapeX, shapeY, shapeWidth, shapeHeight, 25, 25);					
				} else if (renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_ARC || renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_STAR) {
					drawArcOrStar(renderer);
				}
				renderer.graphics.endFill();
			}
		}
		
		public function drawArcOrStar(renderer:MindMapRenderer):void {
			var coordinates:ArrayCollection = new ArrayCollection();
			nr = 0;
			getCoordinates(renderer, coordinates, Object(renderer.data));
			var hull:ConvexHull = new ConvexHull();
			var res:ArrayCollection = new ArrayCollection();
			res = hull.calculateHull(coordinates);
			var p:ArrayCollection = new ArrayCollection();
			var lastPt:Point = new Point();
			for (var i:int = 0; i < res.length; ++i) {
				var pt:Point = Point(res.getItemAt(i));
				if (!pt.equals(lastPt)) {
					p.addItem(pt);
					lastPt = pt;
				}
			}
			pt = Point (res.getItemAt(0));
			p.addItem(pt);
			if (center) {
				renderer.graphics.moveTo(Point(p.getItemAt(0)).x, Point(p.getItemAt(0)).y);
			} else {
				if (side == MindMapDiagramShell.POSITION_LEFT) {
					renderer.graphics.moveTo(Point(p.getItemAt(0)).x - firstX , Point(p.getItemAt(0)).y - firstY);
				}
				else if (side == MindMapDiagramShell.POSITION_RIGHT) {
					renderer.graphics.moveTo(Point(p.getItemAt(0)).x - firstX, Point(p.getItemAt(0)).y  - firstY);
				}
			}
			paintDecoration(renderer, p);
		}
		
		public function paintDecoration(renderer:MindMapRenderer, p:ArrayCollection):void {
			var middleDistanceBetweenPoints:Number;
			if (renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_ARC) {
				middleDistanceBetweenPoints = 120;
			} else if (renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_STAR) {
				middleDistanceBetweenPoints = 20;
			}
			
			var xpoints:ArrayCollection = new ArrayCollection();
			var ypoints:ArrayCollection = new ArrayCollection();
			var i:int;
			for (i = 0; i < p.length; i++) {
				xpoints.addItem(p.getItemAt(i).x);
				ypoints.addItem(p.getItemAt(i).y);
			}
			var lastPoint:Point = new Point(Number(xpoints.getItemAt(0)), Number(ypoints.getItemAt(0)));
			var x0:Number;
			var y0:Number;
			x0 = lastPoint.x;
			y0 = lastPoint.y;
			var x2:Number;
			var y2:Number;
			x2 = x0;
			y2 = y0; //primul si ultimul punct coincid
			for (i = p.length - 2; i >= 0; --i) {
				var nextPoint:Point = new Point(Number(xpoints.getItemAt(i)), Number(ypoints.getItemAt(i)));
				var x1:Number;
				var y1:Number;
				var x3:Number;
				var y3:Number;
				var dx:Number;
				var dy:Number;
				var dxn:Number;
				var dyn:Number;
				x1 = nextPoint.x;
				y1 = nextPoint.y;
				dx = x1 - x0; /* direction of p0 -> p1 */
				dy = y1 - y0;
				var length:Number = Math.sqrt(dx * dx + dy * dy);
				dxn = dx / length; /* normalized direction of p0 -> p1 */
				dyn = dy / length;
				for (var j:int = 0;;) {
					var distanceBetweenPoints:Number = middleDistanceBetweenPoints * random(0.7);
					if (j + 2* distanceBetweenPoints < length) {
						j += distanceBetweenPoints;
						x3 = x0 + j * dxn;
						/* the drawing end point.*/
						y3 = y0 + j * dyn;
					}
					else {
						/* last point */
						break;
					}
					paintDeco(renderer, x2, y2, x3, y3);
					x2 = x3;
					y2 = y3;
				}
				
				paintDeco(renderer, x2, y2, x1, y1);  
				x2 = x1;
				y2 = y1;
				x0 = x1;
				y0 = y1;
			}
		}
		
		public function paintDeco(renderer: MindMapRenderer, x0:Number, y0:Number, x1:Number, y1:Number):void {
			var dx:Number;
			var dy:Number;
			dx = x1 - x0;
			dy = y1 - y0;
			var length:Number = Math.sqrt(dx * dx + dy * dy);
			var dxn:Number;
			var dyn:Number;
			dxn = dx / length;
			dyn = dy / length;
			if (center) {
				paintD(renderer, x0, y0, x1, y1, dx, dy, dxn, dyn);
			} else {
				if (side == MindMapDiagramShell.POSITION_LEFT) {
					paintD(renderer, x0 - firstX, y0 - firstY, x1 - firstX, y1 - firstY, dx, dy, dxn, dyn);
				} else if (side == MindMapDiagramShell.POSITION_RIGHT) {
					paintD(renderer, x0 - firstX, y0 - firstY, x1 - firstX, y1 - firstY, dx, dy, dxn, dyn);
				}
			}
		}
		
		public function paintD(renderer:MindMapRenderer, x0:Number, y0:Number, x1:Number, y1:Number, dx:Number, dy:Number, dxn:Number, dyn:Number):void {
			var xctrl:Number;
			var yctrl:Number;
			var middleDistanceToConvexHull:Number; 
			var distanceToConvexHull:Number;
			if (renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_ARC) {
				middleDistanceToConvexHull = 20;
				distanceToConvexHull = middleDistanceToConvexHull * 2.2 * random(0.7);
				xctrl = x0 + 0.5 * dx - distanceToConvexHull * dyn;
				yctrl = y0 + 0.5 * dy + distanceToConvexHull * dxn; 
				renderer.graphics.curveTo(xctrl, yctrl, x1, y1);
			} else if (renderer.cloudType == FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE_STAR) {
				middleDistanceToConvexHull = 40;
				distanceToConvexHull = middleDistanceToConvexHull * random(0.5);
				var k:Number = random(0.3);
				xctrl = x0 + 0.5 * dx * k - distanceToConvexHull * dyn;
				yctrl = y0 + 0.5 * dy * k + distanceToConvexHull * dxn;
				renderer.graphics.lineTo(xctrl, yctrl);
				renderer.graphics.lineTo(x1, y1);
			}
			
		}
		
		public function getCoordinates(renderer:MindMapRenderer, inList:ArrayCollection, node:Object):void {
			diagramShell = MindMapDiagramShell(renderer.diagramShellContext.diagramShell);
			diagramShell.setPropertyValue(renderer.diagramShellContext, node, "additionalPadding", diagramShell.additionalPadding);
			side = diagramShell.getModelController(renderer.diagramShellContext, node).getSide(renderer.diagramShellContext, node);
			var coordWidth:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, node, "width");
			var coordHeight:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, node, "height");
			var coordX:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, node, "x");
			var coordY:Number = diagramShell.getPropertyValue(renderer.diagramShellContext, node, "y");
			
			nr ++;
			if (side == MindMapDiagramShell.POSITION_CENTER) {
				center = true;
			}
			if (nr == 1) {
				firstX = coordX;
				firstY = coordY;
			}
			
			inList.addItem(new Point(coordX, coordY));
			inList.addItem(new Point(coordX, coordY + coordHeight));
			inList.addItem(new Point(coordX + coordWidth, coordY + coordHeight));
			inList.addItem(new Point(coordX + coordWidth, coordY));
			
			var mindMapModelController:MindMapModelController  = ControllerUtils.getMindMapModelController(renderer.diagramShellContext, node);
			if (mindMapModelController.getExpanded(renderer.diagramShellContext, node) == true) {
				var childrenList:IList = mindMapModelController.getChildren(renderer.diagramShellContext, node);
				var i:int = 0;
				while (childrenList != null && i < childrenList.length) {
					i++;
					getCoordinates(renderer, inList, childrenList.getItemAt(i-1));
				}
			}
		}
		
		public function random(min:Number):Number {
			return (min + (1-min) *  Math.random());
		}
		
	}
}
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort; 

	class ConvexHull {	
		public var p0:Point = new Point();
		
		public function calculateHull(coordinates:ArrayCollection): ArrayCollection {
			// use a copy of coordinates since it will get modified in doGraham()
			var arrayAux:ArrayCollection = new ArrayCollection();
			arrayAux.addAll(coordinates);
			return doGraham(arrayAux);
		}
		
		public function ccw(p0:Point, p1:Point, p2:Point):int {
			var dx1: int;
			var dx2: int;
			var dy1: int;
			var dy2: int;
			dx1 = p1.x - p0.x;
			dy1 = p1.y - p0.y;
			dx2 = p2.x - p0.x;
			dy2 = p2.y - p0.y;
			var comp:int = dx1 * dy2 - dy1 * dx2;
			if (comp > 0) {
				return 1;
			}
			if (comp < 0) {
				return -1;
			}
			if ((dx1 * dx2 < 0) || (dy1 * dy2 < 0)) {
				return -1;
			}
			if (dx1 * dx1 + dy1 * dy1 >= dx2 * dx2 + dy2 * dy2) {
				return 0;
			}
			return 1;
		}
		
		public function doGraham(p:ArrayCollection): ArrayCollection {
			var i:int;
			var min:int;
			var m:int;
			var t:Point;
			min = 0; 
			var newArray:ArrayCollection = new ArrayCollection();
			for (i = 1; i < p.length; ++i) {
				if ((Point (p.getItemAt(i)).y) < (Point(p.getItemAt(min)).y)) {
					min = i;
				}
			}
			for (i = 0; i < p.length; ++i) {
				if ((Point (p.getItemAt(i)).y) == (Point(p.getItemAt(min)).y) && 
					(Point (p.getItemAt(i)).x) > (Point(p.getItemAt(min)).x)) {
					min = i;
				}
			}
			
			t = Point(p.getItemAt(0));
			p.setItemAt(Point(p.getItemAt(min)), 0);
			p.setItemAt(t, min);
			p0 = Point(p.getItemAt(0));
			var pointsSort:Sort = new Sort();
			pointsSort.compareFunction = compare;
			p.sort = pointsSort;		
			p.refresh();
			p.sort = null;
			newArray.addAll(p);
			var temp:Point = Point(p.getItemAt(p.length - 1));
			newArray.addItemAt(temp, 0);
			p = newArray;
			m = 3;
			for (i = 4; i < p.length; ++i) {
				while ((m > 0) && (ccw(Point (p.getItemAt(m)), Point(p.getItemAt(m-1)), Point(p.getItemAt(i))) >= 0)) {
					m--;
				}
				m++;
				t = Point(p.getItemAt(m));
				p.setItemAt(p.getItemAt(i), m);
				p.setItemAt(t, i);
			}
			p.removeItemAt(0);
			while (m < p.length) {
				p.removeItemAt(p.length - 1);
			}
			while (m > p.length) {
				p.addItem(null);
			}
			return p;
		}
		
		
		public function compare(p1:Object, p2:Object, fields:Array = null):int {
			var comp:Number = theta(p0, Point(p1)) - theta(p0, Point(p2));
			if (Point(p1).equals(Point(p2))) {
				return 0;
			}
			if (comp > 0) {
				return 1;
			}
			if (comp < 0) {
				return -1;
			}
			var dx1:int;
			var dx2:int;
			var dy1:int;
			var dy2:int;
			dx1 = Point (p1).x - (p0).x;
			dy1 = Point (p1).y - (p0).y;
			dx2 = Point (p2).x - (p0).x;
			dy2 = Point (p2).y - (p0).y;
			var comp2:int = (dx1 * dx1 + dy1 * dy1) - (dx2 * dx2 + dy2 * dy2);
			if (comp2 > 0) {
				return -1; 
			}
			if (comp2 < 0) {
				return 1;
			}
			return 0;
		}
		
		public function theta(p1:Point, p2:Point):Number {
			var dx:int;
			var dy:int;
			var ax:int;
			var ay:int;
			var t:Number;
			dx = p2.x - p1.x;
			ax = Math.abs(dx);
			dy = p2.y - p1.y;
			ay = Math.abs(dy);
			if ((dx == 0) && (dy == 0)) {
				t = 0;
			} else {
				t = (Number(dy))/(Number (ax + ay));
			}
			if (dx < 0) {
				t = 2 - t;
			} else {
				if (dy < 0) {
					t = 4 + t;
				}
			}
			return t * 90;
		}
		
	}
