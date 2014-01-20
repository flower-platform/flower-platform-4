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
package org.flowerplatform.flexutil.shortcut {
	
	import mx.controls.Alert;

	/**
	 * @author Florin
	 * @author Cristi
	 */
	public class Shortcut {
	
		public var ctrl:Boolean; // 17 
		
		public var shift:Boolean; // 16
		
		public var lowerCaseCode:int;
		
		public var upperCaseCode:int;
		
		public function Shortcut(ctrl: Boolean, shift: Boolean, key:String) {
			this.ctrl = ctrl;
			this.shift = shift;
			this.lowerCaseCode = key.charCodeAt(0);
			this.upperCaseCode = key.toUpperCase().charCodeAt(0);
		}   

		public function equals(other:Shortcut):Boolean {
			return this.ctrl == other.ctrl && this.shift == other.shift && this.lowerCaseCode == other.lowerCaseCode;
		}
		
	}
}