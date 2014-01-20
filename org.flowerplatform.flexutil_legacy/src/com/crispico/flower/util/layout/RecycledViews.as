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
package  com.crispico.flower.util.layout {
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina
	 * 
	 */
	public class RecycledViews {
		
		/**
		 * 
		 */
		public var normalViews:Dictionary = new Dictionary();
		
		/**
		 * 
		 */
		public var editorViews:ArrayCollection = new ArrayCollection();
		
		public function isEmpty():Boolean {	
			for (var key:Object in normalViews) {
				return false;
			}
			if (editorViews.length > 0) {
				return false;
			}
			return true;
		}
		
		public static function getNormalViewKey(view:ViewLayoutData):String {
			var key:String = view.viewId;
			if (view.customData != null) {
				key += "|" + view.customData;
			}
			return key;
		}
	}
	
}