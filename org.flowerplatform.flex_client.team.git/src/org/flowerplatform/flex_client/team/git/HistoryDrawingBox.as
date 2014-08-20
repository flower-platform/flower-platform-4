/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flex_client.team.git {
	
	import mx.collections.ArrayCollection;
	
	import spark.components.gridClasses.GridItemRenderer;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;

	/**
	 *	@author Cristina Constantinescu
	 *  @author Vlad Bogdan Manica
	 */ 
	public class HistoryDrawingBox extends GridItemRenderer {
			
		public function HistoryDrawingBox() {
			clipAndEnableScrolling = true;
		}
		
		private function drawLine(x1:int, y1:int, x2:int, y2:int, width:int, color:uint):void {
			graphics.lineStyle(width, color);
			graphics.moveTo(x1, y1);
			graphics.lineTo(x2, y2);			
		}
		
		private function drawDot(x:int, y:int, w:int, h:int, foreground:uint, backgroundColor:uint):void {
			graphics.beginFill(backgroundColor);
			graphics.lineStyle(2, foreground);
			graphics.drawEllipse(x + 2, y + 1, w - 2, h - 2);
		}
			
		private function parseColor(color:String):uint {
			return uint("0x" + color.substring(1));
		}
		
		override public function set data(value:Object):void
		{
			if (super.data != value && value != null) {
				super.data = value;
				invalidateDisplayList();
			}
		}
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			if (data == null) {
				invalidateDisplayList();
				return;
			}
			
			graphics.clear();
			var drawingNode:Node = Node(Node(data).getPropertyValue(GitHistoryConstants.DRAWINGS));
			
			for (var key:String in drawingNode.properties) {
				if (key == GitHistoryConstants.DRAW_LINE) {
					var lines:Number = ArrayCollection(drawingNode.getPropertyValue(key)).length; 
					
					for (var i:Number = 1; i < lines; i += 7) {
						drawLine(
							int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(i)), int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(i+1)),
							int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(i+2)), int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(i+3)),
							int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(i+4)), parseColor(String(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(i+5))));													
					}											
				}
				else if (key == GitHistoryConstants.DRAW_COMMIT_DOT || key == GitHistoryConstants.DRAW_BOUNDARY_DOT) {
					drawDot(
						int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(1)), int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(2)),
						int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(3)), int(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(4)),
						parseColor(String(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(5))), parseColor(String(ArrayCollection(drawingNode.getPropertyValue(key)).getItemAt(6))));
				}
			}		
		}
	}	
}