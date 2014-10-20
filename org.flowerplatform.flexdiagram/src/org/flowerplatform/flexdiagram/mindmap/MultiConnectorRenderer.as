package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.IList;
	import mx.core.IDataRenderer;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
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
				_data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler1);
			}
			_data = MultiConnectorModel(value);			
			if (_data != null) {
				_data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler1);
				modelChangedHandler1(null);
			}
		}
		
		// TODO check if event is childConnectorProperties -> invalidateDisplayList: draw
		protected function modelChangedHandler1(event:PropertyChangeEvent):void {
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
			
			// TODO check if event is childConnectorProperties
			// if (event == null || MIND_MAP_RENDERER_CHILD_CONNECTOR_PROPERTIRS == event.property) -> invalidateDisplayList: draw
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
				drawStraightLine(x1, y1, x2, y2);
			}
		}
		
		protected function drawStraightLine(x1:Number, y1:Number, x2:Number, y2:Number):void {
			graphics.moveTo(x1, y1);
			graphics.lineTo(x2, y2);
		}
		
	}
}