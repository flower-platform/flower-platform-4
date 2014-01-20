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
package  com.crispico.flower.flexdiagram.util.common {
	
	import com.crispico.flower.flexdiagram.util.imagefactory.ImageFactory;
	
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	/**
	 * Besides UIComponent functionality, the class provides a wrapper for a bitmap.
	 * The wrapper is used in case we want to create a component that must have an image attached.
	 * 
	 * <p>The class extends a <code>UIComponent</code> because <code>Container</code> or <code>Box<code> didn't work. 
	 * The problem was that the last two only accepts <code>IUIComponent<code>s as their children.</p>
	 * 
	 * <p>The getters for measures were overridden so that the bitmap container will take its child's measures.
	 * It seems that when the child is instantiated, the parent's measures aren't automatically updated.</p>
	 * 
	 * @author Cristina
	 * 
	 */
	public class BitmapContainer extends UIComponent  {
		
		private var _bitmap:Bitmap;
		
		private var imageURLs:ArrayCollection = new ArrayCollection(); /* of Strings */
		
		/**
		 * Initialize the <code>bitmap</code> child and tries to retrieve its icon from a list of URLs.
		 * 
		 */
		public function BitmapContainer(value:Object = null):void {
			_bitmap = new Bitmap();
			addChild(_bitmap);
			if (value != null) {
				retrieveImage(value);
			}			
		}
		
		/**
		 * Sets the bitmapData property to the Bitmap.
		 * 
		 */
		public function set bitmapData(value:BitmapData):void {
			if (value == ImageFactory.INSTANCE.unavailableBitmapData && _bitmap.bitmapData != null)
				return; // We do not show the unloaded image if one already exists. 
			_bitmap.bitmapData = value;	
			invalidateSize();						
		}
		
		public override function get measuredWidth():Number {
			return _bitmap.width;
		}
		
		public override function get measuredHeight():Number {
			return _bitmap.height;
		}
		
		public override function get width():Number {
			return _bitmap.width;
		}
		
		public override function get height():Number {
			return _bitmap.height;
		}
		
		/**
		 * Returns an <code>ArrayCollection</code> containing the image URLs.
		 * 
		 * <p>
		 * Can be set with: <code>null</code>, a <code>String</code> representing an URL of
		 * an image, or an <code>ArrayCollection</code> containing the image URLs. The setter
		 * delegates to <code>retrieveImage()</code>, but is in the form of a property to be
		 * used in item renderers using binding.
		 * 
		 * @author Cristi
		 */ 
		public function get source():Object {
			return imageURLs;
		}
		
		public function set source(value:Object):void {
			retrieveImage(value);
		}
		
		/**
		 * Given an array of URLs, it retrieves the icon for the <code>bitmapData</code> property.
		 * 
		 */
		public function retrieveImage(value:Object):void {
			if (value == null) {
				return;
			}
			var list:ArrayCollection = new ArrayCollection();
			if (value is String || value is Class) {
				list.addItem(value);
			} else {
				list.addAll(ArrayCollection(value));
			}
			// if the new array of URLs is different from the old one
			// stores it and retrive the new image
			if (differentImageURLs(list)) {
				imageURLs = list;
				ImageFactory.INSTANCE.retrieveImage(imageURLs, this, "bitmapData");
			}
		}
		
		/**
		 * Compares imageURLs with a new array of URLs.
		 */ 
		private function differentImageURLs(value:ArrayCollection):Boolean {
			if (imageURLs.length != value.length)
				return true;
			for each (var url:String in value) {
				if (!imageURLs.contains(url))
					return true;
			}
			return false;
		}
	}
	
}