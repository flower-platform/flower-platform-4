package org.flowerplatform.flex_client.mindmap.renderer {
	
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.mindmap.renderer.NodeRenderer;
	import org.flowerplatform.flex_client.mindmap.MindMapNodePropertiesConstants;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class MindMapNodeRenderer extends NodeRenderer {
		
		public function MindMapNodeRenderer() {
			super();
			
			// don't allow super to clear graphics, I'm doing that
			allowBaseRendererToClearGraphics = false;
		}
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
			switch (event.property) {			
				case "expandedHeight":
				case "expandedWidth":				
					invalidateDisplayList();
			}
		}
				
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {		
			// clear all graphics
			graphics.clear();
			
			if (drawGraphicsOnlyFromBaseClass) {
				super.updateDisplayList(unscaledWidth, unscaledHeight);	
				return;
			}
			
			// start drawing
			
			// draw could shape
			var shape:String = data.properties[MindMapNodePropertiesConstants.CLOUD_SHAPE];
			if (shape != null && shape != MindMapNodePropertiesConstants.NONE) {				
				graphics.lineStyle(2, 0x808080); // gray line with bigger thickness
				graphics.beginFill(Utils.convertValueToColor(data.properties[MindMapNodePropertiesConstants.CLOUD_COLOR]), 1);
				
				var diagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShellContext.diagramShell);
				var cloudPadding:Number = diagramShell.getPropertyValue(diagramShellContext, data, "additionalPadding");
				
				var shapeX:Number = - cloudPadding/2;
				var shapeY:Number = - diagramShell.getDeltaBetweenExpandedHeightAndHeight(diagramShellContext, data, true)/2;
				var shapeWidth:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedWidth") + cloudPadding;
				var shapeHeight:Number = Math.max(diagramShell.getPropertyValue(diagramShellContext, data, "expandedHeight"), diagramShell.getPropertyValue(diagramShellContext, data, "height") + cloudPadding);
				
				if (shape == MindMapNodePropertiesConstants.RECTANGLE) {
					graphics.drawRect(shapeX, shapeY, shapeWidth, shapeHeight);
				} else {
					graphics.drawRoundRect(shapeX, shapeY, shapeWidth, shapeHeight, 20, 20);					
				}
				graphics.endFill();
			}
			
			// end drawing
			
			super.updateDisplayList(unscaledWidth, unscaledHeight);			
		}
		
		override protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			super.nodeUpdatedHandler(event);
			
			var refreshNodePositions:Boolean = false;
			var diagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShellContext.diagramShell);
			var dynamicObject:Object = diagramShell.getDynamicObject(diagramShellContext, data);
			
			var minWidthChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.MIN_WIDTH);
			if (minWidthChanged) {
				minWidth = node.properties[MindMapNodePropertiesConstants.MIN_WIDTH];
			}	
			
			var maxWidthChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.MAX_WIDTH);
			if (maxWidthChanged) {
				maxWidth = node.properties[MindMapNodePropertiesConstants.MAX_WIDTH];
			}
			
			var fontFamilyChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.FONT_FAMILY);
			if (fontFamilyChanged) {
				labelDisplay.setStyle("fontFamily", Utils.getSupportedFontFamily(data.properties[MindMapNodePropertiesConstants.FONT_FAMILY]));				
			}
			
			var fontSizeChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.FONT_SIZE);
			if (fontSizeChanged) {
				labelDisplay.setStyle("fontSize", data.properties[MindMapNodePropertiesConstants.FONT_SIZE]);
			}
			
			var fontBoldChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.FONT_BOLD);
			if (fontBoldChanged) {				
				labelDisplay.setStyle("fontWeight", data.properties[MindMapNodePropertiesConstants.FONT_BOLD] == true ? "bold" : "normal");
			}
			
			var fontItalicChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.FONT_ITALIC);
			if (fontItalicChanged) {
				labelDisplay.setStyle("fontStyle", data.properties[MindMapNodePropertiesConstants.FONT_ITALIC] == true ? "italic" : "normal");
			}
			
			var colorChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.COLOR_TEXT);
			if (colorChanged) {
				labelDisplay.setStyle("color", Utils.convertValueToColor(data.properties[MindMapNodePropertiesConstants.COLOR_TEXT]));
			}
			
			var backgroundColorChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.COLOR_BACKGROUND);
			if (backgroundColorChanged) {				
				backgroundColor = Utils.convertValueToColor(data.properties[MindMapNodePropertiesConstants.COLOR_BACKGROUND]);
			}
				
			var cloudColorChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.CLOUD_COLOR);
			
			var cloudShapeChanged:Boolean = hasPropertyChanged(MindMapNodePropertiesConstants.CLOUD_SHAPE);						
			if (cloudShapeChanged) {				
				var shape:String = data.properties[MindMapNodePropertiesConstants.CLOUD_SHAPE];
				
				if (shape != null && shape != MindMapNodePropertiesConstants.NONE) { // we have a cloud shape, add additional padding to node and request refresh				
					diagramShell.setPropertyValue(diagramShellContext, data, "additionalPadding", diagramShell.additionalPadding);	
					refreshNodePositions = true;				
				} else if (dynamicObject.additionalPadding) { // no cloud shape needed anymore, remove additional padding from node and request refresh
					delete dynamicObject.additionalPadding;		
					refreshNodePositions = true;		
				}				
			}
			
			if (refreshNodePositions) {
				diagramShell.refreshModelPositions(diagramShellContext, data);
			}
			
			if (minWidthChanged || maxWidthChanged || backgroundColorChanged || cloudColorChanged) {
				invalidateSize();
				invalidateDisplayList();
			}			
		}
		
	}
}
