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
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Alexandra Topoloaga
	 */ 
	public class MindMapNodeRenderer2 extends MindMapRenderer {
		
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
		
			if (event == null || event.property == MindMapConstants.FONT_FAMILY) {
				fontFamily = data.getPropertyValue(MindMapConstants.FONT_FAMILY);
			} 
			if (event == null || event.property == MindMapConstants.FONT_SIZE) {
				fontSize = data.getPropertyValue(MindMapConstants.FONT_SIZE);
			} 
			if (event == null || event.property == MindMapConstants.FONT_BOLD) {
				fontBold = data.getPropertyValue(MindMapConstants.FONT_BOLD);
			} 
			if (event == null || event.property == MindMapConstants.FONT_ITALIC) {
				fontItalic = data.getPropertyValue(MindMapConstants.FONT_ITALIC);
			}
			
			var titleProvider:GenericValueProviderFromDescriptor = NodeControllerUtils.getTitleProvider(mindMapDiagramShell.registry, node);
			var titleProperty:String = titleProvider.getPropertyNameFromGenericDescriptor(node);
			if (event == null || event.property == titleProperty) {
				var title:String = String(titleProvider.getValue(node));
				text = Utils.getCompatibleHTMLText(title);
			}
			
			if (event == null || event.property == MindMapConstants.COLOR_TEXT) {
				textColor = Utils.convertValueToColor(data.getPropertyValue(MindMapConstants.COLOR_TEXT));
			}
			
			if (event == null || event.property == MindMapConstants.COLOR_BACKGROUND) {
				backgroundColor = Utils.convertValueToColor(data.getPropertyValue(MindMapConstants.COLOR_BACKGROUND));
				invalidateDisplayList();
			}
			
			var iconsProvider:GenericValueProviderFromDescriptor =  NodeControllerUtils.getIconsProvider(mindMapDiagramShell.registry, node);
			var iconsProperty:String = iconsProvider.getPropertyNameFromGenericDescriptor(node);
			if (event == null || event.property == iconsProperty) {
				var iconsValue:String = iconsProvider.getValue(node) as String;
				if (node.properties.note != null && String(node.properties.note).length > 0) {				 
					iconsValue = Resources.getResourceUrl("/images/mindmap/knotes.png") + (iconsValue == null ? "" : (Utils.ICONS_SEPARATOR + iconsValue));
					icons = new FlowerArrayList(iconsValue.split(Utils.ICONS_SEPARATOR));
				} else {
					if (iconsValue != null) {
						icons = new FlowerArrayList(iconsValue.split(Utils.ICONS_SEPARATOR));
					} else {
						icons = null;
					}	
				}
			}
		}
		
		protected function get node():Node {
 			return Node(data);	
		}
		
		override protected function beginModelListen():void {
			super.beginModelListen();
			node.properties.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);	
		}
		
		override protected function endModelListen():void {
			super.endModelListen();
			node.properties.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}
		
		override protected function shouldDrawCircle():Boolean {			
			return node != null 
				&& node.properties.hasOwnProperty(CoreConstants.HAS_CHILDREN)
				&& Boolean(node.properties[CoreConstants.HAS_CHILDREN]).valueOf() 
				&& !mindMapDiagramShell.getModelController(diagramShellContext, node).getExpanded(diagramShellContext, node);
		}
	}
}