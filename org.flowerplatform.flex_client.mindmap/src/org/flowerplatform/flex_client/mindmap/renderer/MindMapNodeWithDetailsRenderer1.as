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
	
	import flash.events.MouseEvent;
	
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import spark.components.DataRenderer;
	import spark.components.Group;
	import spark.components.Image;
	import spark.components.RichText;
	import spark.layouts.HorizontalLayout;
	import spark.layouts.VerticalLayout;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.mindmap.ui.NoteAndDetailsComponentExtension;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Sebastian Solomon
	 * @author Mariana Gheorghe
	 * @author Alexandra Topoloaga
	 */ 
	public class MindMapNodeWithDetailsRenderer1 extends DataRenderer implements IDiagramShellContextAware {
		
		protected var embeddedRenderer:MindMapNodeRenderer2;
		protected var detailsGroup:Group;
		protected var detailsIcon:Image;
		protected var detailsText:RichText;
		protected var noteComponentExtension:NoteAndDetailsComponentExtension = new NoteAndDetailsComponentExtension();
		
		public function MindMapNodeWithDetailsRenderer1() {
			var vLayout:VerticalLayout = new VerticalLayout();
			vLayout.gap = 2;
			this.layout = vLayout;
			
			addEventListener(ResizeEvent.RESIZE, resizeHandler);
		}
		
		public function get diagramShellContext():DiagramShellContext {
			return embeddedRenderer.diagramShellContext;
		}
		
		public function set diagramShellContext(value:DiagramShellContext):void {
			embeddedRenderer.diagramShellContext = value;
		}
		
		public function getEmbeddedRenderer():MindMapNodeRenderer2 {
			return embeddedRenderer;
		}
		
		/**
		 * Duplicate from AbstractMindMapNodeRenderer.
		 */
		override public function set data(value:Object):void {
			if (data != null) {
				endModelListen();
			}
			embeddedRenderer.data = value;
			super.data = value;
			if (data != null) {
				beginModelListen();
			}
		}
		
		/**
		 * Duplicate from AbstractMindMapNodeRenderer.
		 */
		protected function beginModelListen():void {
			data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);	
			modelChangedHandler(null);
		}
		
		/**
		 * Duplicate from AbstractMindMapNodeRenderer.
		 */
		protected function endModelListen():void {
			data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}
		
		protected function get mindMapDiagramShell():MindMapEditorDiagramShell {
			return MindMapEditorDiagramShell(embeddedRenderer.diagramShellContext.diagramShell);	
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			createEmbeddedRenderer();
			addElement(embeddedRenderer);
			
			createDetailsGroup();
			addElement(detailsGroup);
		}
		
		private function createEmbeddedRenderer():void {
			embeddedRenderer = new MindMapNodeRenderer2();
			embeddedRenderer.percentWidth = 100;
			embeddedRenderer.addEventListener(FlexEvent.INITIALIZE, nodeRendererInitialized);
		}
		
		/**
		 * Must be added after its own listeners are registered on the embedded renderer.
		 */
		private function nodeRendererInitialized(event:FlexEvent):void {	
			embeddedRenderer.addEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
			embeddedRenderer.addEventListener(MouseEvent.MOUSE_OUT, mouseOutHandler);
			embeddedRenderer.addEventListener(ResizeEvent.RESIZE, resizeHandler);
		}
		
		private function createDetailsGroup():void {
			detailsGroup = new Group();
			detailsGroup.layout = new HorizontalLayout();
			detailsGroup.percentWidth = 100;
			
			detailsText = new RichText();
			detailsText.percentWidth = 100;
			
			detailsIcon = new Image();
			detailsIcon.addEventListener(MouseEvent.CLICK, detailsIconClickHandler);
			
			detailsGroup.addElement(detailsIcon);
			detailsGroup.addElement(detailsText);
			
			detailsText.visible = false;
			detailsText.includeInLayout = false;
		}
		
		protected function detailsIconClickHandler(event:MouseEvent=null):void {
			if (detailsText.includeInLayout) {
				detailsText.visible = false;
				detailsText.includeInLayout = false;
				detailsIcon.source = Resources.arrowDownIcon;
			} else {
				detailsText.includeInLayout = true;
				detailsText.visible = true;
				detailsIcon.source = Resources.arrowUpIcon;				
			}
			
			if (detailsText.includeInLayout) {
				var text:String = node.properties[MindMapConstants.NODE_DETAILS] as String;
				text = Utils.getCompatibleHTMLText(text);
				detailsText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
			}
			
			mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "detailsTextVisible", detailsText.includeInLayout ? 1 : 0);	
		}
		
		protected function modelChangedHandler(event:PropertyChangeEvent = null):void {
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
			}
		}
		
		private function setDetailsGroupVisibile(value:Boolean):void {
			detailsGroup.includeInLayout = value;
			detailsGroup.visible = value;
		}
		
		protected function get node():Node {
			return Node(data);	
		}
		
		protected function mouseOverHandler(event:MouseEvent):void {
			if (noteComponentExtension.parent == null) {
				var text:String;
				var hasText:Boolean = false;
				if (hasNodeDetails(node) && !detailsText.includeInLayout) {
					text = node.properties[MindMapConstants.NODE_DETAILS] as String;
					text = Utils.getCompatibleHTMLText(text);
					noteComponentExtension.nodeDetails.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
					noteComponentExtension.nodeDetails.includeInLayout = true;
					noteComponentExtension.nodeDetails.visible = true;
					hasText = true;
				} else {
					noteComponentExtension.nodeDetails.includeInLayout = false;
					noteComponentExtension.nodeDetails.visible = false;
				}
				
				if (hasNote(node)) {
					text = node.properties[MindMapConstants.NOTE] as String
					text = Utils.getCompatibleHTMLText(text);
					noteComponentExtension.noteText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
					noteComponentExtension.noteIcon.includeInLayout = true;
					noteComponentExtension.noteText.includeInLayout = true;
					hasText = true;
				} else {
					noteComponentExtension.noteIcon.includeInLayout = false;
					noteComponentExtension.noteText.includeInLayout = false;
				}
				
				if (hasText) {
					DiagramRenderer(mindMapDiagramShell.diagramRenderer).addElement(noteComponentExtension)
					
					var dynamicObject:Object = mindMapDiagramShell.getDynamicObject(diagramShellContext, node);
					noteComponentExtension.x = dynamicObject.x ;
					noteComponentExtension.y = dynamicObject.y + dynamicObject.height;	
				}
			}
		}
		
		private function hasNote(node:Node):Boolean {
			return node.properties.hasOwnProperty(MindMapConstants.NOTE) && String(node.properties[MindMapConstants.NOTE]).length > 0
		}
		
		private function hasNodeDetails(node:Node):Boolean {
			return node.properties.hasOwnProperty(MindMapConstants.NODE_DETAILS) && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0
		}
		
		protected function mouseOutHandler(event:MouseEvent):void {	
			if (noteComponentExtension.parent != null) {
				DiagramRenderer(diagramShellContext.diagramShell.diagramRenderer).removeElement(noteComponentExtension);
			}				
		}
		
		/**
		 * Duplicate from NodeRenderer and AbstractMindMapNodeRenderer.
		 */
		protected function resizeHandler(event:ResizeEvent):void {
			if (height == 0 || width == 0) {
				// don't change values if first resize, wait until component fully initialized
				return;
			}
			var refresh:Boolean = false;
			if (mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "width") != width) {
				mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "width", width);
				refresh = true;
			}
			if (mindMapDiagramShell.getPropertyValue(diagramShellContext, data, "height") != height) {			
				mindMapDiagramShell.setPropertyValue(diagramShellContext, data, "height", height);
				refresh = true;
			}
			
			if (refresh) {					
				mindMapDiagramShell.shouldRefreshModelPositions(diagramShellContext, mindMapDiagramShell.rootModel);
				mindMapDiagramShell.shouldRefreshVisualChildren(diagramShellContext, mindMapDiagramShell.rootModel);
			}
		}
		
	}
}