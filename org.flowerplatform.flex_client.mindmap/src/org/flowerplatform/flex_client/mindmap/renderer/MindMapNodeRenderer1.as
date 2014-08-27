/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation version 3.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
* 
* license-end
*/
package org.flowerplatform.flex_client.mindmap.renderer {
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	
	import spark.primitives.BitmapImage;
	
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.mindmap.AbstractMindMapNodeRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.Utils;

	/**
	 * @author Alexandra Topoloaga
	 */ 
	public class MindMapNodeRenderer1 extends AbstractMindMapNodeRenderer {
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			
			var mindMapDiagramShell:MindMapDiagramShell = this.mindMapDiagramShell;
			
			if (mindMapDiagramShell != null && diagramShellContext != null) {
				// i.e. used as a renderer in a mind map diagram
				if (event == null || event.property == "x") {
					x = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "x");	
				}
				if (event == null || event.property == "y") {
					y = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "y");	
				}
				if (event == null || event.property == "depth") {
					depth = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "depth");	
				}
				if (event == null || event.property == "cloudColor") {
					cloudColor =  Utils.convertValueToColor(data.getPropertyValue(MindMapConstants.CLOUD_COLOR));
				}			
				if (event == null || event.property == "cloudType") {
					cloudType = data.getPropertyValue(MindMapConstants.CLOUD_SHAPE);
				}
				if (event == null || event.property == "expandedWidth") {
					invalidateDisplayList();
				}
			}
			
			// else used as a renderer in a plain Flex component
			
			if (event == null || event.property == "fontFamily") {
				fontFamily = Utils.getSupportedFontFamily(data.getPropertyValue(MindMapConstants.FONT_FAMILY));
			} 
			if (event == null || event.property == "fontSize") {
				fontSize = 12; //data.getPropertyValue(MindMapConstants.FONT_SIZE);
			} 
			if (event == null || event.property == "fontWeight") {
				fontWeight = data.getPropertyValue(MindMapConstants.FONT_BOLD) == true ? "bold" : "normal";
			} 
			if (event == null || event.property == "fontStyle") {
				fontStyle = data.getPropertyValue(MindMapConstants.FONT_ITALIC) == true ? "italic" : "normal";
			}
			if (event == null || event.property == "node.TEXT") {
				text = data.getPropertyValue("node.TEXT");
			}
			if (event == null || event.property == "textColor") {
				textColor =  Utils.convertValueToColor(data.getPropertyValue(MindMapConstants.COLOR_TEXT));
			}
			if (event == null || event.property == "background") {
				invalidateDisplayList();
				background =  0xFFFFFFFF; //Utils.convertValueToColor(data.getPropertyValue(MindMapConstants.COLOR_BACKGROUND));
			}
			if (event == null || event.property == "icons") {
//				var iconDisplay:BitmapImage = new BitmapImage();
//				iconDisplay.contentLoader = FlexUtilGlobals.getInstance().imageContentCache;
//				iconDisplay.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying("../../org.flowerplatform.flexdiagram.samples/icons/penguin.png");
//				iconDisplay.verticalAlign = "middle";
//				iconDisplay.depth = UIComponent(this).depth;
//				var icons:ArrayCollection = new ArrayCollection();
//				icons.addItem(iconDisplay);
			}
		}
		
	}
}