/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
 * license-end
 */
package org.flowerplatform.flexdiagram.mindmap {
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GenericMindMapConnector extends UIComponent {
		
		public var source:Object;
		
		public var target:Object;
				
		private var context:DiagramShellContext;
		
		public function GenericMindMapConnector() {
			depth = int.MAX_VALUE; // model has depth, so put connector above
			mouseEnabled = false;
		}
		
		public function setSource(value:Object):GenericMindMapConnector {
			this.source = value;
			return this;
		}
		
		public function setTarget(value:Object):GenericMindMapConnector {
			this.target = value;
			return this;
		}
		
		public function setContext(value:DiagramShellContext):GenericMindMapConnector {
			this.context = value;
			return this;
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		protected function getColor():uint {
			return 0x808080;
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		protected function getEdgeWidth():int {
			return 1;
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		protected function getEdgeStyle():String {
			return FlexDiagramConstants.EDGE_SMOOTHLY_CURVED;
		}
		
		private function get diagramShell():MindMapDiagramShell {
			return MindMapDiagramShell(context.diagramShell);
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			var edgeStyle:String = getEdgeStyle();
			
			switch(edgeStyle) {
				case FlexDiagramConstants.EDGE_HIDE: {
					graphics.clear();
					break;
				}
				case FlexDiagramConstants.EDGE_SMOOTHLY_CURVED: {
					smoothlyCurvedEdge(unscaledWidth, unscaledHeight);
					break;
				}
				case FlexDiagramConstants.EDGE_HORIZONTAL: {
					horizontalEdge(unscaledWidth, unscaledHeight);
					break;
				}
				case FlexDiagramConstants.EDGE_LINEAR: {
					linearEdge(unscaledWidth, unscaledHeight);
					break;
				}
			}
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		private function horizontalEdge(unscaledWidth:Number, unscaledHeight:Number):void {
			var sourceBounds:Array = getEndBounds(source);
			var targetBounds:Array = getEndBounds(target);
			
			var edgeWidth:int = getEdgeWidth();
			var color:uint = getColor();
			
			graphics.clear();
			graphics.lineStyle(edgeWidth, color);
			
			var sourcePoint:Point;
			var targetPoint:Point;
			
			if (diagramShell.getModelController(context, source).getSide(context, source) == MindMapDiagramShell.POSITION_LEFT) {
				sourcePoint = new Point(sourceBounds[0] + sourceBounds[2], sourceBounds[1] + sourceBounds[3]/2);
				targetPoint = new Point(targetBounds[0], targetBounds[1] + targetBounds[3]/2);
			} else {
				sourcePoint = new Point(sourceBounds[0], sourceBounds[1] + sourceBounds[3]/2);
				targetPoint = new Point(targetBounds[0] + targetBounds[2], targetBounds[1] + targetBounds[3]/2);
			}		
			graphics.moveTo(sourcePoint.x, sourcePoint.y);
			graphics.lineTo(targetPoint.x/2 + sourcePoint.x/2, sourcePoint.y);
			graphics.lineTo(targetPoint.x/2 + sourcePoint.x/2, targetPoint.y);
			graphics.lineTo(targetPoint.x, targetPoint.y);
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		private function linearEdge(unscaledWidth:Number, unscaledHeight:Number):void {
			var sourceBounds:Array = getEndBounds(source);
			var targetBounds:Array = getEndBounds(target);
			
			var edgeWidth:int = getEdgeWidth();
			var color:uint = getColor();
			
			graphics.clear();
			graphics.lineStyle(edgeWidth, color);
			
			var sourcePoint:Point;
			var targetPoint:Point;
			
			if (diagramShell.getModelController(context, source).getSide(context, source) == MindMapDiagramShell.POSITION_LEFT) {
				sourcePoint = new Point(sourceBounds[0] + sourceBounds[2], sourceBounds[1] + sourceBounds[3]/2);
				targetPoint = new Point(targetBounds[0], targetBounds[1] + targetBounds[3]/2);
				graphics.moveTo(sourcePoint.x, sourcePoint.y);
				graphics.lineTo(targetPoint.x, targetPoint.y);
			} else {
				sourcePoint = new Point(sourceBounds[0], sourceBounds[1] + sourceBounds[3]/2);
				targetPoint = new Point(targetBounds[0] + targetBounds[2], targetBounds[1] + targetBounds[3]/2);
				graphics.moveTo(sourcePoint.x, sourcePoint.y);			
				graphics.lineTo(targetPoint.x, targetPoint.y);
			}			
		}
		
		private function smoothlyCurvedEdge (unscaledWidth:Number, unscaledHeight:Number):void {
			var sourceBounds:Array = getEndBounds(source);
			var targetBounds:Array = getEndBounds(target);
			
			var edgeWidth:int = getEdgeWidth();
			var color:uint = getColor();
			
			graphics.clear();
			graphics.lineStyle(edgeWidth, color);
			
			var sourcePoint:Point;
			var targetPoint:Point;
			
			if (diagramShell.getModelController(context, source).getSide(context, source) == MindMapDiagramShell.POSITION_LEFT) {
				sourcePoint = new Point(sourceBounds[0] + sourceBounds[2], sourceBounds[1] + sourceBounds[3]/2);
				targetPoint = new Point(targetBounds[0], targetBounds[1] + targetBounds[3]/2);
				graphics.moveTo(sourcePoint.x, sourcePoint.y);
				
				if (sourceBounds[1] == targetBounds[1]) {
					graphics.lineTo(targetPoint.x, targetPoint.y);
				} else if (sourceBounds[1] < targetBounds[1]) {
					graphics.cubicCurveTo(					
						getRightTopControlPoint(sourcePoint.x, sourcePoint.y).x, 
						getRightTopControlPoint(sourcePoint.x, sourcePoint.y).y,
						getRightBottomControlPoint(targetPoint.x, targetPoint.y).x,
						getRightBottomControlPoint(targetPoint.x, targetPoint.y).y,
						targetPoint.x, 
						targetPoint.y);
				} else if (sourceBounds[1] > targetBounds[1]) {
					graphics.cubicCurveTo(					
						getLeftBottomControlPoint(sourcePoint.x, sourcePoint.y).x, 
						getLeftBottomControlPoint(sourcePoint.x, sourcePoint.y).y,
						getLeftTopControlPoint(targetPoint.x, targetPoint.y).x,
						getLeftTopControlPoint(targetPoint.x, targetPoint.y).y,
						targetPoint.x, 
						targetPoint.y);
				}			
			} else {
				sourcePoint = new Point(sourceBounds[0], sourceBounds[1] + sourceBounds[3]/2);
				targetPoint = new Point(targetBounds[0] + targetBounds[2], targetBounds[1] + targetBounds[3]/2);
				graphics.moveTo(sourcePoint.x, sourcePoint.y);			
				if (sourceBounds[1] == targetBounds[1]) {
					graphics.lineTo(targetPoint.x, targetPoint.y);
				} else if (sourceBounds[1] < targetBounds[1]) {
					graphics.cubicCurveTo(					
						getLeftTopControlPoint(sourcePoint.x, sourcePoint.y).x, 
						getLeftTopControlPoint(sourcePoint.x, sourcePoint.y).y,
						getLeftBottomControlPoint(targetPoint.x, targetPoint.y).x,
						getLeftBottomControlPoint(targetPoint.x, targetPoint.y).y,
						targetPoint.x, 
						targetPoint.y);
				} else if (sourceBounds[1] > targetBounds[1]) {
					graphics.cubicCurveTo(					
						getRightBottomControlPoint(sourcePoint.x, sourcePoint.y).x, 
						getRightBottomControlPoint(sourcePoint.x, sourcePoint.y).y,
						getLeftBottomControlPoint(targetPoint.x, targetPoint.y).x,
						getLeftBottomControlPoint(targetPoint.x, targetPoint.y).y,
						targetPoint.x, 
						targetPoint.y);
				}
			}				
		}
		private function getLeftTopControlPoint(x:Number, y:Number):Point {
			return new Point(x - diagramShell.horizontalPadding/2, y);
		}
		
		private function getRightTopControlPoint(x:Number, y:Number):Point {
			return new Point(x + diagramShell.horizontalPadding/2, y);
		}
		
		private function getLeftBottomControlPoint(x:Number, y:Number):Point {
			return new Point(x + diagramShell.horizontalPadding/2, y);
		}
		
		private function getRightBottomControlPoint(x:Number, y:Number):Point {
			return new Point(x - diagramShell.horizontalPadding/2, y);
		}
		
		protected function getEndBounds(model:Object):Array {
			var endRenderer:IVisualElement = diagramShell.getRendererForModel(context, model);
			
			// the renderer is not on the screen; => provide estimates
			var controller:AbsoluteLayoutRectangleController = ControllerUtils.getAbsoluteLayoutRectangleController(context, model);				
			var rect:Rectangle = controller.getBounds(context, model);
			return [rect.x, rect.y, rect.width, rect.height];
		}
	}
}