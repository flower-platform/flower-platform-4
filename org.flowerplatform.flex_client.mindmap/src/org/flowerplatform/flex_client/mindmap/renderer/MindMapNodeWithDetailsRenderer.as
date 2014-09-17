/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
	
	import mx.events.PropertyChangeEvent;
	import mx.graphics.SolidColorStroke;
	
	import spark.components.Group;
	import spark.components.Image;
	import spark.components.RichText;
	import spark.layouts.HorizontalLayout;
	import spark.layouts.VerticalLayout;
	import spark.primitives.Line;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.mindmap.ui.NoteAndDetailsComponentExtension;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Sebastian Solomon
	 */ 
	public class MindMapNodeWithDetailsRenderer extends MindMapNodeRenderer {
		
		public var horizontalLine:Line;
		protected var nodeGroup:Group;
		protected var detailsGroup:Group;
		protected var detailsIcon:Image;
		protected var detailsText:RichText;
		protected var noteComponentExtension:NoteAndDetailsComponentExtension = new NoteAndDetailsComponentExtension();
		
		override public function setLayout():void {
			var vLayout:VerticalLayout = new VerticalLayout();
			vLayout.gap = 2;
			vLayout.paddingBottom = 2;
			vLayout.paddingTop = 2;
			vLayout.paddingLeft = 2;
			vLayout.paddingRight = 2;
			vLayout.verticalAlign = "middle";
			
			this.layout = vLayout;
		}
		
		override protected function createChildren():void {
			createNodeGroup();
			super.createChildren();

			createDetailsGroup();
			createHorizontalLine();
			
			addElement(nodeGroup);
			addElement(horizontalLine);
			addElement(detailsGroup);
		}
		
		override protected function setHitArea(unscaledWidth:Number, unscaledHeight:Number):void {
			if (horizontalLine.visible) {
				super.setHitArea(unscaledWidth, horizontalLine.y);
			} else {
				super.setHitArea(unscaledWidth, unscaledHeight);
			}
			nodeGroup.hitArea = hitArea;
		}

		private function createNodeGroup():void {
			var hLayout:HorizontalLayout = new HorizontalLayout();
			hLayout.gap = 2;
			hLayout.paddingBottom = 2;
			hLayout.paddingTop = 2;
			hLayout.paddingLeft = 2;
			hLayout.paddingRight = 2;
			hLayout.verticalAlign = "middle";
			
			nodeGroup = new Group();
			nodeGroup.layout = hLayout;
		}
		
		private function createHorizontalLine():void {
			horizontalLine = new Line();
			horizontalLine.percentWidth = 100;
			var colorStroke:SolidColorStroke = new SolidColorStroke();
			colorStroke.color = 808080;
			colorStroke.weight = 1;
			horizontalLine.stroke = colorStroke;
		}
		
		private function createDetailsGroup():void {
			detailsGroup = new Group();
			detailsGroup.layout = new HorizontalLayout();;
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
				invalidateDisplayList();
			} else {
				detailsText.includeInLayout = true;
				detailsText.visible = true;
				detailsIcon.source = Resources.arrowUpIcon;				
			}
		}
			
		override public function getMainComponent():Group {
			return nodeGroup;
		}
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
//			switch (event.property) {
//				case MindMapConstants.NODE_DETAILS:
					if (node.properties[MindMapConstants.NODE_DETAILS] != null && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0) {
						setDetailsGroupVisibile(true);
						if (detailsText.includeInLayout) {
							detailsIcon.source = Resources.arrowUpIcon;
						} else {
							detailsIcon.source = Resources.arrowDownIcon;
						}
						
						var text:String = node.properties[MindMapConstants.NODE_DETAILS] as String;
						text = Utils.getCompatibleHTMLText(text);
						detailsText.textFlow = TextConverter.importToFlow(text , Utils.isHTMLText(text) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);
					} else {
						setDetailsGroupVisibile(false);
					}
//					break;
//			}			
		}
		
		private function setDetailsGroupVisibile(value:Boolean):void {
			horizontalLine.includeInLayout = value;
			horizontalLine.visible = value;
			detailsGroup.includeInLayout = value;
			detailsGroup.visible = value;
		}
		
		override protected function mouseOverHandler(event:MouseEvent):void {
			super.mouseOverHandler(event);
			if (noteComponentExtension.parent == null) {
				var text:String;
				var hasText:Boolean = false;
				if (hasNoteDetails(node) && !detailsText.includeInLayout) {
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
					if (horizontalLine.visible) {
						noteComponentExtension.y = dynamicObject.y + horizontalLine.y;
					} else {
						noteComponentExtension.y = dynamicObject.y + dynamicObject.height;	
					}
				}
			}
		}
		
		override protected function drawLittleCircle(y:Number=NaN):void {
			if (horizontalLine.visible) {
				super.drawLittleCircle(horizontalLine.y/2);
			} else {
				super.drawLittleCircle();
			}
		}
		
		private function hasNote(node:Node):Boolean {
			return node.properties.hasOwnProperty(MindMapConstants.NOTE) && String(node.properties[MindMapConstants.NOTE]).length > 0
		}
		
		private function hasNoteDetails(node:Node):Boolean {
			return node.properties.hasOwnProperty(MindMapConstants.NODE_DETAILS) && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0
		}
		
		override public function newIconIndex():int {			
			return nodeGroup.numElements - 1; // add icon before label
		}
		
		override protected function mouseOutHandler(event:MouseEvent):void {	
			super.mouseOutHandler(event);
			if (noteComponentExtension.parent != null) {
				DiagramRenderer(diagramShellContext.diagramShell.diagramRenderer).removeElement(noteComponentExtension);
			}				
		}
		
	}
		
}
