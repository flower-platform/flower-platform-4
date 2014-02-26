package org.flowerplatform.flex_client.core.mindmap.renderer {
	
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flex_client.core.NodePropertiesConstants;
	import org.flowerplatform.flex_client.core.mindmap.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.mindmap.AbstractMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRenderer extends AbstractMindMapModelRenderer {
				
		override protected function assignData():void {
			x = MindMapDiagramShell(diagramShellContext.diagramShell).getPropertyValue(diagramShellContext, data, "x");	
			y = MindMapDiagramShell(diagramShellContext.diagramShell).getPropertyValue(diagramShellContext, data, "y");		
			
			nodeUpdatedHandler();
		}
				
		override protected function resizeHandler(event:ResizeEvent):void {
			var refresh:Boolean = false;
			if (MindMapDiagramShell(diagramShellContext.diagramShell).getPropertyValue(diagramShellContext, data, "width") != width) {
				MindMapDiagramShell(diagramShellContext.diagramShell).setPropertyValue(diagramShellContext, data, "width", width);
				refresh = true;
			}
			if (MindMapDiagramShell(diagramShellContext.diagramShell).getPropertyValue(diagramShellContext, data, "height") != height) {			
				MindMapDiagramShell(diagramShellContext.diagramShell).setPropertyValue(diagramShellContext, data, "height", height);
				refresh = true;
			}
			
			if (refresh) {				
				var parent:Object = ControllerUtils.getModelChildrenController(diagramShellContext, data).getParent(diagramShellContext, data);
				MindMapDiagramShell(diagramShellContext.diagramShell).refreshModelPositions(diagramShellContext, parent != null ? parent : data);
			}
		}						
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			switch (event.property) {
				case "x":
					x = MindMapDiagramShell(diagramShellContext.diagramShell).getPropertyValue(diagramShellContext, data, "x");					
					break;
				case "y":
					y = MindMapDiagramShell(diagramShellContext.diagramShell).getPropertyValue(diagramShellContext, data, "y");				
					break;
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
		
		protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			var textChanged:Boolean = (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(NodePropertiesConstants.TEXT) != -1 : true;
			if (textChanged) {
				labelDisplay.text = data.properties[NodePropertiesConstants.TEXT];
				invalidateSize();
				invalidateDisplayList();
			}
		}
		
		override protected function canDrawCircle(model:Object):Boolean {			
			return model != null && Boolean(model.properties[NodePropertiesConstants.HAS_CHILDREN]).valueOf() && !MindMapDiagramShell(diagramShellContext.diagramShell).getModelController(diagramShellContext, model).getExpanded(diagramShellContext, model);
		}
		
	}
}
