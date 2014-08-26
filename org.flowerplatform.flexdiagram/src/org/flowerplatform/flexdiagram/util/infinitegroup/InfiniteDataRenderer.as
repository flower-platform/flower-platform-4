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
package org.flowerplatform.flexdiagram.util.infinitegroup {
	import flash.events.Event;
	import flash.geom.Rectangle;
	
	import mx.events.PropertyChangeEvent;
	
	import spark.components.DataRenderer;
	import spark.components.Group;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InfiniteDataRenderer extends DataRenderer {
				
		private var _contentRect:Rectangle;
				
		/**
		 * The group's coordonates and dimensions.
		 * 
		 * <p>
		 * Important: don't set this property as bindable -> it will dispatch update event even if values are equal -> infinite loop in updateDisplayList.
		 */ 
		public function get contentRect():Rectangle {       
			return _contentRect;
		}
		
		public function set contentRect(value:Rectangle):void {
			// don't use == to verify if they are equals (it doesn't work)
			if (_contentRect != null && value.equals(_contentRect)) {		
				return;
			}
			var oldContentRect:Rectangle = _contentRect;
			_contentRect = value;			
			
			// sometimes (@see https://github.com/flower-platform/flower-platform-4/issues/81), it enters in an infinite loop
			// be aware when using multiple InfiniteDataRenderer that sets this contentRect in updateDisplayList.
			// problem mentioned also here: http://apache-flex-users.2333346.n4.nabble.com/Infinite-recursion-in-custom-layout-td5148.html
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "contentRect", oldContentRect, _contentRect));
		}
		
		private var _scrollByToolInProgress:Boolean = false;
		
		/**
		 * Must be modified only when working on mobile.
		 * If <code>true</code> (the scrolling will be done by move/resize tool),
		 * scrollers will not be displayed. 
		 * 
		 * @see InfiniteScrollerLayout
		 */ 
		[Bindable]
		public function get scrollByToolInProgress():Boolean {       
			return _scrollByToolInProgress;
		}
		
		public function set scrollByToolInProgress(value:Boolean):void {
			if (value == _scrollByToolInProgress)
				return;
			
			_scrollByToolInProgress = value;
		}
		
		public function InfiniteDataRenderer() {
			super();
			
			layout = new InfiniteGroupBasicLayout();			
		}
	
		public function getMaxVerticalScrollPosition():Number {
			return contentRect.height + Math.abs(contentRect.y) - height;
		}
		
		public function getMaxHorizontalScrollPosition():Number {
			return contentRect.width + Math.abs(contentRect.x) - width;
		}
		
		/**
		 * Don't allow changing contentWidth/contentHeight.
		 * 
		 * <p>
		 * The ones from <code>contentRect</code> must be used.
		 */ 
		override public function setContentSize(width:Number, height:Number):void {
			width = _contentRect ? _contentRect.width : width;
			height = _contentRect ? _contentRect.height : height;
				
			super.setContentSize(width, height);
		}
	}
}