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
package com.crispico.flower.flexdiagram.contextmenu
{
	public class SubMenuEntryModel implements IMenuEntryModel {
		
		private var _label:String;
		
		private var _image:Object;
		
		private var _sortIndex:int = FlowerContextMenu.DEFAULT_SORT_INDEX;
		
		public function SubMenuEntryModel(image:Object, label:String, sortIndex:int = int.MAX_VALUE){
			_label = label;
			_image = image;
			_sortIndex = sortIndex;
		}
		
		public function get label():String {
			return _label;
		}
		
		public function set label(value:String):void {
			_label = value;
		}
		
		public function get image():Object {
			return _image;
		}
		
		public function set image(value:Object):void {
			_image = value;
		}
		
		public function get sortIndex():int {
			return _sortIndex;
		}
		
		public function set sortIndex(value:int):void {
			this._sortIndex = value;
		}
		
	}
}