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
package org.flowerplatform.flexutil.samples.global_menu {
	import org.flowerplatform.flexutil.action.ComposedAction;
	
	/**
	 * Composed action sample that reacts to selection.
	 * <p>Also it disables itself when the disableFor is selected</p>
	 * 
	 * @author Mircea Negreanu
	 */
	public class SampleActionGlobalMenuC1 extends ComposedAction {
		public var disableFor:String = null;
		
		public function SampleActionGlobalMenuC1() {
			super();
		}
		
		override public function get label():String {
			if (super.label != null) {
				return super.label;
			}
			if (selection == null || selection.length == 0) {
				return "Action for: empty";
			} else {
				var sel:Object = selection.getItemAt(0);
				var result:Object;
				if (sel is XML) {
					result = "XML";
				} else {
					result = sel;
				}
				return "Action for: " + result;
			}
		}
		
		override public function get enabled():Boolean {
			if (disableFor == null || selection == null || selection.length == 0) {
				return true;
			} else if (selection.getItemAt(0) == disableFor) {
				return false;
			}
			
			return true;
		}
	}
}