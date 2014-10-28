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
	import flash.geom.Point;
	
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class MultiConnectorRenderer extends UIComponent	implements IDataRenderer, IDiagramShellContextAware {
		
		private var _diagramShellContext:DiagramShellContext;
		
		private var _data:MultiConnectorModel;
		
		public var lineColor:uint = 0x808080;
		
		public var lineWidth:Number = 1;
		
		public function MultiConnectorRenderer() {
			// so that it appears above all elements (e.g. clouds)
			depth = Infinity;
			mouseEnabled = false;
		}
		
		public function get diagramShellContext():DiagramShellContext {
			return _diagramShellContext;
		}

		public function set diagramShellContext(value:DiagramShellContext):void {
			_diagramShellContext = value;
		}

		public function get data():Object {
			return _data;
		}
		
		public function set data(value:Object):void {
			width = MindMapDiagramShell(diagramShellContext.diagramShell).horizontalPadding;
			if (_data != null) {
				_data.source.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			}
			_data = MultiConnectorModel(value);			
			if (_data != null) {
				_data.source.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				modelChangedHandler(null);
			}
		}
		
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
			if (event == null || "x" == event.property) {
				x = _data.x;
			}
			if (event == null || "y" == event.property) {
				y = _data.y;
				invalidateDisplayList();
			}
			if (event == null || "width" == event.property) {
				width = _data.width;
			}
			if (event == null || "height" == event.property) {
				height = _data.height;
				invalidateDisplayList();
			}
			if (event == null || FlexDiagramConstants.MIND_MAP_RENDERER_CHILD_CONNECTOR_PROPERTIES == event.property) {
				invalidateDisplayList();
			}
		}
		
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);

			var mindMapDiagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShellContext.diagramShell);
			var mindMapModelController:MindMapModelController = mindMapDiagramShell.getModelController(diagramShellContext, _data.source);
			var children:IList = mindMapModelController.getChildren(diagramShellContext, _data.source);
			
			graphics.clear();
			graphics.lineStyle(lineWidth, lineColor);
			
			// usefull for debugging
//			graphics.beginFill(0x00DD55, 1);
//			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
//			graphics.endFill();
			
			
			var x1:Number, y1:Number, x2:Number, y2:Number;
			if (_data.isRight) {
				x1 = 0;
			} else {
				x1 = width;
			}
			y1 = mindMapDiagramShell.getPropertyValue(diagramShellContext, _data.source, "y") + mindMapDiagramShell.getPropertyValue(diagramShellContext, _data.source, "height") / 2  - y;
			
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				if (_data.isForRoot) {
					// check side of child
					var childIsRight:Boolean = mindMapDiagramShell.getModelController(diagramShellContext, child).getSide(diagramShellContext, child) == MindMapDiagramShell.POSITION_RIGHT;
					if (childIsRight != _data.isRight) {
						// skip, because it's from the other side
						continue;
					}
				}
				if (_data.isRight) {
					x2 = mindMapDiagramShell.getPropertyValue(diagramShellContext, child, "x") - x;
				} else {
					x2 = 0; 
				}
				
				y2 = mindMapDiagramShell.getPropertyValue(diagramShellContext, child, "y") + mindMapDiagramShell.getPropertyValue(diagramShellContext, child, "height") / 2  - y;
				
//				if (child.connectorColor != 0 || child.connectorStyle != null || child.connectorWidth != null) {
//					// check if color or width is selected
//					if (child.connectorColor != 0 && (child.connectorWidth != null || child.connectorWidth != "")) {
//						graphics.lineStyle(Number(child.connectorWidth), child.connectorColor);
//					} else if (child.connectorColor != 0 ) {
//						graphics.lineStyle(lineWidth, child.connectorColor);
//					} else if (child.connectorWidth != null || child.connectorWidth != "") {
//						graphics.lineStyle(Number(child.connectorWidth), lineColor);
//					}
//					
//					// check if style is selected
//					if (child.connectorStyle == null || child.connectorStyle == "") {
//						drawStraightLine(x1 ,y1, x2, y2);
//					} else {
//						changeConnectorStyle(child, x1, y1);
//					}
//					
//				} else {
//					graphics.lineStyle(lineWidth, lineColor);
//					drawStraightLine(x1, y1, x2, y2);
//				}
			}
		}
		
		protected function changeConnectorStyle(child:Object, xParent:Number, yParent:Number):void {
			
			var mindMapDiagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShellContext.diagramShell);
			var connectorStyle:String = child.connectorStyle;
			var connectorColor:uint = child.connectorColor;
			var connectorWidth:String = child.connectorWidth;
			var sideChildIsRight:Boolean = child.side == MindMapDiagramShell.POSITION_RIGHT;
			var xChild:int = mindMapDiagramShell.getPropertyValue(diagramShellContext, child, "x");
			var yChild:int = mindMapDiagramShell.getPropertyValue(diagramShellContext, child, "y");
			var widthChild:int = mindMapDiagramShell.getPropertyValue(diagramShellContext, child, "width");
			var heightChild:int = mindMapDiagramShell.getPropertyValue(diagramShellContext, child , "height");
		
			if (sideChildIsRight) {
				xChild -= x;
			} else {
				xChild += widthChild - x;
			}
			
			yChild += heightChild / 2 - y;
			
			switch(connectorStyle) {
				case FlexDiagramConstants.MIND_MAP_CONNECTOR_HIDE:
					//graphics.clear();
					graphics.moveTo(xChild , yChild);
					break;
					
				case FlexDiagramConstants.MIND_MAP_CONNECTOR_LINEAR:
					drawStraightLine(xParent, yParent, xChild, yChild);
					break;
				
				case FlexDiagramConstants.MIND_MAP_CONNECTOR_HORIZONTAL:
					drawHorizontalConnector(xParent, yParent, xChild, yChild, sideChildIsRight);
					break;
				
				case FlexDiagramConstants.MIND_MAP_CONNECTOR_SMOOTHLY_CURVED:
					drawSmoothlyCurvedConnector(xParent, yParent, xChild, yChild, sideChildIsRight);
					break;
			}
		}
		
		protected function drawStraightLine(x1:Number, y1:Number, x2:Number, y2:Number):void {
			graphics.moveTo(x1, y1);
			graphics.lineTo(x2, y2);
		}

		protected function drawHorizontalConnector(xParent:int, yParent:int, xChild:int, yChild:int, sideChildIsRight:Boolean):void {
			graphics.moveTo(xParent,yParent);
			
			if (sideChildIsRight) {
				graphics.lineTo(xParent + xChild/2, yParent);
				graphics.lineTo(xChild/2, yChild);
			} else {
				graphics.lineTo(xParent/2,yParent);
				graphics.lineTo(xChild + xParent/2,yChild);
			}
			
			graphics.lineTo(xChild, yChild);
		}
		
		protected function drawSmoothlyCurvedConnector(xParent:int, yParent:int, xChild:int, yChild:int, sideChildIsRight:Boolean):void {
			graphics.moveTo(xChild,yChild);
			if( sideChildIsRight ) {
				if (xChild == xParent) {
					graphics.lineTo(xParent, yParent);
				} else if (xChild < xParent) {
					graphics.cubicCurveTo(					
						getLeftTopControlPoint(xChild, yChild).x, 
						getLeftTopControlPoint(xChild, yChild).y,
						getLeftBottomControlPoint(xParent, yParent).x,
						getLeftBottomControlPoint(xParent, yParent).y,
						xParent, yParent);
				} else if (xChild > xParent) {
					graphics.cubicCurveTo(					
						getRightBottomControlPoint(xChild, yChild).x, 
						getRightBottomControlPoint(xChild, yChild).y,
						getLeftBottomControlPoint(xParent, yParent).x,
						getLeftBottomControlPoint(xParent, yParent).y,
						xParent, yParent);
				}
			} else {
				if (xChild == xParent) {
					graphics.lineTo(xParent, yParent);
				} else if (xChild < xParent) {
					graphics.cubicCurveTo(					
						getRightTopControlPoint(xChild, yChild).x, 
						getRightTopControlPoint(xChild, yChild).y,
						getRightBottomControlPoint(xParent, yParent).x,
						getRightBottomControlPoint(xParent, yParent).y,
						xParent, yParent);
				} else if (xChild > xParent) {
					graphics.cubicCurveTo(					
						getLeftBottomControlPoint(xChild, yChild).x, 
						getLeftBottomControlPoint(xChild, yChild).y,
						getLeftBottomControlPoint(xParent, yParent).x,
						getLeftBottomControlPoint(xParent, yParent).y,
						xParent, yParent);
				}
			}
		}
		
		protected function getLeftTopControlPoint(x:Number, y:Number):Point {
			return new Point(x - MindMapDiagramShell(diagramShellContext.diagramShell).horizontalPadding/2, y);
		}
		
		protected function getRightTopControlPoint(x:Number, y:Number):Point {
			return new Point(x + MindMapDiagramShell(diagramShellContext.diagramShell).horizontalPadding/2, y);
		}
		
		protected function getLeftBottomControlPoint(x:Number, y:Number):Point {
			return new Point(x + MindMapDiagramShell(diagramShellContext.diagramShell).horizontalPadding/2, y);
		}
		
		protected function getRightBottomControlPoint(x:Number, y:Number):Point {
			return new Point(x - MindMapDiagramShell(diagramShellContext.diagramShell).horizontalPadding/2, y);
		} 
	}
}