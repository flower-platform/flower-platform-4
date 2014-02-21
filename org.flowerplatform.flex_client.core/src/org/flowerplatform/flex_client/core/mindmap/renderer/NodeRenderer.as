package org.flowerplatform.flex_client.core.mindmap.renderer {
	
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flex_client.core.mindmap.event.NodeRenderer_HasChildrenChangedEvent;
	import org.flowerplatform.flex_client.core.mindmap.event.NodeUpdatedEvent;
	import org.flowerplatform.flexdiagram.mindmap.AbstractMindMapModelRenderer;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRenderer extends AbstractMindMapModelRenderer {
				
		override protected function assignData():void {
			x = _diagramShell.getPropertyValue(data, "x");	
			y = _diagramShell.getPropertyValue(data, "y");		
			
			labelDisplay.text = data.properties["body"];
		}
				
		override protected function resizeHandler(event:ResizeEvent):void {
			var refresh:Boolean = false;
			if (_diagramShell.getPropertyValue(data, "width") != width) {
				_diagramShell.setPropertyValue(data, "width", width);
				refresh = true;
			}
			if (_diagramShell.getPropertyValue(data, "height") != height) {			
				_diagramShell.setPropertyValue(data, "height", height);
				refresh = true;
			}
			
			if (refresh) {				
				var parent:Object = _diagramShell.getControllerProvider(data).getModelChildrenController(data).getParent(data);
				_diagramShell.refreshModelPositions(parent != null ? parent : data);
			}
		}						
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			switch (event.property) {
				case "x":
					x = _diagramShell.getPropertyValue(data, "x");					
					break;
				case "y":
					y = _diagramShell.getPropertyValue(data, "y");				
					break;
				case "body":
					labelDisplay.text = data.properties["body"];
				case "hasChildren":
					invalidateSize();
				case "children":
					invalidateDisplayList();
			}
		}
		
		override public function set data(value:Object):void {
			if (super.data != null) {				
				data.removeEventListener(NodeUpdatedEvent.NODE_UPDATED, nodeUpdatedHandler);				
			}
			
			super.data = value;
			
			if (data != null) {
				data.addEventListener(NodeUpdatedEvent.NODE_UPDATED, nodeUpdatedHandler);				
			}
		}
		
		protected function nodeUpdatedHandler(event:NodeUpdatedEvent):void {
			if (data.properties.hasOwnProperty("body")) {
				labelDisplay.text = data.properties["body"];
			}
			if (data.properties.hasOwnProperty("hasChildren")) {
				dispatchEvent(new NodeRenderer_HasChildrenChangedEvent());
			}			
			invalidateSize();
			invalidateDisplayList();
		}
		
		override protected function canDrawCircle(model:Object):Boolean {			
			return model != null && Boolean(model.properties["hasChildren"]).valueOf() && !_diagramShell.getModelController(model).getExpanded(model);
		}
		
	}
}