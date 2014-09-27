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
	
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.mindmap.AbstractMindMapNodeWithDetailsRenderer;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Sebastian Solomon
	 * @author Mariana Gheorghe
	 * @author Alexandra Topoloaga
	 */ 
	public class MindMapNodeWithDetailsRenderer2 extends AbstractMindMapNodeWithDetailsRenderer {
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			if (node.properties[MindMapConstants.NODE_DETAILS] != null && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0) {
				setDetailsGroupVisibile(true);
				if (detailsText.includeInLayout) {
					detailsIcon.source = Resources.arrowUpIcon;
				} else {
					detailsIcon.source = Resources.arrowDownIcon;
				}
			}  else {
				setDetailsGroupVisibile(false);
			}
			
			if (detailsText.includeInLayout) {
				var text:String = node.properties[MindMapConstants.NODE_DETAILS] as String;
				text = Utils.getCompatibleHTMLText(text);
				detailsText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
			}
			
			if (mindMapDiagramShell != null && diagramShellContext != null) {
				if (event == null || event.property == "x") {
					x = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "x");
				}
				if (event == null || event.property == "y") {
					y = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "y");
				}
				if (event == null || event.property == MindMapConstants.MIN_WIDTH) {
					minWidth = node.getPropertyValue(MindMapConstants.MIN_WIDTH);
					invalidateSize();
					invalidateDisplayList();
				}
				if (event == null || event.property == MindMapConstants.MAX_WIDTH) {
					maxWidth = node.getPropertyValue(MindMapConstants.MAX_WIDTH);
					invalidateSize();
					invalidateDisplayList();
				}
				if (event == null || event.property == "depth") {
					depth = mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "depth");
				}	
				if (event == null || event.property == "hasChildren") {
					invalidateSize();
				}	
				if (event == null || event.property == "expandedWidth") {
					invalidateDisplayList();
				}
				if (event == null || event.property == "note") {
					note = data.getPropertyValue(MindMapConstants.NOTE);
				}
				if (event == null || event.property == "details") {
					details = data.getPropertyValue(MindMapConstants.NODE_DETAILS);
				}
			}
		}
		
		override protected function createEmbeddedRenderer():void {
			embeddedRenderer = new MindMapNodeRenderer2();
			embeddedRenderer.percentWidth = 100;
			embeddedRenderer.addEventListener(FlexEvent.INITIALIZE, nodeRendererInitialized);
		}
		
		protected function get node():Node {
			return Node(data);	
		}
		
		override protected function hasNote():Boolean {
			return node.properties.hasOwnProperty(MindMapConstants.NOTE) && String(node.properties[MindMapConstants.NOTE]).length > 0;
		}
		
		override protected function hasNodeDetails():Boolean {
			return node.properties.hasOwnProperty(MindMapConstants.NODE_DETAILS) && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0;
		}
	}
}