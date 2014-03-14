package org.flowerplatform.flex_client.mindmap.renderer {
	
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.mindmap.renderer.NodeRenderer;
	import org.flowerplatform.flex_client.mindmap.MindMapNodePropertiesConstants;
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class MindMapNodeRenderer extends NodeRenderer {
		
		public static const ICONS_SEPARATOR:String = "|";
		
		override protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			super.nodeUpdatedHandler(event);
			
			var minWidthChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.MIN_WIDTH);
			if (minWidthChanged) {
				minWidth = node.properties[MindMapNodePropertiesConstants.MIN_WIDTH];
			}	
			
			var maxWidthChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.MAX_WIDTH);
			if (maxWidthChanged) {
				maxWidth = node.properties[MindMapNodePropertiesConstants.MAX_WIDTH];
			}
			
			var iconsChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.ICONS);
			if (iconsChanged) {
				if (node.properties[MindMapNodePropertiesConstants.ICONS] != null) {
					icons = new FlowerArrayList(String(node.properties[MindMapNodePropertiesConstants.ICONS]).split(ICONS_SEPARATOR));
				} else {
					icons = null;
				}
			}
			
			if (minWidthChanged || maxWidthChanged) {				
				invalidateSize();
				invalidateDisplayList();
			}			
		}
		
	}
}