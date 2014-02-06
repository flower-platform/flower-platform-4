package org.flowerplatform.flex_client.core.mindmap.renderer {
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.AbstractMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.core.ContentCache;
	import spark.layouts.HorizontalLayout;
	import spark.primitives.BitmapImage;
		
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
		
		override protected function canDrawCircle(model:Object):Boolean {			
			return model != null && Boolean(model.properties["hasChildren"]).valueOf() && !_diagramShell.getModelController(model).getExpanded(model);
		}
		
	}
}