package org.flowerplatform.flex_client.mindmap.renderer {
	
	import flashx.textLayout.conversion.TextConverter;
	
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flex_client.core.NodePropertiesConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.node.controller.GenericDescriptorValueProvider;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.mindmap.AbstractMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.Utils;
		
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
				
		override protected function assignData():void {
			x = mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "x");	
			y = mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "y");		
			
			nodeUpdatedHandler();
		}
			
		override protected function unassignData():void {
			labelDisplay.text = "";
			icons = null;
			
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
				case "depth":
					depth = mindmapDiagramShell.getPropertyValue(diagramShellContext, data, "depth");				
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
			var titleProvider:GenericDescriptorValueProvider = NodeControllerUtils.getTitleProvider(diagramShellContext.diagramShell.registry, node);
			var titleProperty:String = titleProvider.getPropertyNameFromGenericDescriptor(node);
			var titleChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, titleProperty, event);
			if (titleChanged) {
				var title:String = String(titleProvider.getValue(node));
				title = Utils.getCompatibleHTMLText(title);
				// if text contains html tag, display it as html, otherwise plain text
				labelDisplay.textFlow = TextConverter.importToFlow(title , Utils.isHTMLText(title) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT); 
				invalidateSize();
				invalidateDisplayList();
			}
			
			var iconsProvider:GenericDescriptorValueProvider =  NodeControllerUtils.getIconsProvider(diagramShellContext.diagramShell.registry, node);
			var iconsProperty:String = iconsProvider.getPropertyNameFromGenericDescriptor(node);
			var iconsChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, iconsProperty, event);
			if (iconsChanged) {
				var iconsValue:String = String(iconsProvider.getValue(node));
				if (iconsValue != null) {
					icons = new FlowerArrayList(iconsValue.split(Utils.ICONS_SEPARATOR));
				} else {
					icons = null;
				}
			}
		}
		
		override protected function canDrawCircle():Boolean {			
			return node != null 
				&& NodeControllerUtils.hasPropertyChanged(node, NodePropertiesConstants.HAS_CHILDREN)
				&& Boolean(node.properties[NodePropertiesConstants.HAS_CHILDREN]).valueOf() 
				&& !mindmapDiagramShell.getModelController(diagramShellContext, node).getExpanded(diagramShellContext, node);
		}
		
	}
}
