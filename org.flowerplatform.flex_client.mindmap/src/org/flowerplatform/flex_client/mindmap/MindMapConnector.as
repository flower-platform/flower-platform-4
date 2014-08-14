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
package org.flowerplatform.flex_client.mindmap {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.mindmap.GenericMindMapConnector;
	import org.flowerplatform.flexutil.Utils;

   /**
	* @author Sebastian Solomon
	*/
	public class MindMapConnector extends GenericMindMapConnector {
		
		override protected function getColor():uint {
			if (!Node(target).properties.hasOwnProperty(MindMapConstants.EDGE_COLOR) && 
				!Node(source).properties.hasOwnProperty(MindMapConstants.EDGE_COLOR)) {
				return super.getColor();
			}
			
			if (Node(source).properties[MindMapConstants.EDGE_COLOR] != null) {
				return Utils.convertValueToColor(Node(source).getPropertyValue(MindMapConstants.EDGE_COLOR));
			} else {
				return Utils.convertValueToColor(Node(target).getPropertyValue(MindMapConstants.EDGE_COLOR));
			}
		}
		
		override protected function getEdgeStyle():String {
			if (Node(source).getPropertyValue(MindMapConstants.EDGE_STYLE) != null) {
				return Node(source).getPropertyValue(MindMapConstants.EDGE_STYLE);
			} else {
				var style:String =Node(target).getPropertyValue(MindMapConstants.EDGE_STYLE)
				return style == null ? super.getEdgeStyle() : style;
			}
		}
			
		override protected function getEdgeWidth():int {
			if (!Node(target).properties.hasOwnProperty(MindMapConstants.EDGE_WIDTH) && 
				!Node(source).properties.hasOwnProperty(MindMapConstants.EDGE_WIDTH)) {
				return super.getEdgeWidth();
			}
			
			if (Node(source).getPropertyValue(MindMapConstants.EDGE_WIDTH) != 0) {
				return Node(source).getPropertyValue(MindMapConstants.EDGE_WIDTH);
			} else {
				return Node(target).getPropertyValue(MindMapConstants.EDGE_WIDTH);
			}
		}
		
	}
}
