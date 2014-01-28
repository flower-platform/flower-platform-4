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
package org.flowerplatform.flexdiagram.mindmap {
	import flash.display.DisplayObject;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapConnector extends UIComponent {
		
		public var source:Object;
		
		public var target:Object;
				
		public function setSource(value:Object):MindMapConnector {
			this.source = value;
			return this;
		}
		
		public function setTarget(value:Object):MindMapConnector {
			this.target = value;
			return this;
		}
		
		private function getDiagramShell():MindMapDiagramShell {
			return MindMapDiagramShell(DiagramRenderer(this.parent).diagramShell);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			var sourceBounds:Array = getEndBounds(source);
			var targetBounds:Array = getEndBounds(target);
			
			graphics.clear();
			graphics.lineStyle(1, 0x808080);
			
			var sourcePoint:Point;
			var targetPoint:Point;
			
			if (getDiagramShell().getModelController(source).getSide(source) == MindMapDiagramShell.LEFT) {
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
			return new Point(x - getDiagramShell().horizontalPadding/2, y);
		}
		
		private function getRightTopControlPoint(x:Number, y:Number):Point {
			return new Point(x + getDiagramShell().horizontalPadding/2, y);
		}
		
		private function getLeftBottomControlPoint(x:Number, y:Number):Point {
			return new Point(x + getDiagramShell().horizontalPadding/2, y);
		}
		
		private function getRightBottomControlPoint(x:Number, y:Number):Point {
			return new Point(x - getDiagramShell().horizontalPadding/2, y);
		}
		
		protected function getEndBounds(model:Object):Array {
			var endRenderer:IVisualElement = getDiagramShell().getRendererForModel(model);
			if (endRenderer == null) {
				// the renderer is not on the screen; => provide estimates
				var controller:IAbsoluteLayoutRectangleController = getDiagramShell().getControllerProvider(model).getAbsoluteLayoutRectangleController(model);				
				var rect:Rectangle = controller.getBounds(model);
				return [rect.x, rect.y, rect.width, rect.height];
			} else {
				// renderer on screen => provide real data from renderer
				var modelController:IMindMapModelController = IMindMapControllerProvider(getDiagramShell().getControllerProvider(model)).getMindMapModelController(model);				
				return [modelController.getX(model), modelController.getY(model), modelController.getWidth(model), modelController.getHeight(model)];
			}
		}
	}
}