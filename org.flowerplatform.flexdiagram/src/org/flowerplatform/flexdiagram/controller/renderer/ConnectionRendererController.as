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
package org.flowerplatform.flexdiagram.controller.renderer {
	import flash.display.DisplayObject;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.renderer.connection.BindablePoint;
	import org.flowerplatform.flexdiagram.renderer.connection.ClipUtils;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionEnd;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionRenderer;
	
	import spark.components.Label;
	
	/**
	 * @author Cristian Spiescu
	 */	
	public class ConnectionRendererController extends ClassReferenceRendererController {
		protected static const SOURCE_UP:int = 0;
		
		protected static const SOURCE_DOWN:int = 1;
		
		protected static const TARGET_UP:int = 2;
		
		protected static const TARGET_DOWN:int = 3;
		
		protected static const MIDDLE_UP:int = 4;
		
		protected static const MIDDLE_DOWN:int = 5;
		
		protected static const HORIZONTAL_LINE:int = -1;
		
		protected static const VERTICAL_LINE:int = 1;
		
		protected static const ALMOST_HORIZONTAL_LINE:int = 0;

		public function ConnectionRendererController(diagramShell:DiagramShell, rendererClass:Class=null) {
			super(diagramShell, rendererClass);
		}
		
		/**
		 * @author Cristian Spiescu
		 * @author Cristina Constantinescu
		 */ 
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
			updateConnectionEnds(model, null);
			
			var connectionRenderer:ConnectionRenderer = ConnectionRenderer(renderer);
			connectionRenderer.sourceEndType = getSourceEndFigureType(model);
			connectionRenderer.targetEndType = getTargetEndFigureType(model);
		}
		
		/**
		 * This is invoked by the renderer. See comment there to see why.
		 */
		public function updateConnectionEnds(connectionModel:Object, modifiedEnd:Object):void {
			var connectionRenderer:ConnectionRenderer = ConnectionRenderer(diagramShell.getRendererForModel(connectionModel));
			var sourceRect:Array;
			var targetRect:Array;
			var sourceModel:Object = getSourceModel(connectionModel);
			var targetModel:Object = getTargetModel(connectionModel);

//			if (modifiedEnd == null || modifiedEnd == sourceModel) {
				// source
				if (sourceModel == null) {
					// shouldn't normally happen
					sourceRect = [0, 0, 0, 0];					
				} else {
					sourceRect = getEndRectForClipCalculation(sourceModel);
				}
//			}
//			if (modifiedEnd == null || modifiedEnd == targetModel) {
				// target
				if (targetModel == null) {
					// shouldn't normally happen
					targetRect = [50, 50, 50, 50];		
				} else {
					targetRect = getEndRectForClipCalculation(targetModel);
				}
//			}
			
			var nextPointOfConnection:BindablePoint = new BindablePoint(0,0);
			var clippedPoint:Array;
			// source
			nextPointOfConnection.x = targetRect[0] + targetRect[2] / 2;
			nextPointOfConnection.y = targetRect[1] + targetRect[3] / 2;
			clippedPoint = ClipUtils.computeClipBindablePoint(sourceRect, nextPointOfConnection);
			connectionRenderer._sourcePoint.x = clippedPoint[0];
			connectionRenderer._sourcePoint.y = clippedPoint[1];
			
			// target
			nextPointOfConnection.x = clippedPoint[0];
			nextPointOfConnection.y = clippedPoint[1];
			clippedPoint = ClipUtils.computeClipBindablePoint(targetRect, nextPointOfConnection);
			connectionRenderer._targetPoint.x = clippedPoint[0];
			connectionRenderer._targetPoint.y = clippedPoint[1];
			
			updateLabelPosition(connectionModel, connectionRenderer, connectionRenderer.middleConnectionLabel, MIDDLE_UP);
		}
		
		protected function getEndRectForClipCalculation(endModel:Object):Array {
			var endRenderer:IVisualElement = diagramShell.getRendererForModel(endModel);
			if (endRenderer == null) {
				// the renderer is not on the screen; => provide estimates
				return getEstimatedRectForElementNotVisible(endModel);
			} else {
				// renderer on screen => provide real data from renderer				
				var rectRelativeToDiagram:Rectangle = DisplayObject(endRenderer).getBounds(DisplayObject(diagramShell.diagramRenderer));
				// bounds width/height aren't the right ones, use renderer's width/height instead
				return [rectRelativeToDiagram.x, rectRelativeToDiagram.y, endRenderer.width, endRenderer.height];
			}
		}
		
		protected function getEstimatedRectForElementNotVisible(model:Object):Array {
			if (model == null) {
				throw new Error("No parent that has a IAbsoluteLayoutRectangleController has been found!");
			}
			
			var controller:IAbsoluteLayoutRectangleController = diagramShell.getControllerProvider(model).getAbsoluteLayoutRectangleController(model);
			if (controller != null) {
				var rect:Rectangle = controller.getBounds(model);
				return [rect.x, rect.y, rect.width, rect.height];
			} else {
				// look to find the parent that is child of the diagram, i.e. has IAbsoluteLayoutRectangleController
				var childrenController:IModelChildrenController = diagramShell.getControllerProvider(model).getModelChildrenController(model);
				if (childrenController == null) {
					throw new Error("Cannot find a IModelChildrenController for model = " + model + ". Elements should provide IModelChildrenController is they need to be connectable, even if they don't have children!");
				}
				return getEstimatedRectForElementNotVisible(childrenController.getParent(model));
			}
		}
		
		public function getSourceModel(connectionModel:Object):Object {
			throw new Error("This method should be implemented");
		}
		
		public function getTargetModel(connectionModel:Object):Object {
			throw new Error("This method should be implemented");
		}
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		public function getSourceEndFigureType(connectionModel:Object):String {
			return ConnectionEnd.NONE;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		public function getTargetEndFigureType(connectionModel:Object):String {
			return ConnectionEnd.NONE;
		}
		
		public function hasMiddleLabel(connectionModel:Object):Boolean {
			return false;
		}
		
		/**
		 * 
		 * <p>If the connection's orientation changes (an <code>UpdateConnectionEndsEvent</code> is 
		 * sent) the label's position is recomputed. The event is send when the source/target of
		 * the connection is changed, so we need to make sure that the connection figure is valid
		 * (has a source and a target editPart) in order to compute the correct values for
		 * the label.
		 *  
		 * <p>The method computes some information about the connection in order
		 * to provide it to the <code>getLabelPosition()</code> method.
		 * <ul>
		 * 	<li> If the connection intersects the vertical/horizontal edge of the
		 * 	source/target figure, 'isHorizontal' flag will be set to false/true.
		 * 	There is a special case which concerns the label's whose position is
		 * 	"MIDDLE". In this case, 'isHorizontal' flag is computed by the slope of
		 * 	the associated segment.
		 * 	<li> The 'refPoint' variable represents the reference point for the label.
		 * 	This could be the sourcePoint/targetPoint/midPoint of the connection.
		 * 	<li> The 'isTopOrLeft' flag is computed based on the information given 
		 * 	by the <code>getPositionType()</code> method; this flag is set for the 
		 * 	labels whose position constant is UP.
		 * 	<li> The 'alignTopOrLeft' flag is computed by the 
		 * 	<code>ClipUtils.computeEdgeIntersectionProperty()</code> method.
		 * </ul>
		 * 
		 */
		protected function updateLabelPosition(connectionModel:Object, connectionRenderer:ConnectionRenderer, connectionLabel:Label, posType:int):void {
			if (connectionLabel == null || getSourceModel(connectionModel) == null || getTargetModel(connectionModel) == null) {
				return;
			}
			// this will be :
			// ALMOST_HORIZONTAL_LINE when the line is very close to horizontal
			// HORIZONTAL_LINE when horizontal
			// VERTICAL_LINE when vertical 
			var lineType:int; 
			var isTopOrLeft:Boolean;
			var refPoint:BindablePoint;
			var segmId:int;
			var array:Array;
			var alignTopOrLeft:Boolean;
			var slope:Number;
			
			switch (posType) {
				case SOURCE_UP:
					segmId = 0;
					isTopOrLeft = true;
					refPoint = connectionRenderer._sourcePoint;
					array = ClipUtils.computeEdgeIntersectionProperty(
						getEndRectForClipCalculation(getSourceModel(connectionModel)), refPoint);
					break;
				case SOURCE_DOWN:
					segmId = 0;
					isTopOrLeft = false;
					refPoint = connectionRenderer._sourcePoint;
					array = ClipUtils.computeEdgeIntersectionProperty(
						getEndRectForClipCalculation(getSourceModel(connectionModel)), refPoint);
					break;
				case TARGET_UP:
					segmId = connectionRenderer.getNumberOfSegments() - 1;
					isTopOrLeft = true;
					refPoint = connectionRenderer._targetPoint;
					array = ClipUtils.computeEdgeIntersectionProperty(
						getEndRectForClipCalculation(getTargetModel(connectionModel)), refPoint);
					break;
				case TARGET_DOWN:
					segmId = connectionRenderer.getNumberOfSegments() - 1;
					isTopOrLeft = false;
					refPoint = connectionRenderer._targetPoint;
					array = ClipUtils.computeEdgeIntersectionProperty(
						getEndRectForClipCalculation(getTargetModel(connectionModel)), refPoint);
					break;
				case MIDDLE_UP:
					var arr:Array = connectionRenderer.getPointFromDistance() as Array;
					refPoint = arr[0];
					segmId = arr[1];
					slope = connectionRenderer.getSegmentAt(segmId).getSegmentSlope();
					if (Math.abs(slope) <= 0.1) {
						lineType = ALMOST_HORIZONTAL_LINE; // almost perfectly horizontal
					} else if (Math.abs(slope) < 1) { 
						lineType = HORIZONTAL_LINE;
					} else {
						lineType = VERTICAL_LINE;
					}
					if (lineType != VERTICAL_LINE) {
						alignTopOrLeft = false;
						if (slope < 0)
							isTopOrLeft = false;
						else
							isTopOrLeft = true;
					} else {
						isTopOrLeft = true;
						if (slope < 0)
							alignTopOrLeft = true;
						else 
							alignTopOrLeft = false;
					}
					break;
			}
			connectionLabel.validateProperties();
			connectionLabel.invalidateSize();
			if (posType != MIDDLE_UP) {
				lineType = array[0] == true ? HORIZONTAL_LINE : VERTICAL_LINE;
				alignTopOrLeft = array[1];
			} 
			var p:Point = getLabelPosition(refPoint, connectionLabel.width + 5, connectionLabel.height + 5, lineType, isTopOrLeft, alignTopOrLeft);
			connectionLabel.x = p.x;
			connectionLabel.y = p.y;
			connectionLabel.invalidateDisplayList();
		}
		
		/**
		 * The method computes the (x,y) coordinated for the label. It is called by
		 * the <code>computeLabelPosition()</code> method. 
		 */
		private function getLabelPosition(referencePoint:BindablePoint, labelWidth:int, labelHeight:int, 
										  lineType:int, isTopOrIsLeft:Boolean, alignLeftOrTop:Boolean):Point {
			var resultPoint:Point = new Point();
			if (lineType != VERTICAL_LINE) {
				if (!alignLeftOrTop)
					resultPoint.x = (lineType == ALMOST_HORIZONTAL_LINE) ? referencePoint.x - labelWidth / 2 // center the label on the line when the line is almost horizontal
						: referencePoint.x + 5; //shadow correction
				else
					resultPoint.x = referencePoint.x - labelWidth;
				if (isTopOrIsLeft) 
					resultPoint.y = referencePoint.y - labelHeight;
				else
					resultPoint.y = referencePoint.y;
			} else {
				if (isTopOrIsLeft) 
					resultPoint.x = referencePoint.x - labelWidth;
				else 
					resultPoint.x = referencePoint.x;
				if (!alignLeftOrTop) 
					resultPoint.y = referencePoint.y;
				else 
					resultPoint.y = referencePoint.y - labelHeight;
			}
			return resultPoint;
		}

	}
}