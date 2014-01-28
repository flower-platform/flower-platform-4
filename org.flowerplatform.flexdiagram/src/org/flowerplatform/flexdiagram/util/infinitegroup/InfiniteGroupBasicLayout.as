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
package org.flowerplatform.flexdiagram.util.infinitegroup {
	import flash.geom.Rectangle;
	
	import spark.components.supportClasses.GroupBase;
	import spark.core.NavigationUnit;
	import spark.layouts.BasicLayout;

	/**
	 * Represents the layout used by an InfiniteGroup.
	 * 
	 * <p>
	 * Methods were modified to allow setting negative values to scrollers.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class InfiniteGroupBasicLayout extends BasicLayout {
		
		override public function getHorizontalScrollPositionDelta(navigationUnit:uint):Number {
			var g:GroupBase = target;
			if (!g)
				return 0;     
			
			var scrollRect:Rectangle = getScrollRect();
			if (!scrollRect)
				return 0;
			
			// Special case: if the scrollRect's origin is 0,0 and it's bigger 
			// than the target, then there's no where to scroll to
			if ((scrollRect.x == 0) && (scrollRect.width >= g.contentWidth))
				return 0;  
			
			// maxDelta is the horizontalScrollPosition delta required 
			// to scroll to the END and minDelta scrolls to HOME. 
			var maxDelta:Number = g.contentWidth - scrollRect.right;
			if (maxDelta < 0) { 
				maxDelta = 1;
			}
			var minDelta:Number = -scrollRect.left;
			if (scrollRect.left <= 0) {
				minDelta = -1;
			}
			var getElementBounds:Rectangle;
			switch(navigationUnit) {
				case NavigationUnit.LEFT:
				case NavigationUnit.PAGE_LEFT:
					// Find the bounds of the first non-fully visible element
					// to the left of the scrollRect.
					getElementBounds = getElementBoundsLeftOfScrollRect(scrollRect);
					break;
				
				case NavigationUnit.RIGHT:
				case NavigationUnit.PAGE_RIGHT:
					// Find the bounds of the first non-fully visible element
					// to the right of the scrollRect.
					getElementBounds = getElementBoundsRightOfScrollRect(scrollRect);
					break;
				
				case NavigationUnit.HOME: 
					return minDelta;
					
				case NavigationUnit.END: 
					return maxDelta;
					
				default:
					return 0;
			}
			
			if (!getElementBounds)
				return 0;
			
			var delta:Number = 0;
			switch (navigationUnit) {
				case NavigationUnit.LEFT:
					// Snap the left edge of element to the left edge of the scrollRect.
					// The element is the the first non-fully visible element left of the scrollRect.
					delta = Math.max(getElementBounds.left - scrollRect.left, -scrollRect.width);
					break;    
				case NavigationUnit.RIGHT:
					// Snap the right edge of the element to the right edge of the scrollRect.
					// The element is the the first non-fully visible element right of the scrollRect.
					delta = Math.min(getElementBounds.right - scrollRect.right, scrollRect.width);
					break;    
				case NavigationUnit.PAGE_LEFT:
				{
					// Snap the right edge of the element to the right edge of the scrollRect.
					// The element is the the first non-fully visible element left of the scrollRect. 
					delta = getElementBounds.right - scrollRect.right;
					
					// Special case: when an element is wider than the scrollRect,
					// we want to snap its left edge to the left edge of the scrollRect.
					// The delta will be limited to the width of the scrollRect further below.
					if (delta >= 0)
						delta = Math.max(getElementBounds.left - scrollRect.left, -scrollRect.width);  
				}
					break;
				case NavigationUnit.PAGE_RIGHT:	
				{
					// Align the left edge of the element to the left edge of the scrollRect.
					// The element is the the first non-fully visible element right of the scrollRect.
					delta = getElementBounds.left - scrollRect.left;
					
					// Special case: when an element is wider than the scrollRect,
					// we want to snap its right edge to the right edge of the scrollRect.
					// The delta will be limited to the width of the scrollRect further below.
					if (delta <= 0)
						delta = Math.min(getElementBounds.right - scrollRect.right, scrollRect.width);
				}
					break;
			}
			
			// Makse sure we don't get out of bounds. Also, don't scroll 
			// by more than the scrollRect width at a time.
			return Math.min(maxDelta, Math.max(minDelta, delta));
		}
	
		override public function getVerticalScrollPositionDelta(navigationUnit:uint):Number {
			var g:GroupBase = target;
			if (!g)
				return 0;     
			
			var scrollRect:Rectangle = getScrollRect();
			if (!scrollRect)
				return 0;
			
			// Special case: if the scrollRect's origin is 0,0 and it's bigger 
			// than the target, then there's no where to scroll to
			if ((scrollRect.y == 0) && (scrollRect.height >= g.contentHeight))
				return 0;  
			
			// maxDelta is the horizontalScrollPosition delta required 
			// to scroll to the END and minDelta scrolls to HOME. 
			var maxDelta:Number = g.contentHeight - scrollRect.bottom;
			if (maxDelta < 0) {
				maxDelta = 1;
			}
			var minDelta:Number = -scrollRect.top;
			if (scrollRect.top <= 0) {
				minDelta = -1;
			}
			
			var getElementBounds:Rectangle;
			switch(navigationUnit)
			{
				case NavigationUnit.UP:
				case NavigationUnit.PAGE_UP:
					// Find the bounds of the first non-fully visible element
					// that spans right of the scrollRect.
					getElementBounds = getElementBoundsAboveScrollRect(scrollRect);
					break;
				
				case NavigationUnit.DOWN:
				case NavigationUnit.PAGE_DOWN:
					// Find the bounds of the first non-fully visible element
					// that spans below the scrollRect.
					getElementBounds = getElementBoundsBelowScrollRect(scrollRect);
					break;
				
				case NavigationUnit.HOME: 
					return minDelta;
					
				case NavigationUnit.END: 
					return maxDelta;
					
				default:
					return 0;
			}
			
			if (!getElementBounds)
				return 0;
			
			var delta:Number = 0;
			switch (navigationUnit)
			{
				case NavigationUnit.UP:
					// Snap the top edge of element to the top edge of the scrollRect.
					// The element is the the first non-fully visible element above the scrollRect.
					delta = Math.max(getElementBounds.top - scrollRect.top, -scrollRect.height);
					break;    
				case NavigationUnit.DOWN:
					// Snap the bottom edge of the element to the bottom edge of the scrollRect.
					// The element is the the first non-fully visible element below the scrollRect.
					delta = Math.min(getElementBounds.bottom - scrollRect.bottom, scrollRect.height);
					break;    
				case NavigationUnit.PAGE_UP:
				{
					// Snap the bottom edge of the element to the bottom edge of the scrollRect.
					// The element is the the first non-fully visible element below the scrollRect. 
					delta = getElementBounds.bottom - scrollRect.bottom;
					
					// Special case: when an element is taller than the scrollRect,
					// we want to snap its top edge to the top edge of the scrollRect.
					// The delta will be limited to the height of the scrollRect further below.
					if (delta >= 0)
						delta = Math.max(getElementBounds.top - scrollRect.top, -scrollRect.height);  
				}
					break;
				case NavigationUnit.PAGE_DOWN:
				{
					// Align the top edge of the element to the top edge of the scrollRect.
					// The element is the the first non-fully visible element below the scrollRect.
					delta = getElementBounds.top - scrollRect.top;
					
					// Special case: when an element is taller than the scrollRect,
					// we want to snap its bottom edge to the bottom edge of the scrollRect.
					// The delta will be limited to the height of the scrollRect further below.
					if (delta <= 0)
						delta = Math.min(getElementBounds.bottom - scrollRect.bottom, scrollRect.height);
				}
					break;
			}
			
			return Math.min(maxDelta, Math.max(minDelta, delta));
		}
	}
}