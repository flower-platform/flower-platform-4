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
package com.crispico.flower.util.list {
	
	import flash.display.Graphics;
	import flash.events.Event;
	import flash.geom.Matrix;
	
	import mx.controls.List;
	import mx.core.EdgeMetrics;
	import mx.core.ScrollPolicy;
	import mx.events.FlexEvent;
	import mx.events.ResizeEvent;
	
	/**
	 * Custom list created because there is a bug in flex when updating scrolls.
	 * <p>
	 * A hack was made so that the horizontal scroll will be updated when resizing and when
	 * the data changes.
	 * 
	 * <p>
	 * All instances of this list must provide an <code>itemLabelFunction</code>, <code>labelField</code>
	 * or must override the <code>getItemLabel</code> method.
	 * 
	 * @author Cristina
	 */ 
	public class CustomList extends List {
		
		private static const VERTICAL_SCROLL_COMPENSATION:Number = 15;
		
		/**
		 * Stores the measured width of list items. 
		 * The value is provided by list's <code>measureWidthOfItems</code> method
		 * and is calculated at initialization and when the data changes.
		 * 
		 * <p>
		 * There where some performance issues when the measured width of list items was calculated each time the window was resizing
		 * and because it doesn't change at resize event, we keep it in this attribute. 
		 */
		private var listItemsMeasureWidth:Number;
		
		/**
		 * @see Getter doc.
		 */ 
		private var _itemLabelFunction:Function;
		
		/**
		 * Function called by <code>updateHorizontalScrollBar</code> in order to get the item's label. <br>
		 * Needed when calculating the largest item in the list.
		 * <p> 
		 * The signature function should be : 
		 * 	<pre> function name_function(item:Object):String </pre>
		 * 
		 */  
		public function get itemLabelFunction():Function {
			return _itemLabelFunction;
		}
		
		public function set itemLabelFunction(value:Function):void {
			this._itemLabelFunction = value;
		}
		
		public function CustomList() {
			super();
			
			// when the container is resizing, the list's horizontal scroll bar width must be updated.
			// it doesn't work only setting list horizontalScrollPolicy to "auto"     
			addEventListener(ResizeEvent.RESIZE, resizeListEvent);
			addEventListener(FlexEvent.VALUE_COMMIT, updateHorizontalScrollBar);	
			horizontalScrollPolicy = ScrollPolicy.AUTO;
		}
				
		/**
		 * Updates the list's horizontal scroll bar.
		 * <p>
		 * If the <code>calculateMeasureWidth</code> parameter is true it calculates and stores the width of items.
		 */  		  
		public function updateHorizontalScrollBar(calculateMeasureWidth:Boolean = true):void {	
			if (calculateMeasureWidth) {        		
				var maxItemLabelLength:Number = 0;
				var maxItemIndex:Number = 0;
				for (var i:Number = 0; i < dataProvider.length; i++) {					
					var label:String = getItemLabel(dataProvider.getItemAt(i));
					if (label.length > maxItemLabelLength) {
						maxItemLabelLength = label.length;
						maxItemIndex = i;
					}
				}       		
				listItemsMeasureWidth = measureWidthOfItems(maxItemIndex, 1);           		
			}			
			var maxHorizPos:Number = listItemsMeasureWidth - width + 5;
			if (maxHorizPos < 0) {
				maxHorizPos = 0;
			} else {
				maxHorizPos += VERTICAL_SCROLL_COMPENSATION;
			}
			maxHorizontalScrollPosition = maxHorizPos;			
		}	
		
		private function resizeListEvent(event:ResizeEvent) :void {
			updateHorizontalScrollBar(false);
		}	
		
		/**
		 * If <code>_itemLabelFunction</code> set, returns its result. <br>
		 * If <code>labelField</code> set and ok to use it, returns the corresponding label.<br>
		 * If <code>item</code> is a String, returns it.
		 * <p>
		 * Otherwise, throws in error.
		 * 
		 * <p>
		 * So, in order to work properly, at least one of the above conditions must be satisfied.
		 * 
		 * @return the item label. Represents the concatenation of all labels that are used in the item renderer.
		 */ 
		protected function getItemLabel(item:Object):String {
			if (_itemLabelFunction != null) {
				return _itemLabelFunction(item);
			}
			
			try {
				if (item[labelField] != null) {
					return item[labelField];
				}
			}
			catch(e:Error){
			}
			if (item is String) {
				return String(item);
			}
			throw new Error("An item label hasn't been provided! See this method doc.");
		}
	}
}