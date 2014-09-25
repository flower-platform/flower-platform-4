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
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.mindmap.MindMapNodeRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.Utils;

	/**
	 * @author Alexandra Topoloaga
	 */ 
	public class MindMapNodeRenderer1 extends MindMapNodeRenderer {
		
		public static const DEFAULT_PATH:String = "/images/mindmap/icons/";
		
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
				if (event == null || event.property == "cloud.COLOR") {
					cloudColor =  Utils.convertValueToColor(data.getPropertyValue("cloud.COLOR")); 
				}			
				if (event == null || event.property == "cloud.SHAPE") {
					cloudType = data.getPropertyValue("cloud.SHAPE"); 
				}
				if (event == null || event.property == "expandedWidth") {
					invalidateDisplayList();
				}
			}
			
			// else used as a renderer in a plain Flex component
			
			if (event == null || event.property == "font.NAME") {
				fontFamily = data.getPropertyValue("font.NAME");
			} 
			if (event == null || event.property == "font.SIZE") {
				fontSize = data.getPropertyValue("font.SIZE"); 
			} 
			if (event == null || event.property == "font.BOLD") {
				fontBold = data.getPropertyValue("font.BOLD");
			} 
			if (event == null || event.property == "font.ITALIC") {
				fontItalic = data.getPropertyValue("font.ITALIC");
			}
			if (event == null || event.property == "TEXT") {
				text = data.getPropertyValue("TEXT"); 
			}
			if (event == null || event.property == "COLOR") {
				textColor =  Utils.convertValueToColor(data.getPropertyValue("COLOR")); 
			}
			if (event == null || event.property == "BACKGROUND_COLOR") {
				invalidateDisplayList();
				backgroundColor = Utils.convertValueToColor(data.getPropertyValue("BACKGROUND_COLOR"));
			}
			if (event == null || event.property == "icons") {
				var iconList:ArrayCollection = data.getPropertyValue("icons");
				var list: FlowerArrayList = new FlowerArrayList();
				var iconsValue:String;
				if (iconList != null) {
					for (var i:int = 0; i < iconList.length; i++) {
						iconsValue = (iconsValue == null ? "" : (iconsValue + Utils.ICONS_SEPARATOR)) + Resources.getResourceUrl(DEFAULT_PATH + iconList.getItemAt(i) +".png");
					}
				}
				if (node.getPropertyValue("richcontent(TYPE=NOTE)_content") != null && String(node.getPropertyValue("richcontent(TYPE=NOTE)_content")).length > 0) {		
					iconsValue = Resources.getResourceUrl("/images/mindmap/knotes.png") + (iconsValue == null ? "" : (Utils.ICONS_SEPARATOR + iconsValue));
					icons = new FlowerArrayList(iconsValue.split(Utils.ICONS_SEPARATOR));
				} else if (iconsValue != null) {
					icons =  new FlowerArrayList(iconsValue.split(Utils.ICONS_SEPARATOR));	
				} else {
					icons = null;
				}
			}
		}
		
		protected function get node():Node {
			return Node(data);	
		}
		
		override protected function shouldDrawCircle():Boolean {			
			return node != null 
				&& node.properties.hasOwnProperty(CoreConstants.HAS_CHILDREN)
				&& Boolean(node.properties[CoreConstants.HAS_CHILDREN]).valueOf() 
				&& !mindMapDiagramShell.getModelController(diagramShellContext, node).getExpanded(diagramShellContext, node);
		}
		
	}
}