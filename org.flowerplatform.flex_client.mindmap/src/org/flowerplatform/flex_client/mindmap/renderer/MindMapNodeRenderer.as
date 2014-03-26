package org.flowerplatform.flex_client.mindmap.renderer {
	
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
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
		
		override protected function unassignData():void {
			super.unassignData();
			
			minWidth = 0;
			maxWidth = DEFAULT_MAX_WIDTH;
			backgroundColor = BACKGROUND_COLOR_DEFAULT;
			
			if (labelDisplay) {
				labelDisplay.setStyle("fontFamily", undefined);	
				labelDisplay.setStyle("fontSize", undefined);
				labelDisplay.setStyle("fontWeight", undefined);
				labelDisplay.setStyle("fontStyle", undefined);
				labelDisplay.setStyle("color", undefined);
			}			
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
			if (shape != null && shape != MindMapNodePropertiesConstants.SHAPE_NONE) {				
				graphics.lineStyle(2, 0x808080); // gray line with bigger thickness
				graphics.beginFill(Utils.convertValueToColor(data.properties[MindMapNodePropertiesConstants.CLOUD_COLOR]), 1);
				
				var diagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShellContext.diagramShell);
				var cloudPadding:Number = diagramShell.getPropertyValue(diagramShellContext, data, "additionalPadding");
				var side:int = diagramShell.getModelController(diagramShellContext, data).getSide(diagramShellContext, data);
				
				var width:Number = diagramShell.getPropertyValue(diagramShellContext, data, "width");
				var height:Number = diagramShell.getPropertyValue(diagramShellContext, data, "height");
				var expandedWidth:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedWidth");
				var expandedHeight:Number = diagramShell.getPropertyValue(diagramShellContext, data, "expandedHeight");
								
				var shapeX:Number = - cloudPadding/2;
				var shapeY:Number = - diagramShell.getDeltaBetweenExpandedHeightAndHeight(diagramShellContext, data, true)/2;
				var shapeWidth:Number = expandedWidth + cloudPadding;
				var shapeHeight:Number = Math.max(expandedHeight, height + cloudPadding);
				
				if (side == MindMapDiagramShell.POSITION_LEFT) {
					shapeX -= (expandedWidth - width);
				}
				if (shape == MindMapNodePropertiesConstants.SHAPE_RECTANGLE) {
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
			
			var minWidthChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.MIN_WIDTH, event);
			if (minWidthChanged) {
				minWidth = node.properties[MindMapNodePropertiesConstants.MIN_WIDTH];
			}	
			
			var maxWidthChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.MAX_WIDTH, event);
			if (maxWidthChanged) {
				maxWidth = node.properties[MindMapNodePropertiesConstants.MAX_WIDTH];
			}
			
			var fontFamilyChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.FONT_FAMILY, event);
			if (fontFamilyChanged) {
				labelDisplay.setStyle("fontFamily", Utils.getSupportedFontFamily(data.properties[MindMapNodePropertiesConstants.FONT_FAMILY]));				
			}
			
			var fontSizeChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.FONT_SIZE, event);
			if (fontSizeChanged) {
				labelDisplay.setStyle("fontSize", data.properties[MindMapNodePropertiesConstants.FONT_SIZE]);
			}
			
			var fontBoldChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.FONT_BOLD, event);
			if (fontBoldChanged) {				
				labelDisplay.setStyle("fontWeight", data.properties[MindMapNodePropertiesConstants.FONT_BOLD] == true ? "bold" : "normal");
			}
			
			var fontItalicChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.FONT_ITALIC, event);
			if (fontItalicChanged) {
				labelDisplay.setStyle("fontStyle", data.properties[MindMapNodePropertiesConstants.FONT_ITALIC] == true ? "italic" : "normal");
			}
			
			var colorChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.COLOR_TEXT, event);
			if (colorChanged) {
				labelDisplay.setStyle("color", Utils.convertValueToColor(data.properties[MindMapNodePropertiesConstants.COLOR_TEXT]));
			}
			
			var backgroundColorChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.COLOR_BACKGROUND, event);
			if (backgroundColorChanged) {				
				backgroundColor = Utils.convertValueToColor(data.properties[MindMapNodePropertiesConstants.COLOR_BACKGROUND]);
			}
				
			var cloudColorChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.CLOUD_COLOR, event);
			
			var cloudShapeChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapNodePropertiesConstants.CLOUD_SHAPE, event);						
			if (cloudShapeChanged) {				
				var shape:String = data.properties[MindMapNodePropertiesConstants.CLOUD_SHAPE];
				
				if (shape != null && shape != MindMapNodePropertiesConstants.SHAPE_NONE) { // we have a cloud shape, add additional padding to node and request refresh				
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
