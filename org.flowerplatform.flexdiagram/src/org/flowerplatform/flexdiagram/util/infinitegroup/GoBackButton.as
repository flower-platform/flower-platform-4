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
	
	import flash.events.MouseEvent;
	import flash.geom.Rectangle;
	
	import spark.components.Button;
	import spark.core.IViewport;
	
	/**
	 * The "Go Back" button displayed in case of a "forced" scrolling.
	 * 
	 * @see InfiniteScrollerSkin
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class GoBackButton extends Button {
		
		// available positions
		public static const MIDDLE:int = 0;
		public static const TOP_LEFT:int = 1;
		public static const TOP_RIGHT:int = 2;
		public static const BOTTOM_LEFT:int = 3;
		public static const BOTTOM_RIGHT:int = 4;
		public static const TOP:int = 5;
		public static const BOTTOM:int = 6;
		public static const LEFT:int = 7;
		public static const RIGHT:int = 8;
	
		public var position:int;
		
		public var viewport:InfiniteDataRenderer;
		
		public function GoBackButton() {
			addEventListener(MouseEvent.CLICK, mouseClickHandler);	
		}
		
		private function mouseClickHandler(event:MouseEvent):void {
			// reset scroll positions						
			var x:Number = viewport.contentRect.x;
			var y:Number = viewport.contentRect.y;
			switch (position) {
				case TOP_LEFT:
				case TOP_RIGHT:
				case BOTTOM_LEFT:
				case BOTTOM_RIGHT:
					viewport.horizontalScrollPosition = x;
					viewport.verticalScrollPosition = y;
					break;				
				case LEFT:
				case RIGHT:
					viewport.horizontalScrollPosition = x;
					break;			
				case TOP:
				case BOTTOM:
					viewport.verticalScrollPosition = y;
					break;				
			}			
		}
		
		/**
		 * @see InfiniteScroller.viewport_propertyChangeHandler()
		 */ 
		public function calculatePosition():void {
			if (viewport == null || viewport.contentRect == null) {
				return;
			}
			var hpos:Number = viewport.horizontalScrollPosition;
			var vpos:Number = viewport.verticalScrollPosition;
			
			var scrollRect:Rectangle = viewport.scrollRect;
			var contentRect:Rectangle = viewport.contentRect;

			
			if (contentRect.x < scrollRect.x) {
				if (contentRect.y < scrollRect.y) {
					position = BOTTOM_RIGHT;
				} else if (contentRect.y > scrollRect.y + scrollRect.height) {
					position = TOP_RIGHT;
				} else {
					position = RIGHT;
				}				
			} else if (contentRect.x + contentRect.width > scrollRect.x + scrollRect.width) {
				if (contentRect.y < scrollRect.y) {
					position = BOTTOM_LEFT;
				} else if (contentRect.y > scrollRect.y + scrollRect.height) {
					position = TOP_LEFT;
				} else {
					position = LEFT;
				}
			} else {
				if (contentRect.y < scrollRect.y) {
					position = BOTTOM;
				} else if (contentRect.y > scrollRect.y + scrollRect.height) {
					position = TOP;
				} else {
					position = MIDDLE;
				}
			}			
		}				
	}
}