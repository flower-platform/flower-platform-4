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
package org.flowerplatform.flexutil.shortcut {

	import flash.ui.Keyboard;
	import flash.ui.KeyboardType;
	
	import mx.controls.Alert;
	import mx.utils.StringUtil;
	
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.Utils;

	/**
	 * @author Florin
	 * @author Cristi
	 * @author Cristina Constantinescu
	 */
	public class Shortcut {
	
		public var ctrlKey:Boolean;		
		public var shiftKey:Boolean;
		public var altKey:Boolean;
		
		public var keyCode:uint;
		
		public function Shortcut(ctrlKey:Boolean, shiftKey:Boolean, altKey:Boolean, keyCode:uint) {
			this.ctrlKey = ctrlKey;
			this.shiftKey = shiftKey;
			this.altKey = altKey;			
			this.keyCode = keyCode;
		}

		public function equals(other:Object):Boolean {
			if (this == other) {
				return true;
			}
			if (!(other is Shortcut) || other == null) {
				return false;
			}
			return this.ctrlKey == other.ctrlKey && this.shiftKey == other.shiftKey && this.altKey == other.altKey && this.keyCode == other.keyCode;
		}
		
		public function toString():String {
			var keyName:Object = Utils.getKeyNameFromKeyCode(keyCode);
			if (keyName == null) {
				return null;
			}
			
			var shortcut:String = "";
			var delimiter:String = FlexUtilAssets.INSTANCE.getMessage("shortcut.delimiter");
			if (ctrlKey) {
				shortcut += FlexUtilAssets.INSTANCE.getMessage("keyboard.ctrl") + delimiter;
			}
			if (altKey) {
				shortcut += FlexUtilAssets.INSTANCE.getMessage("keyboard.alt") + delimiter;
			}
			if (shiftKey) {
				shortcut += FlexUtilAssets.INSTANCE.getMessage("keyboard.shift") + delimiter;
			}				
			shortcut += keyName;
			
			return shortcut;
		}
		
	}
}