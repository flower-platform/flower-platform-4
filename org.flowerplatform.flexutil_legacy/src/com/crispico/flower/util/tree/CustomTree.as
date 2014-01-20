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
package  com.crispico.flower.util.tree {
	import com.crispico.flower.util.tree.checkboxtree.CheckBoxTree;
	
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.geom.Rectangle;
	
	import mx.core.EdgeMetrics;
	import mx.core.ScrollPolicy;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * The class extends the Tree component by fixing the  horizontal scroll bar flex bug
	 * (if tree horizontalScrollPolicy set to AUTO, you never get a scroll bar when you lower the width of the Tree).
	 * 
	 * @author Cristina
	 * 
	 */
	public class CustomTree extends CheckBoxTree {
		
		public function CustomTree() {
			super();
            horizontalScrollPolicy = ScrollPolicy.AUTO;
		}
		
		public override function get maxHorizontalScrollPosition():Number {
			if (isNaN(mx_internal::_maxHorizontalScrollPosition))
                 return 0;
              
            return mx_internal::_maxHorizontalScrollPosition;
		}
		
		public override function set maxHorizontalScrollPosition(value:Number):void {
			 mx_internal::_maxHorizontalScrollPosition = value;
             dispatchEvent(new Event("maxHorizontalScrollPositionChanged"));
           
             scrollAreaChanged = true;
             invalidateDisplayList();
		}
		
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			// we call measureWidthOfItems to get the max width of the item renderers.
            // then we see how much space we need to scroll, setting maxHorizontalScrollPosition appropriately 	
			var diffWidth:Number = measureWidthOfItems(0,0) - (unscaledWidth - viewMetrics.left - viewMetrics.right) + 50; 
           
            if (diffWidth <= 0) 
            	maxHorizontalScrollPosition = NaN; 
            else 
				maxHorizontalScrollPosition = diffWidth;
			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
		}
		
		mx_internal override function addClipMask(layoutChanged:Boolean):void {
			var vm:EdgeMetrics = viewMetrics;
			
			if (horizontalScrollBar && horizontalScrollBar.visible)
			vm.bottom -= horizontalScrollBar.minHeight;
			
			if (verticalScrollBar && verticalScrollBar.visible)
			vm.right -= verticalScrollBar.minWidth;
			
			listContent.scrollRect = new Rectangle(
			0, 0,
			unscaledWidth - vm.left - vm.right,
			listContent.heightExcludingOffsets);
		}
		
		/**
		 * By default a flex mx tree, when a letter is pressed, it searchs the tree nodes
		 * that starts with that letter and change the selection to one of them.
		 * 
		 * Usual the apllications have special behavior for ctrl + s combination. (saving the selected item)
		 * But even for this combination, the mx tree will jump to a node with a label starting with "s", so 
		 * if the application will try saving the current selected node it will not save the desired node (initial selected), 
		 * but it will save the new selected node, the one that starts with "s".
		 * 
		 * This solution was adopted for fixing the bugg found on ProjectExplorerTree 
		 * (could not save an item with ctrl + s)
		 * 
		 * @author Daniela
		 */ 
		
		 override protected function keyDownHandler(event:KeyboardEvent):void {
			 if (event.ctrlKey && !event.shiftKey && (event.charCode == "s".charCodeAt(0) || event.charCode == "S".charCodeAt(0)))
			 	return;
			 else
			 	super.keyDownHandler(event);
		 }
	}
	
}