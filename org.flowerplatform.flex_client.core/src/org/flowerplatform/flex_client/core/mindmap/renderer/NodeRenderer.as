package org.flowerplatform.flex_client.core.mindmap.renderer {
	
	import flashx.textLayout.conversion.TextConverter;
	import flashx.textLayout.elements.TextFlow;
	
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flex_client.core.NodePropertiesConstants;
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.mindmap.AbstractMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.utils.TextFlowUtil;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRenderer extends AbstractMindMapModelRenderer {
				
		protected function get node():Node {
			return Node(data);	
		}
		
		protected function get mindmapDiagramShell():MindMapDiagramShell {
			return MindMapDiagramShell(diagramShellContext.diagramShell);	
		}
		
		protected function hasPropertyChanged(property:String, event:NodeUpdatedEvent = null):Boolean {
			return (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(property) != -1 : node.properties.hasOwnProperty(property);
		}
		
		override protected function assignData():void {
			x = mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "x");	
			y = mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "y");		
			
			nodeUpdatedHandler();
		}
			
		override protected function unassignData():void {
			labelDisplay.text = "";
			
			// Important: measuredHeight/measuredWidth are reset to their default values; otherwise the renderer will use recycled values for width/height 
			measuredWidth = 0;
			measuredHeight = 0;
		}
		
		override protected function resizeHandler(event:ResizeEvent):void {
			var refresh:Boolean = false;
			if (mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "width") != width) {
				mindmapDiagramShell.setPropertyValue(diagramShellContext, data, "width", width);
				refresh = true;
			}
			if (mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "height") != height) {			
				mindmapDiagramShell.setPropertyValue(diagramShellContext, data, "height", height);
				refresh = true;
			}
			
			if (refresh) {				
				var parent:Object = ControllerUtils.getModelChildrenController(diagramShellContext, data).getParent(diagramShellContext, data);
				mindmapDiagramShell.refreshModelPositions(diagramShellContext, parent != null ? parent : data);
			}
		}						
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			switch (event.property) {
				case "x":
					x = mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "x");					
					break;
				case "y":
					y = mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "y");				
					break;
				case "hasChildren":
					invalidateSize();
				case "children":
					invalidateDisplayList();
			}
		}
		
		override public function set data(value:Object):void {
			if (data != null) {		
				data.removeEventListener(NodeUpdatedEvent.NODE_UPDATED, nodeUpdatedHandler);				
			}
			
			super.data = value;
			
			if (data != null) {
				data.addEventListener(NodeUpdatedEvent.NODE_UPDATED, nodeUpdatedHandler);				
			}
		}
		
		protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			var textChanged:Boolean = hasPropertyChanged(NodePropertiesConstants.TEXT);
			if (textChanged) {
				var text:String = data.properties[NodePropertiesConstants.TEXT] as String;
				text = Utils.getCompatibleHTMLText(text);
				// if text contains html tag, display it as html, otherwise plain text
				labelDisplay.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT); 
				invalidateSize();
				invalidateDisplayList();
			}
		}
		
		override protected function canDrawCircle():Boolean {			
			return node != null 
				&& hasPropertyChanged(NodePropertiesConstants.HAS_CHILDREN)
				&& Boolean(node.properties[NodePropertiesConstants.HAS_CHILDREN]).valueOf() 
				&& !mindmapDiagramShell.getModelController(diagramShellContext, node).getExpanded(diagramShellContext, node);
		}
		
	}
}
