package org.flowerplatform.flex_client.mindmap.renderer {
	
	import org.flowerplatform.flex_client.core.mindmap.renderer.NodeRenderer;
	import org.flowerplatform.flex_client.core.mindmap.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.mindmap.MindMapNodePropertiesConstants;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class MindMapNodeRenderer extends NodeRenderer {
		
		public static const ICONS_SEPARATOR:String = "|";
		
		override protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			super.nodeUpdatedHandler(event);
			
			var minWidthChanged:Boolean = (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(MindMapNodePropertiesConstants.MIN_WIDTH) != -1 : true;
			if (minWidthChanged) {
				minWidth = data.properties[MindMapNodePropertiesConstants.MIN_WIDTH];
			}	
			
			var maxWidthChanged:Boolean = (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(MindMapNodePropertiesConstants.MAX_WIDTH) != -1 : true;
			if (maxWidthChanged) {
				maxWidth = data.properties[MindMapNodePropertiesConstants.MAX_WIDTH];
			}
			
			var iconsChanged:Boolean = (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(MindMapNodePropertiesConstants.ICONS) != -1 : true;
			if (iconsChanged) {
				if (data.properties[MindMapNodePropertiesConstants.ICONS] != null) {
					icons = new FlowerArrayList(String(data.properties[MindMapNodePropertiesConstants.ICONS]).split(ICONS_SEPARATOR));
				} else {
					icons = null;
				}
			}
			
			var fontFamilyChanged:Boolean = (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(MindMapNodePropertiesConstants.FONT_FAMILY) != -1 : true;
			if (fontFamilyChanged) {
				labelDisplay.setStyle("fontFamily", Utils.getSupportedFontFamily(data.properties[MindMapNodePropertiesConstants.FONT_FAMILY]));				
			}
			
			var fontSizeChanged:Boolean = (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(MindMapNodePropertiesConstants.FONT_SIZE) != -1 : true;
			if (fontSizeChanged) {
				labelDisplay.setStyle("fontSize",  data.properties[MindMapNodePropertiesConstants.FONT_SIZE]);
			}
			
			var fontBoldChanged:Boolean = (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(MindMapNodePropertiesConstants.FONT_BOLD) != -1 : true;
			if (fontBoldChanged) {				
				labelDisplay.setStyle("fontWeight",  data.properties[MindMapNodePropertiesConstants.FONT_BOLD] == true ? "bold" : "normal");
			}
			
			var fontItalicChanged:Boolean = (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(MindMapNodePropertiesConstants.FONT_ITALIC) != -1 : true;
			if (fontItalicChanged) {
				labelDisplay.setStyle("fontStyle",  data.properties[MindMapNodePropertiesConstants.FONT_ITALIC] == true ? "italic" : "normal");
			}
			
			if (minWidthChanged || maxWidthChanged) {
				invalidateSize();
				invalidateDisplayList();
			}
		}
	}
}