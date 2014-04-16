package org.flowerplatform.flex_client.mindmap.renderer {
	
	import flash.events.MouseEvent;
	
	import mx.events.PropertyChangeEvent;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.mindmap.MindMapPlugin;
	import org.flowerplatform.flex_client.mindmap.ui.NoteComponentExtension;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class MindMapNodeRenderer extends NodeRenderer {
		
		protected var noteComponentExtension:NoteComponentExtension = new NoteComponentExtension;
		
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
		
		override protected function drawGraphics(unscaledWidth:Number, unscaledHeight:Number):void {			
			// draw could shape
			var shape:String = data.properties[MindMapConstants.CLOUD_SHAPE];
			if (shape != null && shape != MindMapConstants.SHAPE_NONE) {				
				graphics.lineStyle(2, 0x808080); // gray line with bigger thickness
				graphics.beginFill(Utils.convertValueToColor(data.properties[MindMapConstants.CLOUD_COLOR]), 1);
				
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
				if (shape == MindMapConstants.SHAPE_RECTANGLE) {
					graphics.drawRect(shapeX, shapeY, shapeWidth, shapeHeight);
				} else {
					graphics.drawRoundRect(shapeX, shapeY, shapeWidth, shapeHeight, 20, 20);					
				}
				graphics.endFill();
			}
			super.drawGraphics(unscaledWidth, unscaledHeight);
		}
						
		override protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			super.nodeUpdatedHandler(event);
			
			var iconsProvider:GenericValueProviderFromDescriptor =  NodeControllerUtils.getIconsProvider(diagramShellContext.diagramShell.registry, node);
			var icon:String = iconsProvider.getValue(node) as String;
			
			if (node.properties.note != null && String(node.properties.note).length > 0) {
				var noteIcon:String = Resources.getResourceUrl("/images/mindmap/knotes.png");   
				icon = noteIcon + (icon == null ? "" : (Utils.ICONS_SEPARATOR + icon));
				icons = new FlowerArrayList(icon.split(Utils.ICONS_SEPARATOR));
			} 
			
			var refreshNodePositions:Boolean = false;
			var diagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShellContext.diagramShell);
			var dynamicObject:Object = diagramShell.getDynamicObject(diagramShellContext, data);
			
			var minWidthChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.MIN_WIDTH, event);
			if (minWidthChanged) {
				minWidth = node.properties[MindMapConstants.MIN_WIDTH];
			}	
			
			var maxWidthChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.MAX_WIDTH, event);
			if (maxWidthChanged) {
				maxWidth = node.properties[MindMapConstants.MAX_WIDTH];
			}
			
			var fontFamilyChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.FONT_FAMILY, event);
			if (fontFamilyChanged) {
				labelDisplay.setStyle("fontFamily", Utils.getSupportedFontFamily(data.properties[MindMapConstants.FONT_FAMILY]));				
			}
			
			var fontSizeChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.FONT_SIZE, event);
			if (fontSizeChanged) {
				labelDisplay.setStyle("fontSize", MindMapConstants.FONT_SCALE_FACTOR * data.properties[MindMapConstants.FONT_SIZE]);
			}
			
			var fontBoldChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.FONT_BOLD, event);
			if (fontBoldChanged) {				
				labelDisplay.setStyle("fontWeight", data.properties[MindMapConstants.FONT_BOLD] == true ? "bold" : "normal");
			}
			
			var fontItalicChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.FONT_ITALIC, event);
			if (fontItalicChanged) {
				labelDisplay.setStyle("fontStyle", data.properties[MindMapConstants.FONT_ITALIC] == true ? "italic" : "normal");
			}
			
			var colorChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.COLOR_TEXT, event);
			if (colorChanged) {
				labelDisplay.setStyle("color", Utils.convertValueToColor(data.properties[MindMapConstants.COLOR_TEXT]));
			}
			
			var backgroundColorChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.COLOR_BACKGROUND, event);
			if (backgroundColorChanged) {				
				backgroundColor = Utils.convertValueToColor(data.properties[MindMapConstants.COLOR_BACKGROUND]);
			}
				
			var cloudColorChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.CLOUD_COLOR, event);
			
			var cloudShapeChanged:Boolean = NodeControllerUtils.hasPropertyChanged(node, MindMapConstants.CLOUD_SHAPE, event);						
			if (cloudShapeChanged) {				
				var shape:String = data.properties[MindMapConstants.CLOUD_SHAPE];
				
				if (shape != null && shape != MindMapConstants.SHAPE_NONE) { // we have a cloud shape, add additional padding to node and request refresh				
					diagramShell.setPropertyValue(diagramShellContext, data, "additionalPadding", diagramShell.additionalPadding);	
					refreshNodePositions = true;				
				} else if (dynamicObject.additionalPadding) { // no cloud shape needed anymore, remove additional padding from node and request refresh
					delete dynamicObject.additionalPadding;		
					refreshNodePositions = true;		
				}				
			}
			
			if (refreshNodePositions) {
				var parent:Object = ControllerUtils.getModelChildrenController(diagramShellContext, data).getParent(diagramShellContext, data);
				mindMapDiagramShell.refreshModelPositions(diagramShellContext, parent != null ? parent : data);
			}
			
			if (minWidthChanged || maxWidthChanged) {
				invalidateSize();				
			}	
			if (minWidthChanged || maxWidthChanged || backgroundColorChanged || cloudColorChanged) {			
				invalidateDisplayList();
			}					
		}
		
		override protected function mouseOverHandler(event:MouseEvent):void {
			super.mouseOverHandler(event);
			if (node.properties.hasOwnProperty(MindMapConstants.NOTE) && String(node.properties.note).length > 0 && noteComponentExtension.parent == null) {
				var text:String = String(node.properties.note);
				text = Utils.getCompatibleHTMLText(text);
				// if text contains html tag, display it as html, otherwise plain text
				noteComponentExtension.noteText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
				DiagramRenderer(mindMapDiagramShell.diagramRenderer).addElement(noteComponentExtension)
					
				if (noteComponentExtension.parent != null) {
					var dynamicObject:Object = mindMapDiagramShell.getDynamicObject(diagramShellContext, node);
					noteComponentExtension.x = dynamicObject.x ;
					noteComponentExtension.y = dynamicObject.y + dynamicObject.height;
				}
			}
		}
		
		override protected function mouseOutHandler(event:MouseEvent):void {	
			super.mouseOutHandler(event);
			if (noteComponentExtension.parent != null) {
				DiagramRenderer(diagramShellContext.diagramShell.diagramRenderer).removeElement(noteComponentExtension);
			}				
		}
		
	}
}
