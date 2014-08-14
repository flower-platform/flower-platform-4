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
package org.flowerplatform.flex_client.team.git.ui.component
{
	public class SelectableBranches {
		
		protected var _name:String;
		protected var _isSelected:Boolean;
		
		public function SelectableBranches()
		{
		}
		
		public function get name ():String {
			return _name;
		}
		
		public function set name (s:String):void {
			_name = s;
		}
		
		public function get isSelected ():Boolean {
			return _isSelected;
		}
		
		public function set isSelected (b:Boolean):void {
			_isSelected = b;
		}
	}
}